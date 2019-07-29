package com.smile.guodian.ui.activity.message;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smile.guodian.R;
import com.smile.guodian.UserDao;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.NewMessage;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.ui.activity.LoginActivity;
import com.smile.guodian.ui.activity.MainActivity;
import com.smile.guodian.ui.adapter.NewMessageAdapter;
import com.smile.guodian.utils.ToastUtil;
import com.syz.commonpulltorefresh.lib.PtrClassicFrameLayout;
import com.syz.commonpulltorefresh.lib.PtrDefaultHandler;
import com.syz.commonpulltorefresh.lib.PtrFrameLayout;
import com.syz.commonpulltorefresh.lib.loadmore.GridViewWithHeaderAndFooter;
import com.syz.commonpulltorefresh.lib.loadmore.OnLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class NewMessageActivity extends BaseActivity {

    @BindView(R.id.message_new_none)
    RelativeLayout none;
    @BindView(R.id.message_new_content)
    RelativeLayout content;
    @BindView(R.id.message_new)
    GridViewWithHeaderAndFooter listView;
    @BindView(R.id.message_new_frame)
    PtrClassicFrameLayout frameLayout;

    @OnClick(R.id.message_new_back)
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.message_new_back:
                this.finish();
                break;
        }
    }

    private int page = 1;
    private String uid;
    private List<NewMessage> messages = new ArrayList<>();
    private NewMessageAdapter adapter;
    Handler handler = new Handler();

    @Override
    protected void init() {
        List<User> users = BaseApplication.getDaoSession().getUserDao().loadAll();
        User user = null;
        if (users.size() > 0)
            user = users.get(0);
        if (user != null) {
            uid = user.getUser_id();
        } else {
            uid = "-1";
        }
        if (uid.equalsIgnoreCase("-1")) {
            none.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);

        } else {
            none.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        }

//        initData();

        adapter = new NewMessageAdapter(this, messages);
        listView.setAdapter(adapter);
        frameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                initData();
            }
        });

        frameLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
//                initData("1");
                frameLayout.autoRefresh(true);
            }
        }, 150);

        frameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messages.clear();
                        page = 1;
                        initData();
                    }
                }, 1000);
            }
        });

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_message_new;
    }


    public void initData() {
        Map<String, String> params = new HashMap<>();

        OkHttp.post(this, HttpContants.BASE_URL + "/Api/user/message?user_id=" + uid + "&page=" + page, params, new OkCallback() {
            @Override
            public void onResponse(String response) {

//                System.out.println(response);

                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray result = null;
                try {
                    result = object.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (result.length() == 0) {
                    none.setVisibility(View.VISIBLE);
                    content.setVisibility(View.GONE);
                } else {
                    content.setVisibility(View.VISIBLE);
                    none.setVisibility(View.GONE);
                }

                Gson gson = new Gson();
                TypeToken<List<NewMessage>> typeToken = new TypeToken<List<NewMessage>>() {
                };
                messages = gson.fromJson(result.toString(), typeToken.getType());

//                if (messages.size() == 0) {
//
//                } else {
//                    none.setVisibility(View.GONE);
//                }

                adapter = new NewMessageAdapter(getBaseContext(), messages);
                listView.setAdapter(adapter);
                show();
            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(NewMessageActivity.this, msg);
            }
        });


    }

    public void show() {

        adapter.notifyDataSetChanged();
        frameLayout.refreshComplete();
        frameLayout.setLoadMoreEnable(true);

    }

}
