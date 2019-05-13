package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
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
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.Gson;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Category;
import com.smile.guodian.model.entity.ProductBean;
import com.smile.guodian.model.entity.ProductCategory;
import com.smile.guodian.model.entity.ProductGood;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.adapter.ItemTipAdapter;
import com.smile.guodian.ui.adapter.ProductAdapter;
import com.smile.guodian.ui.adapter.search.TextAdapter;
import com.smile.guodian.utils.ToastUtil;
import com.smile.guodian.widget.DataGenerator;
import com.smile.guodian.widget.HorizontalListView;
import com.smile.guodian.widget.ProductGenerator;
import com.syz.commonpulltorefresh.lib.PtrClassicFrameLayout;
import com.syz.commonpulltorefresh.lib.PtrDefaultHandler;
import com.syz.commonpulltorefresh.lib.PtrFrameLayout;
import com.syz.commonpulltorefresh.lib.loadmore.GridViewWithHeaderAndFooter;
import com.syz.commonpulltorefresh.lib.loadmore.OnLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ProductActivity extends BaseActivity {

    //    @BindView(R.id.product_content)
//    GridView content;
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
    @BindView(R.id.test_grid_view)
    GridViewWithHeaderAndFooter content;
    @BindView(R.id.test_grid_view_frame)
    PtrClassicFrameLayout frameLayout;
    Handler handler = new Handler();
    @BindView(R.id.product_tip)
    HorizontalListView horizontalListView;


    private List<ProductCategory> categories;

    boolean flag = false;

    @OnClick({R.id.product_back, R.id.product_search})
    public void clickView(View view) {

        switch (view.getId()) {
            case R.id.product_back:
                if (webView.getVisibility() == View.GONE) {
                    this.finish();
                } else {
                    webView.setVisibility(View.GONE);
                    content.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.product_search:
                Intent intent = new Intent(ProductActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }

    }

    private int type;

    private int page = 1;
    private String name;

    private List<ProductGood> goodList = new ArrayList<>();
    private String cat_id = "1";
    ProductAdapter adapter;
    private int selectPosition = 0;

    @Override
    protected void init() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);
        name = intent.getStringExtra("title");
        if (type == 4) {
            tabLayout.setVisibility(View.GONE);
        }
        adapter = new ProductAdapter(goodList, this, name);
        content.setAdapter(adapter);

        webView.getSettings().setJavaScriptEnabled(true);
        title.setText(name);
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                webView.setVisibility(View.VISIBLE);
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


        frameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                initData(cat_id);
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
                        goodList.clear();
                        page = 1;
                        initData("1");
//                        mData.clear();
//                        for (int i = 0; i < 40; i++) {
//                            mData.add(new String("GridView item  ——" + i));
//                        }
//
                    }
                }, 1000);
            }
        });


        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                TextView textView = view.findViewById(R.id.item_text);
//                textView.setTextColor(Color.parseColor("#FF9F4D"));
                selectPosition = position;
                page = 1;
                goodList.clear();
                cat_id = categories.get(position).getId() + "";
                initData(cat_id);
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println(tab.getPosition());
//                if(tab.getPosition()!=0)
                page = 1;
                goodList.clear();
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
                List<ProductGood> goods = productBean.getData().getGoodsList();
                for (int i = 0; i < goods.size(); i++) {
                    goodList.add(goods.get(i));
                }
                show();
                categories = productBean.getData().getCategoryList();

                horizontalListView.setAdapter(new TextAdapter(categories, ProductActivity.this, selectPosition));
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

    @Override
    public void onBackPressed() {
        if (webView.getVisibility() == View.GONE) {
            this.finish();
        } else {
            webView.setVisibility(View.GONE);

        }
    }


    public void show() {

        adapter.notifyDataSetChanged();
        frameLayout.refreshComplete();
        frameLayout.setLoadMoreEnable(true);

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_product;
    }
}
