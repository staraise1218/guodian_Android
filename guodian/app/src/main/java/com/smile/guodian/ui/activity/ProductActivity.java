package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Category;
import com.smile.guodian.model.entity.ProductBean;
import com.smile.guodian.model.entity.ProductCategory;
import com.smile.guodian.model.entity.ProductGood;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.adapter.ProductAdapter;
import com.smile.guodian.utils.ToastUtil;
import com.smile.guodian.widget.DataGenerator;
import com.smile.guodian.widget.ProductGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ProductActivity extends BaseActivity {

    @BindView(R.id.product_content)
    GridView content;
    @BindView(R.id.product_title)
    TextView title;
    @BindView(R.id.product_back)
    ImageView back;
    @BindView(R.id.web)
    WebView webView;
    @BindView(R.id.product_head)
    RelativeLayout head;
    @BindView(R.id.top_tab_layout)
    TabLayout tabLayout;

    private List<ProductCategory> categories;

    boolean flag = false;

    @OnClick(R.id.product_back)
    public void clickView() {
        if (webView.getVisibility() == View.GONE) {
            this.finish();
        } else {
            webView.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
        }
    }

    private int type;

    private int page = 1;
    private String name;

    private List<ProductGood> goodList;
    private String cat_id;

    @Override
    protected void init() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);
        name = intent.getStringExtra("title");
        if (type == 4) {
            tabLayout.setVisibility(View.GONE);
        }

//        content.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem == (totalItemCount - visibleItemCount) && cat_id != null) {
//
//                    page++;
//                    initData(cat_id);
//
//                }
//            }
//        });

        webView.getSettings().setJavaScriptEnabled(true);
        title.setText(name);
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                webView.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
                head.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                System.out.println(goodList.get(position).getGoods_id());
                webView.loadUrl("http://guodian.staraise.com.cn/page/commodity.html?goods_id=" + goodList.get(position).getGoods_id());
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
        initData("1");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println(tab.getPosition());
//                if(tab.getPosition()!=0)
                page = 1;
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    View view = tabLayout.getTabAt(i).getCustomView();
                    ImageView icon = (ImageView) view.findViewById(R.id.tab_content_image);
                    TextView text = (TextView) view.findViewById(R.id.tab_content_text);
                    if (i == tab.getPosition()) {
                        text.setTextColor(getResources().getColor(R.color.font_orange_color));
//                        text.setTextColor(Color.parseColor("#DDA021"));
                    } else {
                        text.setTextColor(getResources().getColor(android.R.color.black));
//                        text.setTextColor(getResources().getColor(android.R.color.black));
                    }
                }

                cat_id = categories.get(tab.getPosition()).getId() + "";

                initData(cat_id);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void initData(String cat_id) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type + "");
        params.put("cat_id", cat_id);
        params.put("page", page + "");


        OkHttp.post(this, HttpContants.BASE_URL + "/Api/index/goodslist", params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ProductBean productBean = gson.fromJson(response, ProductBean.class);
                goodList = productBean.getData().getGoodsList();
                show();
                categories = productBean.getData().getCategoryList();
                if (!flag) {
                    flag = true;
                    for (int i = 0; i < categories.size(); i++) {
                        tabLayout.addTab(tabLayout.newTab().setCustomView(ProductGenerator.getTabView(ProductActivity.this, categories.get(i).getCat_name(), i)));

                    }
                }

            }

            @Override
            public void onFailure(String state, String msg) {
            }
        });
    }


    public void show() {
        ProductAdapter adapter = new ProductAdapter(goodList, this, name);
        content.setAdapter(adapter);

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_product;
    }
}
