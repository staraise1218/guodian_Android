package com.smile.guodian.ui.activity.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.ui.activity.LoginActivity;
import com.smile.guodian.ui.activity.MainActivity;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyPayPassword extends BaseActivity {

    @OnClick({R.id.reset_pay_back, R.id.reset_pay_commit})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.reset_pay_back:
                break;
            case R.id.reset_pay_commit:
                break;
        }
    }

    @BindView(R.id.reset_pay_new)
    EditText newPwd;
    @BindView(R.id.reset_pay_origin)
    EditText origin;
    @BindView(R.id.reset_pay_commit)
    Button commit;

    @Override
    protected void init() {
        origin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start != 0 && !commit.isEnabled()) {
                    commit.setEnabled(true);
                } else if (start == 0 && commit.isEnabled()) {
                    commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void changePassword() {
        String nPwd = newPwd.getText().toString().trim();
        String oPwd = origin.getText().toString().trim();

        Map<String, String> params = new HashMap<>();
//        params.put("mobile", phone);
//        params.put("password", pwd);


        OkHttp.post(this, HttpContants.BASE_URL + "?mobile=" + nPwd + "&password=" + oPwd, params, new OkCallback() {
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

                    String uid = result.getString("user_id");
                    SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("uid", Integer.parseInt(uid));
                    System.out.print(uid);
                    editor.commit();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(ModifyPayPassword.this, msg);
            }
        });
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_reset_pay;
    }
}
