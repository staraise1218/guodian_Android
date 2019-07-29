package com.smile.guodian.ui.activity.me;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePhone1Activty extends BaseActivity {

    @BindView(R.id.change_phone1_phone)
    TextView phone;
    @BindView(R.id.change_phone1_verify)
    EditText verify;
    @BindView(R.id.change_phone1_getVerify)
    TextView getVerify;

    String code;
    private int duration = 60;      //倒计时3秒
    Timer timer = new Timer();
    private String uid;
    private String phon;

    @OnClick({R.id.change_phone1_commit, R.id.change_phone1_getVerify, R.id.change_phone1_cannot})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.change_phone1_cannot:
                final View messageView = LayoutInflater.from(this).inflate(R.layout.dialog_message, null);
                TextView ok = (TextView) messageView.findViewById(R.id.dialog_ok);
                TextView cancel = (TextView) messageView.findViewById(R.id.dialog_cancel);
                TextView close = (TextView) messageView.findViewById(R.id.dialog_close);
                final PopupWindow popupWindow = new PopupWindow(messageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                popupWindow.setOutsideTouchable(true);

                View parent = LayoutInflater.from(this).inflate(R.layout.activity_register, null);
                popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
                //popupWindow在弹窗的时候背景半透明
                final WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 0.5f;
                getWindow().setAttributes(params);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        params.alpha = 1.0f;
                        getWindow().setAttributes(params);
                    }
                });


                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.change_phone1_commit:
                bindPhone();
                break;
            case R.id.change_phone1_getVerify:
                getVerify.setEnabled(false);
                timer.schedule(task, 1000, 1000);
                getVerif();
                break;
        }
    }


    public void bindPhone() {
//        String phon = phone.getText().toString();
        Map<String, String> params = new HashMap<>();
//        params.put("user_id", uid + "");
//        params.put("mobile", phon);
//        params.put("code", code);

        OkHttp.post(this, HttpContants.BASE_URL + "/Api/user/checkOldMobile?user_id=" + uid + "&mobile=" + phon + "&code=" + code, params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    int code = object.getInt("code");
//                    if (code == 200) {
                    Intent intent = new Intent(ChangePhone1Activty.this, ChangePhone2Activity.class);
                    startActivity(intent);
//                    }
                    System.out.println(response + "----");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String state, String msg) {
                Intent intent = new Intent(ChangePhone1Activty.this, ChangePhone2Activity.class);
                startActivity(intent);
                ToastUtil.showShortToast(ChangePhone1Activty.this, msg);
            }
        });
    }


    @Override
    protected void init() {
        User user = BaseApplication.getDaoSession().getUserDao().loadAll().get(0);
        if (user != null)
            uid = user.getUser_id();
        phon = user.getMobile();

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_change1_phone;
    }


    public void getVerif() {

        Map<String, String> params = new HashMap<>();
        params.put("mobile", phon);
        params.put("scene", "4");

        OkHttp.post(this, HttpContants.BASE_URL + "/api/auth/sendMobileCode", params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject data = null;
                try {
                    data = object.getJSONObject("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    code = data.getString("code");
                    verify.setText(code);
//                    handler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(ChangePhone1Activty.this, msg);
//                System.out.println(state + "-" + msg);
            }
        });
    }

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    duration--;
//                    System.out.println(duration);
                    getVerify.setText("重新获取(" + duration + ")");
                    if (duration < 2) {
                        timer.cancel();
                        getVerify.setEnabled(true);
//                        jumpActivity();
                    }
                }
            });

        }
    };

}
