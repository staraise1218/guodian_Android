package com.smile.guodian.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.smile.guodian.R;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.version)
    TextView version;

    @OnClick({R.id.setting_back, R.id.setting_clear, R.id.logout, R.id.setting_acount, R.id.setting_person, R.id.setting_give})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.setting_back:
                this.finish();
                break;
            case R.id.setting_acount:

                break;
            case R.id.setting_clear:
                break;
            case R.id.setting_person:
                break;
            case R.id.setting_give:
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
