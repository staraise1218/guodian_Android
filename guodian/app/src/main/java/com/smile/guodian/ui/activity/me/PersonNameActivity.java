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
import android.widget.TextView;

import com.google.gson.Gson;
import com.smile.guodian.R;
import com.smile.guodian.UserDao;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
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

public class PersonNameActivity extends BaseActivity {

    @BindView(R.id.reset_name)
    EditText resetName;
    @BindView(R.id.reset_name_save)
    Button save;
    @BindView(R.id.reset_name_title)
    TextView textView;
    @BindView(R.id.reset_name_tip)
    TextView tip;

    private int uid;
    private String field;

    @OnClick({R.id.reset_name_back, R.id.reset_name_save})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.reset_name_back:
                this.finish();
                break;
            case R.id.reset_name_save:
                changName();
                break;
        }
    }


    @Override
    protected void init() {

        Intent intent = getIntent();
        field = intent.getStringExtra("filedName");
        System.out.println(field);
        if (field.equalsIgnoreCase("username")) {
            textView.setText("修改用户名");
            tip.setText("输入用户名");
        } else if (field.equalsIgnoreCase("nickname")) {
            textView.setText("修改昵称");
            tip.setText("输入用昵称");

        } else if (field.equalsIgnoreCase("personal_statement")) {
            textView.setText("修改个人说明");
            tip.setText("个人说明");
        }

        uid = getSharedPreferences("db", MODE_PRIVATE).getInt("uid", -1);

        resetName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (start == 0) {
                    save.setEnabled(false);
                } else {
                    save.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public void changName() {
        String name = resetName.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
//        params.put("mobile", phone);
//        params.put("password", pwd);


        OkHttp.post(this, HttpContants.BASE_URL + "/Api/user/changeField?user_id=" + uid + "&field=" + field + "&fieldValue=" + name, params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                PersonNameActivity.this.finish();
            }

            @Override
            public void onFailure(String state, String msg) {
//                System.out.println(state+msg);
                ToastUtil.showShortToast(PersonNameActivity.this, msg);
            }
        });


    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_reset_name;
    }
}
