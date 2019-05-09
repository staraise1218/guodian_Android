package com.smile.guodian.ui.activity.me;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.utils.ToastUtil;
import com.smile.guodian.widget.datepicker.CustomDatePicker;
import com.smile.guodian.widget.datepicker.DateFormatUtils;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageActivity extends BaseActivity {

    @BindView(R.id.message_birthday_text)
    TextView birthday;
    @BindView(R.id.message_name_text)
    TextView name;
    @BindView(R.id.message_phone_text)
    TextView phone;
    @BindView(R.id.message_sex_text)
    TextView sex;
    private TextView mTvSelectedDate;

    Dialog dialog;
    private int uid;

    @OnClick({R.id.message_sex, R.id.message_back, R.id.message_phone, R.id.message_name, R.id.message_birthday})
    public void clickView(View view) {
        Window window;
        switch (view.getId()) {
            case R.id.message_back:
                this.finish();
                break;
            case R.id.message_sex:
                dialog = new Dialog(MessageActivity.this);
                View sexView = View.inflate(MessageActivity.this, R.layout.dialog_sex, null);
//                dialog.setContentView(sexView);
                dialog.setCanceledOnTouchOutside(false); //手指触碰到外界取消
                dialog.setCancelable(true);             //可取消 为true
                window = dialog.getWindow();      // 得到dialog的窗体
                window.setGravity(Gravity.BOTTOM);
                window.setContentView(sexView);
//                window.getDecorView().setPadding(0, 0, 0, 0);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
                dialog.show();

                TextView man = sexView.findViewById(R.id.man);
                TextView woman = sexView.findViewById(R.id.woman);
                TextView cancel = sexView.findViewById(R.id.cancel);

                man.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change("sex", "男");
                    }
                });
                woman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change("sex", "女");
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                break;
            case R.id.message_name:
                dialog = new Dialog(MessageActivity.this);
                View nameView = View.inflate(MessageActivity.this, R.layout.dialog_name, null);
//                dialog.setContentView(sexView);
                dialog.setCanceledOnTouchOutside(false); //手指触碰到外界取消
                dialog.setCancelable(true);             //可取消 为true
                window = dialog.getWindow();      // 得到dialog的窗体
                window.setGravity(Gravity.BOTTOM);
                window.setContentView(nameView);
//                window.getDecorView().setPadding(0, 0, 0, 0);
                window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
                dialog.show();

                final Button commit = nameView.findViewById(R.id.commit);
                final EditText name = nameView.findViewById(R.id.name);
                commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change("username", name.getText().toString());
                    }
                });
                name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (start == 0) {
                            commit.setEnabled(false);
                        } else {
                            commit.setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                break;
            case R.id.message_birthday:
                initDatePicker();
                break;
        }
    }

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

//        mTvSelectedDate.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        CustomDatePicker mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
//                mTvSelectedDate.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);

    }

    @Override
    protected void init() {
        uid = getSharedPreferences("db", MODE_PRIVATE).getInt("uid", -1);
    }


    public void change(String field, String name) {

        Map<String, String> params = new HashMap<>();

        OkHttp.post(this, HttpContants.BASE_URL + "/Api/user/changeField?user_id=" + uid + "&field=" + field + "&fieldValue=" + name, params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                MessageActivity.this.finish();
            }

            @Override
            public void onFailure(String state, String msg) {
//                System.out.println(state+msg);
                ToastUtil.showShortToast(MessageActivity.this, msg);
            }
        });


    }


    @Override
    protected int getContentResourseId() {
        return R.layout.activity_message;
    }
}
