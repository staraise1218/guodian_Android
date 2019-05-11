package com.smile.guodian.ui.activity.me;

import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.ui.activity.RegisterActivity;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class BindPhoneActivity extends BaseActivity {

    User user;

    @BindView(R.id.bindphone_verify)
    EditText verify;
    @BindView(R.id.bindphone_tips)
    TextView tip;
    @BindView(R.id.bindphone_phone)
    EditText phone;
    @BindView(R.id.bindphone_commit)
    Button commit;

    private String code;

    @OnClick({R.id.bindphone_can_not, R.id.bindphone_back, R.id.bindphone_commit, R.id.bindphone_getVerification})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.bindphone_back:
                BindPhoneActivity.this.finish();
                break;
            case R.id.bindphone_getVerification:
                getVerif();
                break;
            case R.id.bindphone_commit:

                break;

        }
    }


    @Override
    protected void init() {

        SpannableString spannableString = new SpannableString("首次登录将自动注册，注册即表示接受《用户协议》和《隐私保护》");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#00BFFF")), 17, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#00BFFF")), 24, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tip.setText(spannableString);
        List<User> users = BaseApplication.getDaoSession().getUserDao().loadAll();
        if (users.size() > 0) {
            user = users.get(0);
        }
        verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
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
        return R.layout.activity_bindphone;
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
//                    handler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(BindPhoneActivity.this, msg);
//                System.out.println(state + "-" + msg);
            }
        });
    }
}
