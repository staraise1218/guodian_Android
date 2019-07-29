package com.smile.guodian.ui.activity.me;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smile.guodian.R;
import com.smile.guodian.UserDao;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.ui.activity.LoginActivity;
import com.smile.guodian.ui.activity.MainActivity;
import com.smile.guodian.ui.activity.RegisterActivity;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class BindPhoneActivity extends BaseActivity {

    User user;

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.bindphone_verify)
    EditText verify;
    @BindView(R.id.bindphone_tips)
    TextView tip;
    @BindView(R.id.bindphone_phone)
    EditText phone;
    @BindView(R.id.bindphone_getVerification)
    TextView reset;
    @BindView(R.id.bindphone_commit)
    Button commit;
    private int duration = 60;      //倒计时3秒
    Timer timer = new Timer();
    private String code;
    private String unionid;
    private String nickname;
    private String icon;

    @OnClick({R.id.bindphone_can_not, R.id.bindphone_back, R.id.bindphone_commit, R.id.bindphone_getVerification})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.bindphone_back:
                BindPhoneActivity.this.finish();
                break;
            case R.id.bindphone_getVerification:
                reset.setEnabled(false);
                timer.schedule(task, 1000, 1000);
                getVerif();
                break;
            case R.id.bindphone_commit:
                bindPhone();
                break;


        }
    }


    private TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    duration--;
//                    System.out.println(duration);
                    reset.setText("重新获取(" + duration + ")");
                    if (duration < 2) {
                        timer.cancel();
                        reset.setEnabled(true);
//                        jumpActivity();
                    }
                }
            });

        }
    };

    @Override
    protected void init() {

        unionid = getIntent().getStringExtra("unionid");
        nickname = getIntent().getStringExtra("nickname");
        icon = getIntent().getStringExtra("icon");
        SpannableString spannableString = new SpannableString("首次登录将自动注册，注册即表示接受《用户协议》和《隐私保护》");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#00BFFF")), 17, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#00BFFF")), 24, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tip.setText(spannableString);
        List<User> users = BaseApplication.getDaoSession().getUserDao().loadAll();
        if (users.size() > 0) {
            user = users.get(0);
        }
        verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    commit.setEnabled(true);
                } else {
                    commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_bindphone;
    }


    public void bindPhone() {

        Map<String, String> params = new HashMap<>();
        String phon = phone.getText().toString();
        String code = verify.getText().toString();
        OkHttp.post(this, HttpContants.BASE_URL + "/Api/auth/bindMobile" + "?mobile=" + phon + "&code=" + code + "&openid=" + unionid + "&nickname=" + nickname + "&head_pic=" + icon, params, new OkCallback() {
            @Override
            public void onResponse(String response) {

                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject result = null;
                try {
                    result = object.getJSONObject("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                User user = gson.fromJson(result.toString(), User.class);
                user.setId(1L);
                UserDao userDao = BaseApplication.getDaoSession().getUserDao();
                if (userDao.loadAll().size() > 0)
                    userDao.update(user);
                else {
                    userDao.insert(user);
                }

                try {
                    String uid = user.getUser_id();
                    SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("uid", Integer.parseInt(uid));
                    editor.commit();
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                    webSettings.setDomStorageEnabled(true);
                    webView.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                            AlertDialog.Builder b = new AlertDialog.Builder(BindPhoneActivity.this);
                            b.setTitle("Alert");
                            b.setMessage(message);
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirm();
                                }
                            });
                            b.setCancelable(false);
                            b.create().show();
                            return true;
                        }

                        @Override
                        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                            return true;
                        }

                        @Override
                        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                            return true;
                        }
                    });
                    webView.loadUrl(HttpContants.BASE_URL + "/page/empty.html?user_id=" + uid);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            Intent intent = new Intent(BindPhoneActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(BindPhoneActivity.this, msg);
            }
        });
    }


    public void getVerif() {

        String phon = phone.getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phon);
        params.put("scene", "5");

        OkHttp.post(this, HttpContants.BASE_URL + "/api/auth/sendMobileCode", params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject data = null;
                try {
                    data = object.getJSONObject("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    code = data.getString("code");
//                    verify.setText(code);
//                    handler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(BindPhoneActivity.this, msg);
//                System.out.println(state + "-" + msg);
            }
        });
    }
}
