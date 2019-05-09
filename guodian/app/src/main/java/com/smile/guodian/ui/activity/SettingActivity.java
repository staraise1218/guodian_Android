package com.smile.guodian.ui.activity;

import android.app.Person;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.ui.activity.me.PersonActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.version)
    TextView version;

    @OnClick({R.id.setting_back, R.id.setting_clear, R.id.logout, R.id.setting_acount, R.id.setting_person, R.id.setting_give})
    public void clickView(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.setting_back:
                this.finish();
                break;
            case R.id.setting_acount:
                break;
            case R.id.setting_clear:
                break;
            case R.id.setting_person:
                intent = new Intent(SettingActivity.this, PersonActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_give:
                break;
            case R.id.logout:
                SharedPreferences.Editor editor = getSharedPreferences("db", MODE_PRIVATE).edit();
                editor.putInt("uid", -1);
                editor.commit();
                intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
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
