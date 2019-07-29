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
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Find;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.adapter.StaggeredAdapter;
import com.smile.guodian.ui.adapter.StaggeredRecycleViewAdapter;
import com.smile.guodian.utils.ToastUtil;
import com.smile.guodian.widget.refresh.XMultiColumnListView;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NavFoundFragment extends Fragment {


    private View mView;
    private Context mContext;
    private static final String ARG_POSITION = "position";
    private int position;
    private Handler mHandler;


    private StaggeredAdapter mAdapter = null;
    private int currentPage = 0;
    //    ContentTask task = new ContentTask(getActivity(), 2);
    private int mType = 2;

    private XMultiColumnListView mListView;
    private boolean isRunning = false;

    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private int mCount = 1;
    private StaggeredRecycleViewAdapter mRecyclerViewAdapter;
    private int uid;
    private TextView title;

    private List<Find> finds = new ArrayList<>();
    private int page = 1;

    public static NavFoundFragment newInstance() {
        NavFoundFragment fragment = new NavFoundFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mPullLoadMoreRecyclerView = view.findViewById(R.id.pullLoadMoreRecyclerView);

        //mPullLoadMoreRecyclerView.setRefreshing(true);
//        mPullLoadMoreRecyclerView.setStaggeredGridLayout(2);
//        mRecyclerViewAdapter = new StaggeredRecycleViewAdapter(getActivity(), finds);
//        mRecyclerViewAdapter.setFinds(finds);
//        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
//        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener());
        uid = getContext().getSharedPreferences("db", Context.MODE_PRIVATE).getInt("uid", -1);


        pullData();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_navigation_we, container, false);
        title = mView.findViewById(R.id.we_title);
//        mView = inflater.inflate(R.layout.fragment_pla_content, null);
        mListView = (XMultiColumnListView) mView.findViewById(R.id.list);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);


        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(new XMultiColumnListView.IXListViewListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                if (isRunning == false) {
                    isRunning = true;
                    page = 0;
                    mType = 1;
                    pullData();
                    ++page;
                }
//                AddItemToContainer(++currentPage, 1);
            }

            @Override
            public void onLoadMore() {
                // TODO Auto-generated method stub

                if (isRunning == false) {
                    isRunning = true;
                    ++page;
                    mType = 2;
                    pullData();


                }

//                AddItemToContainer(++currentPage, 2);
            }
        });
        mAdapter = new StaggeredAdapter(getContext());
        mListView.setAdapter(mAdapter);
        return mView;
    }

    private List<Map<String, String>> setList() {
        List<Map<String, String>> dataList = new ArrayList<>();
        return dataList;

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println(mType);
            isRunning = false;
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mType == 1) {
                        mAdapter.addItemTop(finds);
                        mListView.stopRefresh();
                    } else {
                        if (finds.size() > 0)
                            mAdapter.addItemLast(finds);
                        mListView.stopLoadMore();
                    }
                    if (finds.size() > 0)
                        mAdapter.notifyDataSetChanged();
//                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    break;
            }
        }
    };


    public void pullData() {
        finds = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        OkHttp.post(getContext(), HttpContants.BASE_URL + "/Api/find/index?user_id=" + uid + "&cat_id=11&page=" + page, params, new OkCallback() {
            @Override
            public void onResponse(String response) {
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
                    }
//                    title.setText(finds.get(0).getTitle());
                    handler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String state, String msg) {
//                System.out.println(state+msg);
                if (mType == 1) {
                    mListView.stopRefresh();
                } else {
                    mListView.stopLoadMore();
                }
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

//    class PullLoadMoreListener implements PullLoadMoreRecyclerView.PullLoadMoreListener {
//        @Override
//        public void onRefresh() {
//            page = 1;
//            setRefresh();
//            pullData();
//        }
//
//        @Override
//        public void onLoadMore() {
//            page++;
//            pullData();
//        }
//    }
//
//    private void setRefresh() {
//        mRecyclerViewAdapter.getFinds().clear();
////        mCount = 1;
//
//    }


}
