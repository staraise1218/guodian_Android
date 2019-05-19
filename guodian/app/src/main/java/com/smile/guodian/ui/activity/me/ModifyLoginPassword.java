package com.smile.guodian.ui.activity.me;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyLoginPassword extends BaseActivity {
    @BindView(R.id.modify_password_new)
    EditText newPwd;
    @BindView(R.id.modify_password_verify)
    EditText verify;
    @BindView(R.id.modify_password_phone)
    TextView phone;
    @BindView(R.id.modify_password_get)
    TextView modify;
    @BindView(R.id.modify_password_commit)
    TextView commit;

    private String code;
    private User user;
    private int duration = 60;      //倒计时3秒
    Timer timer = new Timer();

    @OnClick({R.id.modify_password_back, R.id.modify_password_commit, R.id.modify_password_get})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.modify_password_back:
                this.finish();
                break;
            case R.id.modify_password_commit:
                resetPassword();
                break;
            case R.id.modify_password_get:
                modify.setEnabled(false);
                task = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {      // UI thread
                            @Override
                            public void run() {
                                duration--;
                                modify.setText("重新获取(" + duration + ")");
                                if (duration < 2) {
                                    timer.cancel();
                                    modify.setEnabled(true);

                                }
                            }
                        });

                    }
                };
                timer.schedule(task, 1000);
                getVerif();
                break;
        }
    }

    private TimerTask task;

    @Override
    protected void init() {

        user = BaseApplication.getDaoSession().getUserDao().loadAll().get(0);
        if (user != null) {
            phone.setText("已绑定手机号" + user.getMobile());
        }

        newPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
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


    public void resetPassword() {

        String pwd = newPwd.getText().toString();
        code = verify.getText().toString();
        pwd = newPwd.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("mobile", user.getMobile());
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
                    ModifyLoginPassword.this.finish();
                }

            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(ModifyLoginPassword.this, msg);
//                System.out.println(state + "-" + msg);
            }
        });
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_modify_password;
    }

    public void getVerif() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", user.getMobile());
        params.put("scene", "2");
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
                    verify.setText(code);
//                    handler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(ModifyLoginPassword.this, msg);
//                System.out.println(state + "-" + msg);
            }
        });
    }
}
