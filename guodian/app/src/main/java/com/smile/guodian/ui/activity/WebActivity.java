package com.smile.guodian.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.ui.activity.message.MessageCenterActivity;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class WebActivity extends BaseActivity {

    private String name;
    private String goodsid;
    private int type;
    private String url;
    private String article_id;

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
        url = intent.getStringExtra("url");
        article_id = intent.getStringExtra("article_id");

        System.out.println(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        if (type > -1) {
            head.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "123" + url, Toast.LENGTH_LONG).show();
            if (type == 2) {
                url = HttpContants.BASE_URL + "/page/article.html?type=" + type;
                title.setText("鉴定机制");
                head.setVisibility(View.GONE);
            }
            if (type == 1) {
                url = HttpContants.BASE_URL + "/page/article.html?type=" + type;
                title.setText("探索国典");
                head.setVisibility(View.GONE);
            }
            if (type == 3) {
                url = HttpContants.BASE_URL + "/page/article.html?type=" + type;
                title.setText("文章");
                head.setVisibility(View.GONE);
            }
            if (type == 19) {
                url = HttpContants.BASE_URL + "/page/article2.html?article_id=" + article_id;
                title.setText("文章");
                head.setVisibility(View.GONE);
            }
            if (type == 4) {
                url = HttpContants.BASE_URL + "/page/myOrder.html?type=WAITPAY";
                title.setText("待付款");
                head.setVisibility(View.GONE);
            }
            if (type == 5) {
                url = HttpContants.BASE_URL + "/page/myOrder.html?type=WAITRECEIVE";
                title.setText("待收货");
                head.setVisibility(View.GONE);
            }
            if (type == 10) {
                url = HttpContants.BASE_URL + "/page/article.html?type=" + 4;
                title.setText("文章");
                head.setVisibility(View.GONE);
            }
            if (type == 11) {
                url = HttpContants.BASE_URL + "/page/article.html?type=" + 5;
                title.setText("文章");
                head.setVisibility(View.GONE);
            }
            if (type == 16) {
                url = HttpContants.BASE_URL + "/page/article.html?type=" + 6;
                title.setText("文章");
                head.setVisibility(View.GONE);
            }
            if (type == 17) {
                url = HttpContants.BASE_URL + "/page/article.html?type=" + 7;
                title.setText("文章");
                head.setVisibility(View.GONE);
            }
            if (type == 18) {
                url = HttpContants.BASE_URL + "/page/myMember.html";
                title.setText("文章");
                head.setVisibility(View.GONE);
            }


            if (type == 6) {
                url = HttpContants.BASE_URL + "/page/orderReturn.html";
                title.setText("退货单");
                head.setVisibility(View.GONE);
            }
            if (type == 7) {
                url = HttpContants.BASE_URL + "/page/myOrder.html?type=ALL";
                title.setText("全部订单");
                head.setVisibility(View.GONE);
            }
            if (type == 8) {
                url = HttpContants.BASE_URL + "/page/myCoupon.html";
                title.setText("优惠券");
                head.setVisibility(View.GONE);
            }
            if (type == 9) {
                url = HttpContants.BASE_URL + "/page/myIntegral.html";
                title.setText("我的积分");
                head.setVisibility(View.GONE);
            }
            if (type == 20) {
                head.setVisibility(View.GONE);
            }

        } else {
//            head.setVisibility(View.GONE);
            url = HttpContants.BASE_URL + "/page/commodity.html?goods_id=" + goodsid;
            title.setText(name);
            System.out.println(url);
        }

        webView.addJavascriptInterface(WebActivity.this, "android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(WebActivity.this);
                b.setTitle("Alert11");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

            //设置响应js 的Confirm()函数
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                return true;
            }
        });
        webView.reload();

        webView.loadUrl(url);
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

    @JavascriptInterface
    public void goBack() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.print("back");
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    WebActivity.this.finish();
                }
            }
        });
    }

    @JavascriptInterface
    public void goCart() {
        Intent intent = new Intent(WebActivity.this, MainActivity.class);
        intent.putExtra("type", 3);
        startActivity(intent);
    }

    @JavascriptInterface
    public void goMessageCenter() {
        Intent intent = new Intent(WebActivity.this, MessageCenterActivity.class);
        startActivity(intent);
    }

    @JavascriptInterface
    public void showShare(String title, String url, String text, String imageUrl, String type) {
//        System.out.println("tppe" + type);
        OnekeyShare oks = new OnekeyShare();
        if (type.equals("webo")) {
//            goCart();
            oks.setPlatform(SinaWeibo.NAME);
        } else if (type.equals("wx")) {
            oks.setPlatform(Wechat.NAME);
        } else {
            oks.setPlatform(QQ.NAME);
        }
//        oks.setPlatform(QQ.NAME);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(title);
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // imageUrl是图片的路径
        oks.setImageUrl(imageUrl);
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(url);
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.fragment_navigation_cart;
    }
}
