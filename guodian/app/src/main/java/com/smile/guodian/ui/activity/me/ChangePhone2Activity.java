package com.smile.guodian.ui.activity.me;

import android.content.Intent;
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

public class ChangePhone2Activity extends BaseActivity {

    @BindView(R.id.change2_phone_verify)
    EditText verify;
    @BindView(R.id.change2_phone_phone)
    EditText phone;
    @BindView(R.id.change2_phone_commit)
    Button commit;

    @BindView(R.id.change2_phone_getVerification)
    TextView getVerify;

    private String code;
    private int duration = 60;      //倒计时3秒
    Timer timer = new Timer();
    private String uid;

    @OnClick({R.id.change2_phone_back, R.id.change2_phone_commit, R.id.change2_phone_getVerification})

    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.change2_phone_back:
                this.finish();
                break;
            case R.id.change2_phone_commit:
                changePhone();
                break;
            case R.id.change2_phone_getVerification:
                getVerify.setEnabled(false);
                timer.schedule(task, 1000, 1000);
                getVerif();
                break;
        }
    }

    @Override
    protected void init() {

        verify.addTextChangedListener(new TextWatcher() {
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

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_change2_phone;
    }


    public void changePhone() {
        String phon = phone.getText().toString();
        Map<String, String> params = new HashMap<>();
//        params.put("user_id", uid + "");
//        params.put("mobile", phon);
//        params.put("code", code);

        OkHttp.post(this, HttpContants.BASE_URL + "/Api/user/bindNewMobile?user_id=" + uid + "&mobile=" + phon + "&code=" + code, params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    int code = object.getInt("code");
                    System.out.println(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(ChangePhone2Activity.this, msg);
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
                    verify.setText(code);
//                    handler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(ChangePhone2Activity.this, msg);
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
                        timer.cancel();
                        getVerify.setEnabled(true);
//                        jumpActivity();
                    }
                }
            });

        }
    };
}
