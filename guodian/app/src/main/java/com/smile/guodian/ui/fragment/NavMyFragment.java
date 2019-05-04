package com.smile.guodian.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Category;
import com.smile.guodian.model.entity.CategoryBean;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.adapter.UserAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;


public class NavMyFragment extends Fragment {
    @BindView(R.id.user_recycle)
    RecyclerView recyclerView;

    public static NavMyFragment newInstance() {
        NavMyFragment fragment = new NavMyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void initData(){
//        OkHttpUtils.post().url(HttpContants.CATEGORY_LIST).build()
//                .execute(new StringCallback() {
//
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
////                        LogUtil.e("分类一级", e + "", true);true
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        System.out.println(response + "");
//                        int position = 0;
//                        int currentPosition = 0;
//                        Type collectionType = new TypeToken<CategoryBean>() {
//                        }.getType();
//                        CategoryBean enums = mGson.fromJson(response, collectionType);
//                        Iterator<Category> iterator = enums.getData().iterator();
//                        while (iterator.hasNext()) {
//                            Category bean = iterator.next();
//                            position++;
//                            if ((categoryId + "" ).equals( bean.getId())) {
//                                currentPosition = position;
//                            }
//                            categoryFirst.add(bean);
//                        }
//                        System.out.println(currentPosition);
//                        if(currentPosition!=0) {
//                            showCategoryData(currentPosition - 1);
//                        }else {
//                            showCategoryData(0);
//                        }
//
//                        if (categoryId == 0)
//                            defaultClick();
//                        else {
//                            NavCategoryFragment.this.requestWares(categoryId + "");
//                        }
//
//                    }
//                });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_navigation_user, container, false);
        ButterKnife.bind(this,view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext() );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new UserAdapter(getContext()));
        return view;
    }
}
