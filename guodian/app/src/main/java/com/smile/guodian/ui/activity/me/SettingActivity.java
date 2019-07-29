package com.smile.guodian.ui.activity.me;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.ui.activity.LoginActivity;
import com.smile.guodian.ui.activity.MainActivity;
import com.smile.guodian.utils.DataCleanManager;
import com.smile.guodian.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.chache)
    TextView chache;
    @BindView(R.id.setting_webview)
    WebView webView;

    @OnClick({R.id.setting_back, R.id.setting_clear, R.id.logout, R.id.setting_acount, R.id.setting_person, R.id.setting_give})
    public void clickView(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.setting_back:
                this.finish();
                break;
            case R.id.setting_acount:
                intent = new Intent(this, ModifyLoginPassword.class);
                startActivity(intent);
                break;
            case R.id.setting_clear:
                List<String> mPermissionList = new ArrayList<>();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }

                if (mPermissionList.size() == 0) {
                    DataCleanManager.cleanExternalCache(this);
                    DataCleanManager.cleanInternalCache(this);
                    handler.sendEmptyMessageDelayed(1, 1000);
                } else {
                    String[] permission = mPermissionList.toArray(new String[mPermissionList.size()]);
                    ActivityCompat.requestPermissions(SettingActivity.this, permission,
                            123);
                }
                break;
            case R.id.setting_person:
                intent = new Intent(SettingActivity.this, PersonActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_give:
                break;

            case R.id.logout:
                SharedPreferences.Editor editor = getSharedPreferences("db", MODE_PRIVATE).edit();
                editor.putInt("uid", -1);
                editor.commit();

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                webSettings.setDomStorageEnabled(true);
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                        AlertDialog.Builder b = new AlertDialog.Builder(SettingActivity.this);
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
                webView.loadUrl(HttpContants.BASE_URL + "/page/out.html?");
                webView.setVisibility(View.VISIBLE);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
//                        System.out.print(url);
//                        if (where != null && where.equalsIgnoreCase("found")) {
//                                LoginActivity.this.finish();
//                            handler.sendEmptyMessageDelayed(10, 1000);
//                        } else {
//                            handler.sendEmptyMessageDelayed(1, 1000);
////                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
////                                startActivity(intent);
//                        }
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
                intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                chache.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()) + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void init() {
        try {
            chache.setText(DataCleanManager.getTotalCacheSize(SettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 外部存储权限申请返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                DataCleanManager.cleanExternalCache(this);
                DataCleanManager.cleanInternalCache(this);
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_setting;
    }
}
