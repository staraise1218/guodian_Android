package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MyBrowserActivity extends BaseActivity {

    @BindView(R.id.browser_back)
    ImageView back;

    @BindView(R.id.browser_content)
    ListView listView;

    @BindView(R.id.browser_edit)
    TextView edit;

    @BindView(R.id.browser_footer)
    RelativeLayout relativeLayout;

    @BindView(R.id.check_all)
    CheckBox radioButton;

    @OnClick({R.id.browser_edit, R.id.browser_del})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.browser_del:
                browsers = adapter.getBrowsers();
                List<Browser> browserList = new ArrayList<>();

                for (int i = 0; i < browsers.size(); i++) {
                    if (!browsers.get(i).isChecked()) {
                        browserList.add(browsers.get(i));
                    }
                }
                adapter.setBrowsers(browserList);
                adapter.notifyDataSetChanged();
                if (browserList.size() == 0) {
                    edit.setText("编辑");
                    relativeLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.browser_edit:
                if (edit.getText().toString().equals("编辑")) {
                    edit.setText("完成");
                    relativeLayout.setVisibility(View.VISIBLE);
                    adapter = new BrowserAdapter(this, browsers);
                    adapter.setShowCheck(true);
                    listView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                } else {
                    edit.setText("编辑");
                    relativeLayout.setVisibility(View.GONE);
                    adapter = new BrowserAdapter(this, browsers);
                    adapter.setShowCheck(true);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                break;
        }
    }

    private int uid = 1;
    private BrowserAdapter adapter;
    private int page = 1;
    private List<Browser> browsers = new ArrayList<>();

    @Override
    protected void init() {

        SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
        uid = sharedPreferences.getInt("uid", -1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBrowserActivity.this.finish();
            }
        });
        initData();
        adapter = new BrowserAdapter(this, browsers);
        listView.setAdapter(adapter);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.setCheckAll(true);
                } else {
                    adapter.setCheckAll(false);
                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_browser;
    }

    public void initData() {
        Map<String, String> params = new HashMap<>();
        OkHttp.post(this, "http://guodian.staraise.com.cn/Api/user/visit_log?user_id=" + uid + "&page=" + page, params, new OkCallback() {
            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(MyBrowserActivity.this, msg);
            }

            @Override
            public void onResponse(String response) {
                browsers.clear();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    Iterator<String> iterator = data.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        JSONArray one = data.getJSONArray(key);
                        for (int i = 0; i < one.length(); i++) {
                            Browser browser = new Browser();
                            if (i == 0) {
                                browser.setTitle(key);
                            }
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
                    }

                    adapter.setBrowsers(browsers);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
