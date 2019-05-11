package com.smile.guodian.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smile.guodian.HistoryDao;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.GuessGoods;
import com.smile.guodian.model.entity.History;
import com.smile.guodian.model.entity.SearchEntity;
import com.smile.guodian.model.entity.SearchResultEntity;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.adapter.SearchAdapter;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

import static android.content.Context.MODE_PRIVATE;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.search_search)
    EditText search;

    @BindView(R.id.search_cancel)
    TextView cancel;

    @BindView(R.id.search_content)
    RecyclerView recyclerView;

    @OnClick(R.id.search_cancel)
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.search_cancel:
                SearchActivity.this.finish();
                break;
        }
    }

    private boolean history;

    private int page = 0;
    private List<Integer> type = new ArrayList<>();
    private SearchAdapter searchAdapter;
    private List<String> hot = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void init() {

        sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
        history = sharedPreferences.getBoolean("history", false);

        hot.add("龙珠系列");
        hot.add("LV 口红包");
        hot.add("劳力士");
        hot.add("LV 白棋盘格NOENOE");
        hot.add("LV LOCKY BB");
        hot.add("Tom Ford粉色圆框");

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 5) {
                    page = 0;
                    String keyword = search.getText().toString();
                    search(keyword);
                    return true;
                }
                return false;
            }
        });
        searchAdapter = new SearchAdapter(type, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(searchAdapter);
        initData();


    }

    public void initData() {

        List<History> histories = BaseApplication.getDaoSession().getHistoryDao().loadAll();
        final String[] hist = new String[histories.size()];
        for (int i = 0; i < histories.size(); i++) {
            hist[i] = histories.get(i).getHistory();
        }

        type.clear();
        Map<String, String> params = new HashMap<>();

        OkHttp.post(this, HttpContants.BASE_URL + "/Api/goods/searchPage", params, new OkCallback() {
            @Override
            public void onResponse(String response) {

                System.out.println(response);
                Gson gson = new Gson();
                JSONObject ob = null;
                JSONObject data = null;
                try {
                    ob = new JSONObject(response);
                    data = ob.getJSONObject("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<GuessGoods> guessGoodsList = new ArrayList<>();


                JSONArray array = null;
                try {
                    array = data.getJSONArray("favourite_goods");
                    for (int i = 0; i < array.length(); i++) {
                        GuessGoods guessGoods = new GuessGoods();
                        JSONObject object = array.getJSONObject(i);
                        guessGoods.setGoods_id(object.getInt("goods_id"));
                        guessGoods.setGoods_name(object.getString("goods_name"));
                        guessGoods.setStore_count(object.getInt("store_count"));
                        guessGoods.setOriginal_img(object.getString("original_img"));
                        guessGoods.setShop_price(object.getString("shop_price"));
                        guessGoodsList.add(guessGoods);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SearchEntity searchEntity = gson.fromJson(response, SearchEntity.class);
                type.add(2);
                if (!history)
                    type.add(4);
                type.add(3);
                searchAdapter.setHot(hot);
                searchAdapter.setType(type);
                searchAdapter.setOnClickListener(SearchActivity.this);
                searchAdapter.setList(hist);
                searchAdapter.setGuessGoods(guessGoodsList);
                searchAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(SearchActivity.this, msg);
            }
        });

    }


    public void search(final String keyword) {
        page++;
        Map<String, String> params = new HashMap<>();
        OkHttp.post(this, HttpContants.BASE_URL + "/Api/goods/goodslist?keyword=" + keyword + "&page=" + page, params, new OkCallback() {
            @Override
            public void onResponse(String response) {
//                System.out.println(response);

                type.clear();
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject data = object.getJSONObject("data");
                    Gson gson = new Gson();
                    SearchResultEntity resultEntity = gson.fromJson(data.toString(), SearchResultEntity.class);
                    if (resultEntity.getList().size() != 0) {
                        type.add(5);
                    } else {
                        type.add(1);
                        type.add(3);
                    }

                    History history = new History();
                    history.setHistory(keyword);
                    BaseApplication.getDaoSession().getHistoryDao().insert(history);

//                    searchAdapter = new SearchAdapter(type, SearchActivity.this);
                    searchAdapter.setType(type);
                    searchAdapter.setResultEntity(resultEntity);
//                    recyclerView.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(SearchActivity.this, msg);
            }
        });

    }


    @Override
    protected int getContentResourseId() {
        return R.layout.activity_search;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dialog_ok:
                break;
            case R.id.del_history:

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog dialog = builder.create();
                View view = View.inflate(this, R.layout.dialog_del, null);

                TextView del = view.findViewById(R.id.dialog_close);
                TextView cancel = view.findViewById(R.id.dialog_cancel);
                TextView ok = view.findViewById(R.id.dialog_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("history", true);
                        editor.commit();
                        history = true;
                        initData();

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.setView(view);
                dialog.show();

                break;
        }

    }
}
