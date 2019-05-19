package com.smile.guodian.ui.activity.message;

import android.content.Intent;
import android.view.View;

import com.smile.guodian.R;
import com.smile.guodian.ui.activity.BaseActivity;

import butterknife.OnClick;

public class MessageCenterActivity extends BaseActivity {
    @OnClick({R.id.message_center_message})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.message_center_message:
                Intent intent = new Intent(MessageCenterActivity.this, NewMessageActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void init() {

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_message_center;
    }
}
