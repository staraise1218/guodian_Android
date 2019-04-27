package com.smile.guodian.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Find;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.adapter.StaggeredRecycleViewAdapter;
import com.smile.guodian.utils.ToastUtil;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NavWeFragment extends Fragment {

    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private int mCount = 1;
    private StaggeredRecycleViewAdapter mRecyclerViewAdapter;
    private int uid;

    private List<Find> finds = new ArrayList<>();

    public static NavWeFragment newInstance() {
        NavWeFragment fragment = new NavWeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
        //mPullLoadMoreRecyclerView.setRefreshing(true);
        mPullLoadMoreRecyclerView.setStaggeredGridLayout(2);
        mRecyclerViewAdapter = new StaggeredRecycleViewAdapter(getActivity(), finds);
        mRecyclerViewAdapter.setFinds(finds);
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener());
        uid = getContext().getSharedPreferences("db", Context.MODE_PRIVATE).getInt("uid", -1);
//        pullData();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_we, container, false);
    }

    private List<Map<String, String>> setList() {
        List<Map<String, String>> dataList = new ArrayList<>();
        int start = 30 * (mCount - 1);
        Map<String, String> map;
        for (int i = start; i < 30 * mCount; i++) {
            map = new HashMap<>();
            map.put("text", "Third" + i);
            map.put("height", (200 + 10 * i) + "");
            dataList.add(map);
        }
        return dataList;

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    System.out.println("handler");
//                    mRecyclerViewAdapter.getDataList().addAll(setList());
                    mRecyclerViewAdapter.setFinds(finds);
                    mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    break;
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            pullData();
            System.out.println(hidden);
        }
    }

    public void pullData() {
        Map<String, String> params = new HashMap<>();
//        params.put("user_id", "1");

//        Gson gson = new Gson();
//        String paramStr = gson.toJson(params);
//        System.out.println(paramStr);
//        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, paramStr);
//        Request request = new Request.Builder().url(HttpContants.BASE_URL + "/Api/find/index").post(body).build();
//        new OkHttpClient().newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                System.out.println("err");
//            }
//
//            @Override
//            public void onResponse(Call call, Response res) throws IOException {
//                String response = res.body().string();
//                System.out.println(response);
//                JSONObject object = null;
//                try {
//                    object = new JSONObject(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                JSONObject result = null;
//                try {
//                    result = object.getJSONObject("data");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                JSONArray array = null;
//                try {
//                    array = result.getJSONArray("list");
//                    for (int i = 0; i < array.length(); i++) {
//                        Find find = new Find();
//                        JSONObject findOb = array.getJSONObject(i);
//                        find.setArticle_id(findOb.getInt("article_id"));
//                        find.setTitle(findOb.getString("title"));
//                        find.setTumb(findOb.getString("thumb"));
//                        find.setLike_num(findOb.getString("like_num"));
//                        find.setIsliked(findOb.getInt("isliked"));
//                    }
//                    handler.sendEmptyMessage(1);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//    }

//        params.put("password", pwd);

//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.

        OkHttp.post(getContext(), HttpContants.BASE_URL+"/Api/find/index?user_id="+uid, params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
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
                JSONArray array = null;
                try {
                    array = result.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        Find find = new Find();
                        JSONObject findOb = array.getJSONObject(i);
                        find.setArticle_id(findOb.getInt("article_id"));
                        find.setTitle(findOb.getString("title"));
                        find.setTumb(findOb.getString("thumb"));
                        find.setLike_num(findOb.getString("like_num"));
                        find.setIsliked(findOb.getInt("isliked"));
                        finds.add(find);
                        System.out.println(find.toString());
                    }
                    handler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String state, String msg) {
//                System.out.println(state+msg);
                ToastUtil.showShortToast(getContext(), msg);
            }
        });


    }




    private void getData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerViewAdapter.getDataList().addAll(setList());
                mRecyclerViewAdapter.notifyDataSetChanged();
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
            }
        }, 1000);
    }

    class PullLoadMoreListener implements PullLoadMoreRecyclerView.PullLoadMoreListener {
        @Override
        public void onRefresh() {
            setRefresh();
            pullData();
        }

        @Override
        public void onLoadMore() {
            mCount = mCount + 1;
            pullData();
        }
    }

    private void setRefresh() {

        mRecyclerViewAdapter.getFinds().clear();

        mCount = 1;

    }
}
