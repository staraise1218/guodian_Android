package com.smile.guodian.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.ProductBean;
import com.smile.guodian.model.entity.ProductCategory;
import com.smile.guodian.model.entity.ProductGood;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.adapter.ProductAdapter;
import com.smile.guodian.widget.ProductGenerator;
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

public class CategoryProductActivity extends BaseActivity {

    private String branch_id;
    private String name;
    //    @BindView(R.id.product_catogory_content)
//    GridView content;
    @BindView(R.id.product_catogory_title)
    TextView title;
    @BindView(R.id.product_catogory_back)
    ImageView back;
    @BindView(R.id.web)
    WebView webView;
    @BindView(R.id.product_catogory_head)
    RelativeLayout head;
    @BindView(R.id.top_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.test_grid_view)
    GridViewWithHeaderAndFooter content;
    @BindView(R.id.test_grid_view_frame)
    PtrClassicFrameLayout frameLayout;
    Handler handler = new Handler();


    private int page = 1;
    private String sales_num = "desc";
    private int is_new = 1;
    private String price = "asc";

    private List<ProductCategory> categories;

    boolean flag = false;
    private String tail;
    ProductAdapter adapter;

    @OnClick(R.id.product_catogory_back)
    public void clickView() {
        if (webView.getVisibility() == View.GONE) {
            this.finish();
        } else {
            webView.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            head.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (webView.getVisibility() == View.GONE) {
            this.finish();
        } else {
            webView.setVisibility(View.GONE);
//            frameLayout.setVisibility(View.VISIBLE);
//            tabLayout.setVisibility(View.VISIBLE);
//            head.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void init() {

        Intent intent = getIntent();
        branch_id = intent.getStringExtra("branch_id");
        name = intent.getStringExtra("name");
        String names[] = new String[]{"综合", "销量", "价格", "折扣"};
        adapter = new ProductAdapter(goodList, this, name);
        content.setAdapter(adapter);
        if (!flag) {
            flag = true;
            for (int i = 0; i < names.length; i++) {
                tabLayout.addTab(tabLayout.newTab().setCustomView(ProductGenerator.getTabView(CategoryProductActivity.this, names[i], i)));

            }
        }

        frameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                initData(tail);
                frameLayout.loadMoreComplete(true);
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
                        goodList.clear();
                        page = 1;
                        initData(tail);
                    }
                }, 1000);
            }
        });


        webView.getSettings().setJavaScriptEnabled(true);
        title.setText(name);
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                webView.setVisibility(View.VISIBLE);
//                frameLayout.setVisibility(View.GONE);
//                head.setVisibility(View.GONE);
//                tabLayout.setVisibility(View.GONE);
                webView.loadUrl(HttpContants.BASE_URL + "/page/commodity.html?goods_id=" + goodList.get(position).getGoods_id());
                webView.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_BACK:
                                webView.setVisibility(View.GONE);
                                content.setVisibility(View.VISIBLE);
                                tabLayout.setVisibility(View.GONE);
                                head.setVisibility(View.VISIBLE);
                                return true;

                        }
                        return false;
                    }
                });
            }
        });
//        initData("");
        tabLayout.setSelected(true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                goodList.clear();
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        tail = "&sales_num=desc";
//                        initData(tail);
                        frameLayout.autoRefresh(true);
                        break;
                    case 2:
                        tail = "&shop_price=asc";
                        frameLayout.autoRefresh(true);
//                        initData(tail);
                        break;
                }

                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    View view = tabLayout.getTabAt(i).getCustomView();
                    ImageView icon = (ImageView) view.findViewById(R.id.tab_content_image);
                    TextView text = (TextView) view.findViewById(R.id.tab_content_text);
                    if (i == tab.getPosition()) {
                        text.setTextColor(getResources().getColor(R.color.font_orange_color));
                    } else {
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_product_category;
    }


    private int type;


    private List<ProductGood> goodList = new ArrayList<>();

    public void initData(String tail) {
        Map<String, String> params = new HashMap<>();
        OkHttp.post(this, HttpContants.BASE_URL + "/Api/category/goodsList?brand_id=" + branch_id + tail + "&page=" + page, params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("data");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject goodObject = array.getJSONObject(i);
                        ProductGood good = new ProductGood();
                        good.setGoods_id(goodObject.getInt("goods_id"));
                        good.setGoods_name(goodObject.getString("goods_name"));
                        good.setStore_count(goodObject.getInt("store_count"));
                        good.setOriginal_img(goodObject.getString("original_img"));
                        good.setShop_price(goodObject.getString("shop_price"));
                        good.setMarket_price(goodObject.getString("market_price"));
                        goodList.add(good);
                    }
                    if (array.length() == 0) {
                        frameLayout.setLoadMoreEnable(false);
                        frameLayout.refreshComplete();
//                        frameLayout.loadMoreComplete(true);
                    } else {
                        show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String state, String msg) {
                Toast.makeText(CategoryProductActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }


    public void show() {
        adapter.notifyDataSetChanged();
        if (frameLayout.isRefreshing())
            frameLayout.refreshComplete();
        frameLayout.setLoadMoreEnable(true);
    }

}
