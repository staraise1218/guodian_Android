package com.smile.guodian.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smile.guodian.R;
import com.smile.guodian.model.entity.GuessGoods;
import com.smile.guodian.ui.adapter.UserAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NavUserFragment extends Fragment {
    @BindView(R.id.user_recycle)
    RecyclerView recyclerView;
    List<GuessGoods> guessGoodsList = new ArrayList<>();

    public static NavUserFragment newInstance() {
        NavUserFragment fragment = new NavUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new UserAdapter(getContext(), guessGoodsList));
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        System.out.println(guessGoodsList.toString());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        UserAdapter userAdapter = new UserAdapter(getContext(), guessGoodsList);
        recyclerView.setAdapter(userAdapter);
        return view;
    }


}
