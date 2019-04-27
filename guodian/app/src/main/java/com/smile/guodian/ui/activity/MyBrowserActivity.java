package com.smile.guodian.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.smile.guodian.R;
import com.smile.guodian.model.entity.Browser;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.adapter.BrowserAdapter;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MyBrowserActivity extends BaseActivity {

    @BindView(R.id.browser_back)
    ImageView back;

    @BindView(R.id.browser_content)
    ListView listView;

    private int uid = 1;
    private BrowserAdapter adapter;
    private int page = 1;
    private List<Browser> browsers = new ArrayList<>();

    @Override
    protected void init() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBrowserActivity.this.finish();
            }
        });
        initData();
        adapter = new BrowserAdapter(this, browsers);
        listView.setAdapter(adapter);
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_browser;
    }

    public void initData() {
        Map<String, String> params = new HashMap<>();


        OkHttp.post(this, "http://guodian.staraise.com.cn/Api/index/index?user_id=" + uid + "&page=" + page, params, new OkCallback() {
            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(MyBrowserActivity.this, msg);
            }


            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {

                    JSONObject object = new JSONObject(response);
                    int code = object.getInt("code");
                    JSONObject data = object.getJSONObject("data");
                    JSONArray one = data.getJSONArray("04月17日");
                    browsers.clear();
                    for (int i = 0; i < one.length(); i++) {
                        Browser browser = new Browser();
                        JSONObject browserObj = one.getJSONObject(i);
                        browser.setCat_id(browserObj.getInt("cat_id"));
                        browser.setGoods_id(browserObj.getInt("goods_id"));
                        browser.setGoods_name(browserObj.getString("goods_name"));
                        browser.setVisittime(browserObj.getLong("visittime"));
                        browser.setShop_price(browserObj.getString("shop_price"));
                        browser.setOriginal_img(browserObj.getString("original_img"));
                        browser.setVisit_id(browserObj.getInt("visit_id"));
                        browsers.add(browser);
                    }

                    JSONArray two = data.getJSONArray("04月16日");
                    for (int i = 0; i < two.length(); i++) {
                        Browser browser = new Browser();
                        JSONObject browserObj = one.getJSONObject(i);
                        browser.setCat_id(browserObj.getInt("cat_id"));
                        browser.setGoods_id(browserObj.getInt("goods_id"));
                        browser.setGoods_name(browserObj.getString("goods_name"));
                        browser.setVisittime(browserObj.getLong("visittime"));
                        browser.setShop_price(browserObj.getString("shop_price"));
                        browser.setOriginal_img(browserObj.getString("original_img"));
                        browser.setVisit_id(browserObj.getInt("visit_id"));
                        browsers.add(browser);
                    }
                    JSONArray three = data.getJSONArray("04月08日");
                    for (int i = 0; i < three.length(); i++) {
                        Browser browser = new Browser();
                        JSONObject browserObj = one.getJSONObject(i);
                        browser.setCat_id(browserObj.getInt("cat_id"));
                        browser.setGoods_id(browserObj.getInt("goods_id"));
                        browser.setGoods_name(browserObj.getString("goods_name"));
                        browser.setVisittime(browserObj.getLong("visittime"));
                        browser.setShop_price(browserObj.getString("shop_price"));
                        browser.setOriginal_img(browserObj.getString("original_img"));
                        browser.setVisit_id(browserObj.getInt("visit_id"));
                        browsers.add(browser);
                    }
                    JSONArray four = data.getJSONArray("04月01日");
                    for (int i = 0; i < four.length(); i++) {
                        Browser browser = new Browser();
                        JSONObject browserObj = one.getJSONObject(i);
                        browser.setCat_id(browserObj.getInt("cat_id"));
                        browser.setGoods_id(browserObj.getInt("goods_id"));
                        browser.setGoods_name(browserObj.getString("goods_name"));
                        browser.setVisittime(browserObj.getLong("visittime"));
                        browser.setShop_price(browserObj.getString("shop_price"));
                        browser.setOriginal_img(browserObj.getString("original_img"));
                        browser.setVisit_id(browserObj.getInt("visit_id"));
                        browsers.add(browser);
                    }
                    adapter.setBrowsers(browsers);
                    adapter.notifyDataSetChanged();
//                    myCollections = new ArrayList<>();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
