package com.smile.guodian.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Category;
import com.smile.guodian.model.entity.ProductBean;
import com.smile.guodian.model.entity.ProductCategory;
import com.smile.guodian.model.entity.ProductGood;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.activity.message.MessageCenterActivity;
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
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

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
//        if (webView.getVisibility() == View.GONE) {
//            this.finish();
//        } else {
//            webView.setVisibility(View.GONE);
////            frameLayout.setVisibility(View.VISIBLE);
////            tabLayout.setVisibility(View.VISIBLE);
////            head.setVisibility(View.VISIBLE);
//        }
    }


    @Override
    protected void init() {

        Intent intent = getIntent();
        branch_id = intent.getStringExtra("branch_id");
        name = intent.getStringExtra("name");
        title.setText(name);
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

//        final WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setDomStorageEnabled(true);
//        title.setText(name);
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent1 = new Intent(CategoryProductActivity.this, WebActivity.class);
                intent1.putExtra("goodsId", goodList.get(position).getGoods_id() + "");
                startActivity(intent1);
//
//                webView.setVisibility(View.VISIBLE);
//                webView.setWebChromeClient(new WebChromeClient() {
//                    @Override
//                    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//                        AlertDialog.Builder b = new AlertDialog.Builder(CategoryProductActivity.this);
//                        b.setTitle("Alert");
//                        b.setMessage(message);
//                        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                result.confirm();
//                            }
//                        });
//                        b.setCancelable(false);
//                        b.create().show();
//                        return true;
//                    }
//
//                    //设置响应js 的Confirm()函数
//                    @Override
//                    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
//                        return true;
//                    }
//
//                    //设置响应js 的Prompt()函数
//                    @Override
//                    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
//                        return true;
//                    }
//                });
//
//                webView.setWebViewClient(new WebViewClient() {
//                    //                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
////                        System.out.println(request.getUrl().getEncodedPath());
//                        return super.shouldOverrideUrlLoading(view, request);
//                    }
//                });
//                webView.addJavascriptInterface(CategoryProductActivity.this, "android");
//                webView.loadUrl(HttpContants.BASE_URL + "/page/commodity.html?goods_id=" + goodList.get(position).getGoods_id());

//                webView.setOnKeyListener(new View.OnKeyListener() {
//                    @Override
//                    public boolean onKey(View v, int keyCode, KeyEvent event) {
//                        switch (keyCode) {
//                            case KeyEvent.KEYCODE_BACK:
//                                webView.setVisibility(View.GONE);
//                                content.setVisibility(View.VISIBLE);
//                                tabLayout.setVisibility(View.GONE);
//                                head.setVisibility(View.VISIBLE);
//                                return true;
//
//                        }
//                        return false;
//                    }
//                });
            }
        });
//        initData("");
        tabLayout.setSelected(true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                goodList.clear();
                page = 1;
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

    @JavascriptInterface
    public void goBack() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    CategoryProductActivity.this.finish();
                }
            }
        });
    }

    @JavascriptInterface
    public void goMessageCenter() {
        Intent intent = new Intent(CategoryProductActivity.this, MessageCenterActivity.class);
        startActivity(intent);
    }

    @JavascriptInterface
    public void showShare(String title, String url, String text, String imageUrl, String type) {

//        System.out.println(type);

        OnekeyShare oks = new OnekeyShare();
        if (type.equals("webo")) {
            oks.setPlatform(SinaWeibo.NAME);
        } else if (type.equals("wx")) {
            oks.setPlatform(Wechat.NAME);
        } else {
            oks.setPlatform(QQ.NAME);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(title);
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(imageUrl);//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网使用
//        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
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

//                System.out.println("---------"+response);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
        }
    }

    public void show() {
        adapter.notifyDataSetChanged();
        if (frameLayout.isRefreshing())
            frameLayout.refreshComplete();
        frameLayout.setLoadMoreEnable(true);
    }

}
