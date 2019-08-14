package com.smile.guodian.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.smile.guodian.ui.activity.message.MessageCenterActivity;
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
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class ProductActivity extends BaseActivity {

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

    @JavascriptInterface
    public void goBack() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    ProductActivity.this.finish();
                }
            }
        });

    }

    @JavascriptInterface
    public void goMessageCenter() {
        Intent intent = new Intent(ProductActivity.this, WebActivity.class);
        intent.putExtra("type", 20);
        intent.putExtra("url", HttpContants.BASE_URL + "/page/message.html");
        startActivity(intent);
//        Intent intent = new Intent(ProductActivity.this, MessageCenterActivity.class);
//        startActivity(intent);
    }

    @JavascriptInterface
    public void showShare(String title, String url, String text, String imageUrl, String type) {
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
    protected void init() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);
        name = intent.getStringExtra("title");
        if (type == 4) {
            horizontalListView.setVisibility(View.GONE);
        }
        adapter = new ProductAdapter(goodList, this, name);
        content.setAdapter(adapter);

        webView.getSettings().setJavaScriptEnabled(true);
        title.setText(name);
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent1 = new Intent(ProductActivity.this, WebActivity.class);
//                System.out.println(goodList.get(position).getGoods_id()+"-----");
                intent1.putExtra("goodsId", goodList.get(position).getGoods_id() + "");
                startActivity(intent1);
//                webView.setVisibility(View.VISIBLE);
//                webView.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                        super.onPageStarted(view, url, favicon);
//                    }
//
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
//                        super.onPageFinished(view, url);
//                    }
//
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                            return super.shouldOverrideUrlLoading(view, url);
//                    }
//
//                    @Override
//                    public void onReceivedError(WebView view, int errorCode,
//                                                String description, String failingUrl) {
//                        super.onReceivedError(view, errorCode, description, failingUrl);
//                    }
//                });
//                webView.setWebChromeClient(new WebChromeClient() {
//                    @Override
//                    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//                        AlertDialog.Builder b = new AlertDialog.Builder(ProductActivity.this);
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
//                    //设置响应js 的Confirm()函数
//                    @Override
//                    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
//                        return true;
//                    }
//                    //设置响应js 的Prompt()函数
//                    @Override
//                    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
//                        return true;
//                    }
//                });
//                webView.addJavascriptInterface(ProductActivity.this, "android");
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


        frameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                initData(cat_id);
                frameLayout.loadMoreComplete(true);
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
                        initData(cat_id);
                    }
                }, 1000);
            }
        });


        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                page = 1;
                goodList.clear();
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
                System.out.println(response);
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
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_product;
    }
}
