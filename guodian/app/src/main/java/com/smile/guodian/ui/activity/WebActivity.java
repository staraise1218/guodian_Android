package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;

public class WebActivity extends BaseActivity {

    private String name;
    private String goodsid;
    private int type;
    private String url;

    @BindView(R.id.product_head)
    RelativeLayout head;
    @BindView(R.id.bag_title)
    TextView title;
    @BindView(R.id.bag_web)
    WebView webView;


    @OnClick(R.id.bag_back)
    public void clickView(View view) {
        this.finish();
    }

    @Override
    protected void init() {

        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        name = intent.getStringExtra("name");
        goodsid = intent.getStringExtra("goodsId");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        if (type > -1) {

//            Toast.makeText(this, "123" + url, Toast.LENGTH_LONG).show();
            if (type == 2) {
                url = "http://guodian.staraise.com.cn/page/article.html?type=" + type;
                title.setText("鉴定机制");
            }
            if (type == 1) {
                url = "http://guodian.staraise.com.cn/page/article.html?type=" + type;
                title.setText("探索国典");
            }
            if (type == 3) {
                url = "http://guodian.staraise.com.cn/page/article.html?type=" + type;
                title.setText("文章");
            }
            if (type == 4) {
                url = "http://guodian.staraise.com.cn/page/orderWait.html";
                title.setText("待付款");
            }
            if (type == 5) {
                url = "http://guodian.staraise.com.cn/page/orderReceive.html";
                title.setText("待收货");
            }
            if (type == 6) {
                url = "http://guodian.staraise.com.cn/page/orderReturn.html";
                title.setText("退货单");
            }
            if (type == 7) {
                url = "http://guodian.staraise.com.cn/page/orderAll.html";
                title.setText("全部订单");
            }
            if (type == 8) {
                url = "http://guodian.staraise.com.cn/page/myCoupon.html";
                title.setText("优惠券");
            }
            if (type == 9) {
                url = "http://guodian.staraise.com.cn/page/myIntegral.html";
                title.setText("我的积分");
            }

        } else {
            head.setVisibility(View.GONE);
            url = "http://guodian.staraise.com.cn/page/commodity.html?goods_id=" + goodsid;
            title.setText(name);

        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不另跳浏览器
                // 在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                    webView.stopLoading();
                    return true;
                }
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });


        webView.loadUrl(url);
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        WebActivity.this.finish();
                        return true;

                }
                return false;
            }
        });

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.fragment_navigation_cart;
    }
}
