package com.smile.guodian.ui.activity.found;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.smile.guodian.R;
import com.smile.guodian.UserDao;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.ArticleDetail;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.ui.activity.LoginActivity;
import com.smile.guodian.ui.activity.MainActivity;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class FoundDetailActivity extends BaseActivity {
    @BindView(R.id.found_detail_title)
    TextView title;
    @BindView(R.id.found_detail_img)
    ImageView imageView;
    @BindView(R.id.found_detail_content)
    TextView content;
    @BindView(R.id.found_detail_give)
    CheckBox checkBox;

    @OnClick(R.id.found_detail_back)
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.found_detail_back:
                this.finish();
                break;
        }
    }

    private int uid;
    private int article_id;

    @Override
    protected void init() {
        SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
        uid = sharedPreferences.getInt("uid", -1);
        final Intent intent = getIntent();
        article_id = intent.getIntExtra("art_id", -1);
        initData();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (uid == -1) {
                        checkBox.setChecked(false);
                        Intent intent1 = new Intent(FoundDetailActivity.this, LoginActivity.class);
                        intent.putExtra("where", "found");
                        startActivity(intent1);
                    } else {
                        checked();
                        checkBox.setEnabled(false);
                    }
                }


            }
        });

    }


    public void checked() {

        Map<String, String> params = new HashMap<>();

        List<User> userList = BaseApplication.getDaoSession().getUserDao().loadAll();
        User user = new User();
        if (userList.size() > 0) {
            user = userList.get(0);
        }

        params.put("user_id", user.getUser_id() + "");
        params.put("article_id", article_id + "");

        OkHttp.post(this, HttpContants.BASE_URL + "/Api/find/clickLike", params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(String state, String msg) {
                Toast.makeText(FoundDetailActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_found_detail;
    }

    public void initData() {

        System.out.print(uid + "--" + article_id);

        Map<String, String> params = new HashMap<>();

        OkHttp.post(this, HttpContants.BASE_URL + "/Api/find/detail?user_id=" + uid + "&article_id=" + article_id, params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                System.out.print(response);
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject result = null;
                try {
                    result = object.getJSONObject("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (result == null) {
                    return;
                } else {

                    Gson gson = new Gson();
                    ArticleDetail articleDetail = gson.fromJson(result.toString(), ArticleDetail.class);
                    title.setText(articleDetail.getTitle());
                    Glide.with(FoundDetailActivity.this).load(HttpContants.BASE_URL + articleDetail.getThumb()).into(imageView);
                    content.setText(articleDetail.getContent());
                    content.setMovementMethod(ScrollingMovementMethod.getInstance());
                    checkBox.setChecked(articleDetail.getIsliked() == 1);
//                    if (checkBox.isChecked()) {
//                        checkBox.setEnabled(false);
//                    }
                }

            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(FoundDetailActivity.this, msg);
            }
        });


    }

}
