package com.smile.guodian.ui.activity.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smile.guodian.R;
import com.smile.guodian.UserDao;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Address;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.BaseActivity;
import com.smile.guodian.ui.activity.LoginActivity;
import com.smile.guodian.ui.activity.MainActivity;
import com.smile.guodian.ui.adapter.AddressAdapter;
import com.smile.guodian.ui.adapter.ProductAdapter;
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

public class AddressActivity extends BaseActivity {

    @BindView(R.id.address_view)
    GridViewWithHeaderAndFooter content;
    @BindView(R.id.address_frame)
    PtrClassicFrameLayout frameLayout;
    @BindView(R.id.address_empty)
    RelativeLayout empty;
    @BindView(R.id.address_add_content)
    RelativeLayout addContent;

    @OnClick({R.id.address_back, R.id.address_add})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.address_back:
                this.finish();
                break;
            case R.id.address_add:
                Intent intent = new Intent(AddressActivity.this, EditAddressActivity.class);
                startActivity(intent);
                break;
        }
    }


    Handler handler = new Handler();

    private List<Address> addressList = new ArrayList<>();
    private int page = 1;
    private int uid;

    private AddressAdapter adapter;

    @Override
    protected void init() {
        empty.setVisibility(View.GONE);
        addContent.setVisibility(View.VISIBLE);

        adapter = new AddressAdapter(this, addressList);
        content.setAdapter(adapter);


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
                frameLayout.autoRefresh(true);
            }
        }, 150);

        frameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        initData();
                    }
                }, 1000);
            }
        });

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_adress;
    }


    public void initData() {

        Map<String, String> params = new HashMap<>();

        OkHttp.post(this, HttpContants.BASE_URL + "/Api/Address/address_list?user_id=" + uid + "&page=" + page, params, new OkCallback() {
            @Override
            public void onResponse(String response) {

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

                Gson gson = new Gson();
                TypeToken<List<Address>> address = new TypeToken<List<Address>>() {
                };
                addressList = gson.fromJson(result.toString(), address.getType());

                if (addressList.size() > 0) {
                    addContent.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                    show();
                }
                if (addressList.size() == 0) {
                    addContent.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(AddressActivity.this, msg);
            }
        });


    }

    public void show() {

        adapter.notifyDataSetChanged();
        frameLayout.refreshComplete();
        frameLayout.setLoadMoreEnable(true);

    }

}
