package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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

import butterknife.BindView;
import butterknife.OnClick;

public class SetPasswordActivity extends BaseActivity {

    @BindView(R.id.set_password_new)
    EditText password;
    @BindView(R.id.set_password_verify)
    EditText confirm;

    private String code;
    private String phone;

    @OnClick({R.id.set_password_back, R.id.set_password_commit})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.set_password_back:
                this.finish();
                break;
            case R.id.set_password_commit:
                register();
                break;
        }
    }
    public void register() {
//        String phon = phone.getText().toString();
        String pwd = password.getText().toString();
        String rePwd = confirm.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("code", code);
        params.put("password", pwd);
        params.put("password_confirm", rePwd);


        OkHttp.post(this, HttpContants.BASE_URL + "/api/auth/register", params, new OkCallback() {
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
                ToastUtil.showShortToast(SetPasswordActivity.this, msg);
//                System.out.println(state + "-" + msg);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 4:
                    Intent intent = new Intent(SetPasswordActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };



    @Override
    protected void init() {
        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        phone = intent.getStringExtra("phone");
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_setpassword;
    }
}
