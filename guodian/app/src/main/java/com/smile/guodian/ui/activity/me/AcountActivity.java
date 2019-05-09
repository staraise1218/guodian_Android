package com.smile.guodian.ui.activity.me;

import android.view.View;

import com.smile.guodian.R;
import com.smile.guodian.ui.activity.BaseActivity;

import butterknife.OnClick;

public class AcountActivity extends BaseActivity {

    @OnClick({R.id.account_back, R.id.account_password, R.id.account_out, R.id.pay_password})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.account_back:
                this.finish();
                break;
            case R.id.account_password:
                break;
            case R.id.pay_password:
                break;
            case R.id.account_out:
                break;
        }
    }

    @Override
    protected void init() {

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_acount;
    }
}
