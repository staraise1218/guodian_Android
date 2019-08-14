package com.smile.guodian.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.ui.activity.CategoryProductActivity;
import com.smile.guodian.ui.activity.ProductActivity;
import com.smile.guodian.ui.activity.WebActivity;
import com.smile.guodian.ui.activity.message.MessageCenterActivity;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


public class NavCartFragment extends Fragment {
    private WebView webView;
    private int uid;


    public static NavCartFragment newInstance() {
        NavCartFragment fragment = new NavCartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void reload() {
//        System.out.println("reload");
        if (webView != null) {
            webView.reload();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @JavascriptInterface
    public void goBack() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

    }

    @JavascriptInterface
    public void goMessageCenter() {
        Intent intent = new Intent(getContext(), WebActivity.class);
        intent.putExtra("type", 20);
        intent.putExtra("url", HttpContants.BASE_URL + "/page/message.html");
//        Intent intent = new Intent(getContext(), MessageCenterActivity.class);
//        startActivity(intent);
//        Intent intent = new Intent(getActivity(), MessageCenterActivity.class);
        startActivity(intent);
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
        oks.show(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("context");
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("db", Context.MODE_PRIVATE);
        uid = sharedPreferences.getInt("uid", -1);
        webView = view.findViewById(R.id.bag_web);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(NavCartFragment.this, "android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                System.out.println(url);
                if (url.contains("commodity")) {
                    Intent intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra("goodsId", url.split("=")[1]);
                    startActivity(intent);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                System.out.println(url);
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Alert");
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
        if (uid != -1)
            webView.loadUrl(HttpContants.BASE_URL + "/page/shoppingBag.html?user_id=" + uid);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.loadUrl(HttpContants.BASE_URL + "/page/shoppingBag.html?user_id=" + uid);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
