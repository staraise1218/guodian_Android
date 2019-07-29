package com.smile.guodian.ui.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePhoneActivity extends BaseActivity {
    @BindView(R.id.change_phone_number)
    TextView phone;

    @OnClick(R.id.change_phone_commit)
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.change_phone_commit:
                Intent intent = new Intent(ChangePhoneActivity.this, ChangePhone1Activty.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void init() {
        User user = BaseApplication.getDaoSession().getUserDao().loadAll().get(0);
        String phoneNumber = user.getMobile();
        phone.setText(phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11));
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_change_phone;
    }
}
