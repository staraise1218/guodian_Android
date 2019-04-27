package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_account)
    EditText name;
    @BindView(R.id.login_password)
    EditText password;
    @BindView(R.id.login_commit)
    Button commit;
    @BindView(R.id.login_show_password)
    CheckBox hiddenPassword;


    private boolean isHidden = false;

    @OnClick({R.id.login_forget, R.id.login_show_password})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.login_forget:
                Intent intent = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(intent);
                break;
            case R.id.login_show_password:
                if (hiddenPassword.isChecked()) {
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
//        initToolBar();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.login;
    }


    @OnClick({R.id.login_commit, R.id.login_register, R.id.login_forget, R.id.login_no_password, R.id.login_back})
    public void click(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.login_commit:
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
        }
    }

    private void login() {

        String phone = name.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("password", pwd);


        OkHttp.post(this, HttpContants.LOGIN, params, new OkCallback() {
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
                    int uid = result.getInt("user_id");
                    SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("uid", uid);
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String state, String msg) {
//                System.out.println(state+msg);
                ToastUtil.showShortToast(LoginActivity.this, msg);
            }
        });


    }

    private void loginlogic(String phone, String pwd) {

//        List<User> mUserDataList = DataManager.queryUser(phone);
//        if (mUserDataList != null && mUserDataList.size() > 0) {
//
//            String netPwd = mUserDataList.get(0).getPwd();
//            Long netUserId = mUserDataList.get(0).getUserId();
//
//            if (pwd.equals(netPwd)) {
//                ToastUtils.showSafeToast(LoginActivity.this, "登录成功");
//                EnjoyshopApplication application = EnjoyshopApplication.getInstance();
//
//                com.enjoyshop.bean.User user = new com.enjoyshop.bean.User();
//                user.setMobi(phone);
//                user.setUsername("非洲小白脸");
//                user.setId(netUserId);
//                user.setLogo_url(imageUrl);
//
//                application.putUser(user, "12345678asfghdssa");
        finish();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
