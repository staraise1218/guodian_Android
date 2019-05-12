package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smile.guodian.R;
import com.smile.guodian.UserDao;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class FastLoginActivity extends BaseActivity {

    @BindView(R.id.fast_login_account)
    EditText account;
    @BindView(R.id.fast_login_password)
    EditText verify;
    @BindView(R.id.reset_password)
    TextView getVerify;
    @BindView(R.id.fast_login_commit)
    Button commit;
    @BindView(R.id.webView)
    WebView webView;

    private String code;
    private String phone;
    Timer timer = new Timer();
    private int duration = 60;

    @OnClick({R.id.fast_login_back, R.id.fast_login_commit, R.id.reset_password, R.id.fast_login_no_password})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.fast_login_back:
                this.finish();
                break;
            case R.id.fast_login_commit:
                login();
//                Intent intent = new Intent(FastLoginActivity.this, SetPasswordActivity.class);
//                intent.putExtra("code", code);
//                intent.putExtra("phone", phone);
//                startActivity(intent);
                break;
            case R.id.reset_password:
                timer.schedule(task, 1000, 1000);
                getVerify.setEnabled(false);
                getVerif();
                break;
            case R.id.fast_login_no_password:
                final View messageView = LayoutInflater.from(this).inflate(R.layout.dialog_message, null);
                TextView ok = (TextView) messageView.findViewById(R.id.dialog_ok);
                TextView cancel = (TextView) messageView.findViewById(R.id.dialog_cancel);
                TextView close = (TextView) messageView.findViewById(R.id.dialog_close);
                final PopupWindow popupWindow = new PopupWindow(messageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                popupWindow.setOutsideTouchable(true);

                View parent = LayoutInflater.from(this).inflate(R.layout.activity_register, null);
                popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
                //popupWindow在弹窗的时候背景半透明
                final WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 0.5f;
                getWindow().setAttributes(params);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        params.alpha = 1.0f;
                        getWindow().setAttributes(params);
                    }
                });


                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                break;
        }

    }

    public void login() {
        String phon = account.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("mobile", phon);
        params.put("code", code);

        OkHttp.post(this, HttpContants.BASE_URL + "/api/auth/freeLogin", params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
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


                Gson gson = new Gson();
                User user = gson.fromJson(data.toString(), User.class);
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
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadUrl("http://guodian.staraise.com.cn/page/empty.html?user_id=" + uid);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
//                            if (where == null) {
                            Intent intent = new Intent(FastLoginActivity.this, MainActivity.class);
                            startActivity(intent);
//                            } else {
//                                LoginActivity.this.finish();
//                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(FastLoginActivity.this, msg);
//                System.out.println(state + "-" + msg);
            }
        });
    }

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    duration--;
//                    System.out.println(duration);
                    getVerify.setText("重新获取(" + duration + ")");
                    if (duration < 2) {
                        getVerify.setEnabled(true);
                        timer.cancel();
//                        jumpActivity();
                    }
                }
            });

        }
    };

    public void getVerif() {

        String phon = account.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("mobile", phon);
        params.put("scene", "3");

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
                    ToastUtil.showShortToast(FastLoginActivity.this, code);
//                    handler.sendEmptyMessage(1);/**/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(FastLoginActivity.this, msg);
//                System.out.println(state + "-" + msg);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    verify.setText(code);
                    break;
            }
        }
    };

    @Override
    protected void init() {
        verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                code = s.toString();
                if (start == 0) {
                    commit.setEnabled(false);
                } else {
                    commit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_fast;
    }
}
