package com.smile.guodian.ui.activity.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.smile.guodian.R;
import com.smile.guodian.ui.activity.BaseActivity;

import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @OnClick({R.id.setting_back, R.id.logout, R.id.setting_person, R.id.setting_acount, R.id.setting_give, R.id.setting_clear})
    public void clickView(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.set_password_back:
                this.finish();
                break;
            case R.id.setting_person:
                intent = new Intent(this, PersonActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_acount:
                intent = new Intent(this, AcountActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_give:
                break;
            case R.id.setting_clear:
                break;
            case R.id.logout:
                SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("uid", -1);
                editor.commit();
                break;
        }
    }

    @Override
    protected void init() {

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_setting;
    }
}
