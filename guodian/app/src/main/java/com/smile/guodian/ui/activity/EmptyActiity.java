package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.smile.guodian.R;

import butterknife.BindView;
import butterknife.OnClick;

public class EmptyActiity extends BaseActivity {
    @BindView(R.id.empty_title)
    TextView title;

    @OnClick(R.id.message_new_back)
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.message_new_back:
                this.finish();
                break;
        }
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        title.setText(name);

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.empty_activity;
    }
}
