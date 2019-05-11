package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.register_phone)
    EditText phone;
    @BindView(R.id.register_verify)
    EditText verify;
    @BindView(R.id.register_getVerification)
    TextView getVerification;
    @BindView(R.id.register_password)
    EditText password;
    @BindView(R.id.register_commit)
    Button commit;
    @BindView(R.id.register_show_password)
    CheckBox showPassword;
    @BindView(R.id.register_tips)
    TextView tip;

    private boolean passworldHiden = false;


    private int duration = 60;      //倒计时3秒
    Timer timer = new Timer();
    private String code;


    @OnClick({R.id.register_getVerification, R.id.register_commit, R.id.register_back, R.id.register_can_not, R.id.register_show_password})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.register_getVerification:
                getVerif();
                view.setEnabled(false);
                timer.schedule(task, 1000, 1000);
                break;
            case R.id.register_commit:
                timer.cancel();
                register();
                break;
            case R.id.register_back:
                timer.cancel();
                this.finish();
                break;
            case R.id.register_can_not:
                break;
            case R.id.register_show_password:

                if (showPassword.isChecked()) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                break;
        }
    }


    public void register() {
        String phon = phone.getText().toString();
        String pwd = password.getText().toString();
        code = verify.getText().toString();
        Map<String, String> params = new HashMap<>();
        OkHttp.post(this, HttpContants.BASE_URL + "/api/auth/register?mobile=" + phon + "&code=" + code + "&password=" + pwd + "&password_confirm=" + pwd, params, new OkCallback() {
            @Override
            public void onResponse(String response) {
//                System.out.println(response);
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
                    int uid = data.getInt("user_id");
                    SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("uid", uid);
                    editor.commit();
                    handler.sendEmptyMessage(4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(RegisterActivity.this, msg);
//                System.out.println(state + "-" + msg);
            }
        });
    }


    public void getVerif() {

        String phon = phone.getText().toString();

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
                ToastUtil.showShortToast(RegisterActivity.this, msg);
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
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void init() {
        SpannableString spannableString = new SpannableString("点击注册意味着您接受《用户协议》和《隐私保护》");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#00BFFF")), 10, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#00BFFF")), 17, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tip.setText(spannableString);


        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && commit.isEnabled()) {
                    commit.setEnabled(false);
                } else if (!commit.isEnabled() && verify.getText().toString().length() != 0 && password.getText().toString().length() != 0) {
                    commit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && commit.isEnabled()) {
                    commit.setEnabled(false);
                } else if (!commit.isEnabled() && phone.getText().toString().length() != 0 && verify.getText().toString().length() != 0) {
                    commit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && commit.isEnabled()) {
                    commit.setEnabled(false);
                } else if (!commit.isEnabled() && phone.getText().toString().length() != 0 && password.getText().toString().length() != 0) {
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
        return R.layout.activity_register;
    }

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    duration--;
                    System.out.println(duration);
                    getVerification.setText("重新获取（" + duration + "）");
                    if (duration < 2) {
                        timer.cancel();
                    }
                }
            });

        }
    };

}
