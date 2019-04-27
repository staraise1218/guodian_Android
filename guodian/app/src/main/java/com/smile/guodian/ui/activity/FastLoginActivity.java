package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
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
    Button getVerify;
    @BindView(R.id.fast_login_commit)
    Button commit;
    private String code;
    private String phone;
    Timer timer = new Timer();
    private int duration = 60;

    @OnClick({R.id.fast_login_back, R.id.fast_login_commit, R.id.reset_password})
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
                try {
                    code = data.getString("code");
                    Intent intent = new Intent(FastLoginActivity.this, MainActivity.class);
                    startActivity(intent);
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

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    duration--;
//                    System.out.println(duration);
                    getVerify.setText("重新获取（" + duration + "）");
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
                    handler.sendEmptyMessage(1);/**/
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
