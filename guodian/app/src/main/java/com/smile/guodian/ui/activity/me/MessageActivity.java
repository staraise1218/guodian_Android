package com.smile.guodian.ui.activity.me;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.utils.ToastUtil;

import java.net.UnknownServiceException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
    @BindView(R.id.message_sex)
    RelativeLayout mSex;
    @BindView(R.id.message_phone)
    RelativeLayout mPhone;
    @BindView(R.id.message_name)
    RelativeLayout mName;
    @BindView(R.id.message_birthday)
    RelativeLayout mBirthday;

    Dialog dialog;
    private int uid;
    Window window;
    PopupWindow popupWindow;
    View parent;
    WindowManager.LayoutParams params;
    TimePickerView pvCustomLunar;
    private User user;

    @OnClick({R.id.message_sex, R.id.message_back, R.id.message_phone, R.id.message_name, R.id.message_birthday})
    public void clickView(View view) {

        switch (view.getId()) {
            case R.id.message_back:
                this.finish();
                break;
            case R.id.message_sex:
//                if (user.getSex() != null) {
//                    break;
//                }
                View sexView = LayoutInflater.from(this).inflate(R.layout.dialog_sex, null);
                TextView btnCarema = (TextView) sexView.findViewById(R.id.btn_camera);
                TextView btnPhoto = (TextView) sexView.findViewById(R.id.btn_photo);
                TextView btnCancel = (TextView) sexView.findViewById(R.id.btn_cancel);
                popupWindow = new PopupWindow(sexView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                popupWindow.setOutsideTouchable(true);
                parent = LayoutInflater.from(this).inflate(R.layout.activity_person, null);
                popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                //popupWindow在弹窗的时候背景半透明
                params = getWindow().getAttributes();
                params.alpha = 0.5f;
                getWindow().setAttributes(params);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        params.alpha = 1.0f;
                        getWindow().setAttributes(params);
                    }
                });


                TextView man = sexView.findViewById(R.id.man);
                TextView woman = sexView.findViewById(R.id.woman);
                TextView cancel = sexView.findViewById(R.id.cancel);

                man.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change("sex", "1");
                    }
                });
                woman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change("sex", "2");
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                break;
            case R.id.message_name:
//                if (user.getRealname() != null) {
//                    break;
//                }
                View nameView = LayoutInflater.from(this).inflate(R.layout.dialog_name, null);
                popupWindow = new PopupWindow(nameView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                parent = LayoutInflater.from(this).inflate(R.layout.activity_person, null);
                popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                //popupWindow在弹窗的时候背景半透明
                params = getWindow().getAttributes();
                params.alpha = 0.5f;
                getWindow().setAttributes(params);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        params.alpha = 1.0f;
                        getWindow().setAttributes(params);
                    }
                });

                final Button commit = nameView.findViewById(R.id.commit);
                final EditText name = nameView.findViewById(R.id.name);
                cancel = nameView.findViewById(R.id.name_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
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
//                if (user.getBirthday() != null) {
//                    break;
//                }
                pvCustomLunar.show();
                break;
            case R.id.message_phone:
//                if (user.getMobile() != null) {
//                    break;
//                }
                Intent intent = new Intent(MessageActivity.this, ChangePhoneActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void init() {
        uid = getSharedPreferences("db", MODE_PRIVATE).getInt("uid", -1);
        user = BaseApplication.getDaoSession().getUserDao().loadAll().get(0);
        initLunarPicker();
        birthday.setText(user.getBirthday());
        if (user.getSex().equalsIgnoreCase("1")) {
            sex.setText("男");
        } else {
            sex.setText("女");
        }
        name.setText(user.getRealname());
        phone.setText(user.getMobile());

//        if (user.getSex() != null) {
//            mSex.setEnabled(false);
//        }
//        if (user.getBirthday() != null) {
//            mBirthday.setEnabled(false);
//        }
//        if (user.getMobile() != null) {
//            mPhone.setEnabled(false);
//        }
//        if (user.getRealname() != null) {
//            mName.setEnabled(false);
//        }

    }


    public void change(final String field, final String fieldvalue) {
        Map<String, String> params = new HashMap<>();
        System.out.println(field + "---" + fieldvalue);
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"user_id\"\r\n\r\n" + uid + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"field\"\r\n\r\n" + field + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"fieldValue\"\r\n\r\n" + fieldvalue + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");

        OkHttp.post(this, HttpContants.BASE_URL + "/Api/user/changeField", body, this, new OkCallback() {
            @Override
            public void onResponse(String response) {
                if (popupWindow != null)
                    popupWindow.dismiss();


                if (field.equalsIgnoreCase("sex")) {
                    if (fieldvalue.equalsIgnoreCase("1")) {
                        sex.setText("男");
                    }
                    if (fieldvalue.equalsIgnoreCase("2")) {
                        sex.setText("女");
                    }
                }
                if (field.equalsIgnoreCase("username")) {
                    name.setText(fieldvalue);
                }

                if (field.equalsIgnoreCase("birthday")) {
                    birthday.setText(fieldvalue);
                }
                if (field.equalsIgnoreCase("phone")) {
                    phone.setText(fieldvalue);
                }
            }

            @Override
            public void onFailure(String state, String msg) {
//                System.out.println(state+msg);
                ToastUtil.showShortToast(MessageActivity.this, msg);
            }
        });


    }

    /**
     * 农历时间已扩展至 ： 1900 - 2100年
     */
    private void initLunarPicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2069, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomLunar = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String string = dateFormat.format(date);
                change("birthday", string);
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_lunar, new CustomListener() {

                    @Override
                    public void customLayout(final View v) {
                        TextView tvSubmit = v.findViewById(R.id.tv_confirm);
                        TextView ivCancel = v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomLunar.returnData();
                                pvCustomLunar.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomLunar.dismiss();
                            }
                        });
                        //公农历切换


                    }

                    private void setTimePickerChildWeight(View v, float yearWeight, float weight) {
                        ViewGroup timePicker = (ViewGroup) v.findViewById(R.id.timepicker);
                        View year = timePicker.getChildAt(0);
                        LinearLayout.LayoutParams lp = ((LinearLayout.LayoutParams) year.getLayoutParams());
                        lp.weight = yearWeight;
                        year.setLayoutParams(lp);
                        for (int i = 1; i < timePicker.getChildCount(); i++) {
                            View childAt = timePicker.getChildAt(i);
                            LinearLayout.LayoutParams childLp = ((LinearLayout.LayoutParams) childAt.getLayoutParams());
                            childLp.weight = weight;
                            childAt.setLayoutParams(childLp);
                        }
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.GRAY)
                .build();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_message;
    }
}
