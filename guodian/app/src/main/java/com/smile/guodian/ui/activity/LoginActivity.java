package com.smile.guodian.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smile.guodian.R;
import com.smile.guodian.UserDao;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.me.BindPhoneActivity;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_account)
    EditText name;
    @BindView(R.id.login_password)
    EditText password;
    @BindView(R.id.login_commit)
    Button commit;
    @BindView(R.id.login_show_password)
    CheckBox hiddenPassword;
    @BindView(R.id.login_web)
    WebView webView;


    private String where = "";


    private boolean isHidden = false;

    @OnClick({R.id.login_forget, R.id.login_show_password})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.login_show_password:
                if (!hiddenPassword.isChecked()) {
                    isHidden = false;
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    isHidden = true;
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }


    @Override
    protected void init() {
        Intent intent = getIntent();
        where = intent.getStringExtra("where");
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() != 0 && !commit.isEnabled()) {
                    commit.setEnabled(true);
                } else if (s.length() == 0 && commit.isEnabled()) {
                    commit.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        initToolBar();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.login;
    }

    Intent intent = null;

    @OnClick({R.id.login_commit, R.id.login_register, R.id.login_forget, R.id.login_no_password, R.id.login_back, R.id.login_qq, R.id.login_wx, R.id.login_wb})
    public void click(View view) {

        Platform plat = null;
        switch (view.getId()) {
            case R.id.login_commit:
                if (commit.isEnabled())
                    login();   //登录
                break;
            case R.id.login_register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_forget:
                intent = new Intent(this, ForgetPassword.class);
                startActivity(intent);
                break;
            case R.id.login_no_password:
                intent = new Intent(this, FastLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.login_back:
                this.finish();
                break;
            case R.id.login_wx:
                plat = ShareSDK.getPlatform(Wechat.NAME);
                ShareSDK.setActivity(this);
                plat.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                        System.out.println(platform.getDb().exportData());
                        String result = platform.getDb().exportData();
                        System.out.println(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            String uid = jsonObject.getString("userID");
                            String icon = jsonObject.getString("icon");
                            String nickname = jsonObject.getString("nickname");
                            thirdLogin(uid, "qq", icon, nickname);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        System.out.println(platform.getDb().exportData() + "error" + throwable.getMessage());
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        System.out.println(platform.getDb().exportData() + "cancel");
                    }
                });
//                String openId = plat.getDb().getUserId();
                plat.authorize();
//                plat.showUser(openId);
                break;
            case R.id.login_wb:
//                Toast.makeText(this, "WeiBo", Toast.LENGTH_LONG).show();
                plat = ShareSDK.getPlatform(SinaWeibo.NAME);
                ShareSDK.setActivity(this);
                plat.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                        String result = platform.getDb().exportData();
                        System.out.println(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            String uid = jsonObject.getString("userID");
//                            String icon = jsonObject.getString("icon");
//                            String nickname = jsonObject.getString("nickname");
                            thirdLogin(uid, "weibo", "", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        System.out.println(platform.getDb().exportData() + "error");
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        System.out.println(platform.getDb().exportData() + "cancel");
                    }
                });
                plat.SSOSetting(false);
//                plat.authorize();
                plat.showUser(null);
                break;
            case R.id.login_qq:
                plat = ShareSDK.getPlatform(QQ.NAME);
                ShareSDK.setActivity(this);
                plat.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        String result = platform.getDb().exportData();
                        System.out.println(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            String uid = jsonObject.getString("userID");
                            String icon = jsonObject.getString("icon");
                            String nickname = jsonObject.getString("nickname");
                            thirdLogin(uid + "4", "qq", icon.replace("&", "%26"), nickname);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
//                        thirdLogin("3072C57928D4DB09937E768326D90391", "qq", "", "");
                        System.out.println(platform.getDb().exportData() + "error");
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
//                        thirdLogin("3072C57928D4DB09937E768326D90391", "qq", "", "");
                        System.out.println(platform.getDb().exportData() + "cancel");
                    }
                });
//                String openId = plat.getDb().getUserId();
                plat.authorize();
                break;
        }
    }


    private void thirdLogin(final String unionid, String type, final String icon, final String nickname) {
        Map<String, String> params = new HashMap<>();
        OkHttp.post(this, HttpContants.BASE_URL + "/Api/Auth/thirdLogin?openid=" + unionid + "&type=" + type, params, new OkCallback() {
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
                try {
                    String userid = result.getString("user_id");
                    if (userid == null || userid.equalsIgnoreCase("")) {
//                        System.out.println("----" + userid);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                                intent.putExtra("unionid", unionid);
                                intent.putExtra("icon", icon);
                                intent.putExtra("nickname", nickname);
                                startActivity(intent);
                            }
                        });
                        return;
                    }
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
                            AlertDialog.Builder b = new AlertDialog.Builder(LoginActivity.this);
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

                        //设置响应js 的Confirm()函数
                        @Override
                        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                            return true;
                        }

                        //设置响应js 的Prompt()函数
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
                            if (where.equalsIgnoreCase("found")) {
                                LoginActivity.this.finish();
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(LoginActivity.this, msg);
            }
        });


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    LoginActivity.this.finish();
                    break;
                case 1:
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };


//    kubeadm join 192.168.31.103:6443 --token ffdmip.pv8ekh02dfa1lk8q  --discovery-token-ca-cert-hash sha256:10fd14f9bb1b2682f1b07179841e7ecb168be2ee7bca5a0f99b9128af06aa00f


    private void login() {

        String phone = name.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        Map<String, String> params = new HashMap<>();

        OkHttp.post(this, HttpContants.LOGIN + "?mobile=" + phone + "&password=" + pwd, params, new OkCallback() {
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
                            AlertDialog.Builder b = new AlertDialog.Builder(LoginActivity.this);
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
                    webView.setVisibility(View.VISIBLE);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            System.out.print(url);
                            if (where != null && where.equalsIgnoreCase("found")) {
//                                LoginActivity.this.finish();
                                handler.sendEmptyMessageDelayed(0, 1000);
                            } else {
                                handler.sendEmptyMessageDelayed(1, 1000);
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                startActivity(intent);
                            }
                        }

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {

                            return super.shouldOverrideUrlLoading(view, url);
                        }

                        @Override
                        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                            return super.shouldOverrideKeyEvent(view, event);
                        }

                        @Override
                        public void onReceivedError(WebView view, int errorCode,
                                                    String description, String failingUrl) {
                            super.onReceivedError(view, errorCode, description, failingUrl);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(LoginActivity.this, msg);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (webView != null) {
//            webView.setVisibility(View.GONE);
//            webView.removeAllViews();
//            webView.destroy();
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
