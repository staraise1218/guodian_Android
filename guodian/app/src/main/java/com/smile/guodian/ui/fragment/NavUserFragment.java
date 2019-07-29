package com.smile.guodian.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smile.guodian.R;
import com.smile.guodian.UserDao;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.GuessGoods;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.LoginActivity;
import com.smile.guodian.ui.adapter.UserAdapter;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NavUserFragment extends Fragment {
    @BindView(R.id.user_recycle)
    RecyclerView recyclerView;
    List<GuessGoods> guessGoodsList = new ArrayList<>();
    UserAdapter adapter;

    public static NavUserFragment newInstance() {
        NavUserFragment fragment = new NavUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println("Show  ");
    }

    @Override
    public void onResume() {
        super.onResume();

        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new UserAdapter(getContext(), guessGoodsList);
            recyclerView.setAdapter(adapter);
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    };

    public void updateData() {

        UserDao userDao = BaseApplication.getDaoSession().getUserDao();
        List<User> users = userDao.loadAll();
        if (users.size() == 0)
            return;
        User user = users.get(0);
        System.out.println(user.getMobile());

        Map<String, String> params = new HashMap<>();
        params.put("user_id", user.getUser_id());

        OkHttp.post(getContext(), HttpContants.BASE_URL + "/Api/user/index", params, new OkCallback() {
            @Override
            public void onResponse(String response) {
//                System.out.println(response);

                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject result = null;
                try {
                    result = object.getJSONObject("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    JSONObject userInfo = result.getJSONObject("userInfo");
                    int waitPay = userInfo.getInt("waitPay");
                    int waitReceive = userInfo.getInt("waitReceive");
                    int returnCount = userInfo.getInt("return_count");
                    if (adapter == null) {
                        return;
                    }

                    adapter.setReceiveCount(waitReceive);
                    adapter.setReturnCount(returnCount);
                    adapter.setWaitCount(waitPay);

                    handler.sendEmptyMessage(1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(getContext(), msg);
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("oncreate");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_user, container, false);
        ButterKnife.bind(this, view);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("db", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("guess", "");


        JSONArray array = null;
        try {
            array = new JSONArray(data);
            for (int i = 0; i < array.length(); i++) {
                GuessGoods guessGoods = new GuessGoods();
                JSONObject object = array.getJSONObject(i);
                guessGoods.setGoods_id(object.getInt("goods_id"));
                guessGoods.setGoods_name(object.getString("goods_name"));
                guessGoods.setStore_count(object.getInt("store_count"));
                guessGoods.setOriginal_img(object.getString("original_img"));
                guessGoods.setShop_price(object.getString("shop_price"));
                guessGoods.setMarket_price(object.getString("market_price"));
                guessGoodsList.add(guessGoods);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        UserAdapter userAdapter = new UserAdapter(getContext(), guessGoodsList);
        recyclerView.setAdapter(userAdapter);

        System.out.println("oncreateView");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("onViewCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("onAttach");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("onSaveInstanceState");
    }


}
