package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class ResetPassword extends BaseActivity {

    @BindView(R.id.reset_password_new)
    EditText newPwd;
    @BindView(R.id.reset_password_verify)
    EditText verify;
    @BindView(R.id.reset_password)
    TextView reset;
    @BindView(R.id.reset_password_commit)
    Button commit;
    private int duration = 60;      //倒计时3秒
    Timer timer = new Timer();
    private String code;
    private String phone;


    @OnClick({R.id.reset_password_commit, R.id.reset_password_back, R.id.reset_password})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.reset_password_back:
                ResetPassword.this.finish();
                break;
            case R.id.reset_password_commit:
                resetPassword();
                break;
            case R.id.reset_password:
                timer.schedule(task, 1000, 1000);
                getVerif();
                break;
        }
    }

    public void getVerif() {

        String phon = newPwd.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("mobile", phon);
        params.put("scene", "1");

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
                    handler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(ResetPassword.this, msg);
//                System.out.println(state + "-" + msg);
            }
        });
    }

    public void resetPassword() {

        String pwd = newPwd.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("code", code);
        params.put("password", pwd);
        params.put("password_confirm", pwd);

        OkHttp.post(this, HttpContants.BASE_URL + "/Api/Auth/resetPwd", params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int state = 0;
                try {
                    state = object.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (state == 200) {
                    handler.sendEmptyMessage(4);
                }

            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(ResetPassword.this, msg);
//                System.out.println(state + "-" + msg);
            }
        });
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    verify.setText(code);
                    commit.setEnabled(true);
                    break;
                case 2:
                    commit.setEnabled(false);
                    break;
                case 4:

                    Intent intent = new Intent(ResetPassword.this, MainActivity.class);
                    startActivity(intent);
                    ResetPassword.this.finish();
                    break;
            }
        }
    };


    private TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    duration--;
                    System.out.println(duration);
                    reset.setText("重新获取（" + duration + "）");
                    if (duration < 2) {
                        timer.cancel();
//                        jumpActivity();
                    }
                }
            });

        }
    };

    @Override
    protected void init() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                System.out.println(start+"--"+s);
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
        return R.layout.activity_reset_password;
    }
}
