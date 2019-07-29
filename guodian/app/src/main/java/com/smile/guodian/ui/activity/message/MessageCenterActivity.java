package com.smile.guodian.ui.activity.message;

import android.content.Intent;
import android.view.View;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.ui.activity.EmptyActiity;
import com.smile.guodian.ui.activity.WebActivity;

import butterknife.OnClick;

public class MessageCenterActivity extends BaseActivity {
    @OnClick({R.id.message_center_message, R.id.message_center_back, R.id.message_center_kefu, R.id.message_center_wuliu})
    public void clickView(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.message_center_message:
                intent = new Intent(MessageCenterActivity.this, NewMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.message_center_back:
                this.finish();
                break;
            case R.id.message_center_kefu:
                intent = new Intent(MessageCenterActivity.this, EmptyActiity.class);
                intent.putExtra("name", "在线客服");
//                intent.putExtra("type", 20);
//                intent.putExtra("url", HttpContants.BASE_URL + "/page/kfAndHelp.html");
                startActivity(intent);
                break;
            case R.id.message_center_wuliu:
                intent = new Intent(MessageCenterActivity.this, EmptyActiity.class);
                intent.putExtra("name", "物流助手");
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
