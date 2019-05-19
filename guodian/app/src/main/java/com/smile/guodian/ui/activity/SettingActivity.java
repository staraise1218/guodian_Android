package com.smile.guodian.ui.activity;

import android.app.Person;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.ui.activity.me.AcountActivity;
import com.smile.guodian.ui.activity.me.PersonActivity;
import com.smile.guodian.utils.DataCleanManager;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.chache)
    TextView chache;

    @OnClick({R.id.setting_back, R.id.setting_clear, R.id.logout, R.id.setting_acount, R.id.setting_person, R.id.setting_give})
    public void clickView(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.setting_back:
                this.finish();
                break;
            case R.id.setting_acount:
                intent = new Intent(this, AcountActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_clear:
                try {
                    System.out.println(DataCleanManager.getTotalCacheSize(getApplicationContext()) + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DataCleanManager.cleanInternalCache(getApplicationContext());
                handler.sendEmptyMessageDelayed(1, 1000);
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            chache.setText("0B");
        }
    };

    @Override
    protected void init() {
        try {
            chache.setText(DataCleanManager.getTotalCacheSize(SettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_setting;
    }
}
