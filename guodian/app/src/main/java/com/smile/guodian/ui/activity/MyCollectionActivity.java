package com.smile.guodian.ui.activity;

import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Collection;
import com.smile.guodian.model.entity.Inventory;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.adapter.InventoryAdapter;
import com.smile.guodian.widget.SlideRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MyCollectionActivity extends BaseActivity {
    private SlideRecyclerView recycler_view_list;
    private List<Inventory> mInventories;
    private InventoryAdapter mInventoryAdapter;
    @BindView(R.id.collection_back)
    ImageView back;
    Collection collection = new Collection();
    private int uid = -1;
    private int page = 1;
    List<Collection.MyCollection> myCollections;

    @Override
    protected void init() {
        SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
        uid = sharedPreferences.getInt("uid", -1);
        recycler_view_list = (SlideRecyclerView) findViewById(R.id.recycler_view_list);
        recycler_view_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_inset));
        recycler_view_list.addItemDecoration(itemDecoration);
        mInventories = new ArrayList<>();
        Inventory inventory;
        Random random = new Random();
        initData();
//        for (int i = 0; i < 50; i++) {
//            inventory = new Inventory();
//            inventory.setItemDesc("测试数据" + i);
//            inventory.setQuantity(random.nextInt(100000));
//            inventory.setItemCode("0120816");
//            inventory.setDate("20180219");
//            inventory.setVolume(random.nextFloat());
//            mInventories.add(inventory);
//        }
//        mInventoryAdapter = new InventoryAdapter(this, collection.getData());
//        recycler_view_list.setAdapter(mInventoryAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCollectionActivity.this.finish();
            }
        });
    }

    public void initData() {
        Map<String, String> params = new HashMap<>();

//        params.put("uer_id","1");
//        params.put("page","1");
//        Gson gson = new Gson();
//        String paramStr = gson.toJson(params);
//        System.out.println(paramStr);
//        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, paramStr);
//        RequestBody body = RequestBody.create(MediaType.parse())

        OkHttp.post(this,  HttpContants.BASE_URL+"/Api/user/collectGoodslist?user_id=" + uid + "&page=" + page, params, new OkCallback() {
            @Override
            public void onFailure(String state, String msg) {
                System.out.println(state + msg);
            }


            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject object = new JSONObject(response);
                    int data = object.getInt("code");
                    JSONArray array = object.getJSONArray("data");
                    myCollections = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        Collection.MyCollection myCollection = collection.new MyCollection();
                        JSONObject jsonCollection = array.getJSONObject(i);
                        myCollection.setCollect_id(jsonCollection.getInt("collect_id"));
                        myCollection.setAdd_time(jsonCollection.getLong("add_time"));
                        myCollection.setGoods_name(jsonCollection.getString("goods_name"));
                        myCollection.setOriginal_img(jsonCollection.getString("original_img"));
                        myCollection.setShop_price(jsonCollection.getString("shop_price"));
                        myCollection.setStore_count(jsonCollection.getInt("store_count"));
                        myCollections.add(myCollection);
                    }

                    collection.setData(myCollections);
                    mInventoryAdapter = new InventoryAdapter(MyCollectionActivity.this, collection.getData());
                    recycler_view_list.setAdapter(mInventoryAdapter);
                    mInventoryAdapter.setOnDeleteClickListener(new InventoryAdapter.OnDeleteClickLister() {
                        @Override
                        public void onDeleteClick(View view, int position) {
                            delete(position);

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void delete(final int position) {
        Map<String, String> params = new HashMap<>();


        OkHttp.post(this,  HttpContants.BASE_URL+"/Api/user/cancel_collect?user_id=" + uid + "&collect_id=" + myCollections.get(position).getCollect_id(), params, new OkCallback() {
            @Override
            public void onFailure(String state, String msg) {
                System.out.println(state + msg);
            }


            @Override
            public void onResponse(String response) {
//                System.out.println(response);

                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int data = object.getInt("code");
                    if (data == 200) {
                        myCollections.remove(position);
                        mInventoryAdapter.notifyDataSetChanged();
                        recycler_view_list.closeMenu();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_collection;
    }
}

