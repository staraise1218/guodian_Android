package com.smile.guodian.ui.activity.me;

import android.view.View;

import com.smile.guodian.R;
import com.smile.guodian.ui.activity.BaseActivity;

import butterknife.OnClick;

public class EditAddressActivity extends BaseActivity {

    @OnClick(R.id.address_back)
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.address_back:
                this.finish();
                break;
        }
    }

    @Override
    protected void init() {

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_edit_address;
    }
}
