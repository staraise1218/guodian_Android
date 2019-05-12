package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.smile.guodian.R;
import com.smile.guodian.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPassword extends BaseActivity {

    @BindView(R.id.findpassword_phone)
    EditText editText;
    @BindView(R.id.findpassword_commit)
    Button commit;

    @OnClick({R.id.findpassword_back, R.id.findpassword_commit})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.findpassword_back:
                this.finish();
                break;
            case R.id.findpassword_commit:
                String phone = editText.getText().toString();
                if (phone.length() != 11) {
                    ToastUtil.showShortToast(this, "输入正确的手机号");
                } else {
                    Intent intent = new Intent(ForgetPassword.this, ResetPassword.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                    this.finish();
                }
                break;
        }

    }


    @Override
    protected void init() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
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

        return R.layout.activity_findpassword;
    }
}
