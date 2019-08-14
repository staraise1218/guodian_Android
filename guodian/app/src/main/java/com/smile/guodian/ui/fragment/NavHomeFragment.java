package com.smile.guodian.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smile.guodian.R;
import com.smile.guodian.base.BundleKey;
import com.smile.guodian.base.Type;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.HomeBase;
import com.smile.guodian.model.entity.HomeBottom;
import com.smile.guodian.model.entity.HomeTop;
import com.smile.guodian.presenter.HomePresenter;
import com.smile.guodian.ui.activity.CategoryProductActivity;
import com.smile.guodian.ui.activity.DetailActivity;
import com.smile.guodian.ui.activity.MainActivity;
import com.smile.guodian.ui.activity.SearchActivity;
import com.smile.guodian.ui.activity.WebActivity;
import com.smile.guodian.ui.activity.message.MessageCenterActivity;
import com.smile.guodian.ui.adapter.HomeAdapter;
import com.smile.guodian.contract.HomeContract;
import com.smile.guodian.utils.ToastUtil;
import com.smile.guodian.widget.LoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NavHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        LoadMoreRecyclerView.OnLoadMoreListener, HomeContract.View, HomeAdapter.OnItemClickListener {
    private final static int HOME_TOP = 1;
    private final static int HOME_BOTTOM = 2;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    LoadMoreRecyclerView recyclerView;
    @BindView(R.id.home_search)
    EditText search;
    @BindView(R.id.home_notify)
    ImageView notify;

    @OnClick(R.id.home_notify)
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.home_notify:
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("type", 20);
                intent.putExtra("url", HttpContants.BASE_URL + "/page/message.html");
//                Intent intent = new Intent(getContext(), MessageCenterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private Context context;
    private MainActivity activity;

    private List<HomeBase> list = new ArrayList<>();

    private HomeAdapter adapter;
    private HomePresenter presenter;
    private int page = 1;
    private int pageSize = 10;
    private HomeBase footerItem = new HomeBase();


    public static NavHomeFragment newInstance() {
        NavHomeFragment fragment = new NavHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_home, container, false);
        ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        context = activity.getApplicationContext();
        presenter = new HomePresenter();
        presenter.init(this, context);
        return view;
    }

    @Override
    public void initView() {

        refreshLayout.setColorSchemeResources(R.color.font_orange_color);
        refreshLayout.setOnRefreshListener(this);
        int spanCount = getResources().getInteger(R.integer.grid_span_count);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeAdapter(context, activity, list);
        adapter.setOnItemClickListener(this);
//        layoutManager.setSpanSizeLookup(adapter.getSpanSizeLookup());
        recyclerView.setAdapter(adapter);
        recyclerView.setOnLoadMoreListener(this);

        footerItem.setType(Type.TYPE_FOOTER_LOAD);
        footerItem.setSpanCount(spanCount);
        presenter.start(HOME_TOP);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        setRefreshLoading(false);
//        presenter.start(HOME_TOP);
        page = 1;
    }

    @Override
    public void onLoadMore() {
        setRefreshLoading(true);
        page++;
        presenter.start(HOME_BOTTOM, page, pageSize);
    }

    //轮播、分类、头条、直播回调
    @Override
    public void show(HomeTop bean) {
        list.clear();
        adapter.setLoopList(bean.getCarousel());
        adapter.setHeadlineList(bean.getHeadlines());
        adapter.setHotList(bean.getHotlist());
        adapter.setRegabout(bean.getReg_about());
        adapter.setGuessGoods(bean.getGuessGoodsList());
        adapter.setCustomer(bean.getCustom_goods());
        adapter.setCategoryLists(bean.getCategoryLists());

        list.addAll(bean.getList());
        list.add(footerItem);
        adapter.notifyDataSetChanged();

    }


    //猜你喜欢模块回调
    @Override
    public void show(HomeBottom bean) {
        recyclerView.setPage(page, bean.getTotalPage());
        footerItem.setType(page < bean.getTotalPage() ? Type.TYPE_FOOTER_LOAD : Type.TYPE_FOOTER_FULL);
        list.addAll(list.size() - 1, bean.getData());
        adapter.notifyDataSetChanged();
        setRefreshLoading(false);
    }

    @Override
    public void loading() {

    }

    @Override
    public void networkError() {
        setRefreshLoading(false);
        ToastUtil.showShortToast(context, R.string.toast_network_error);

    }

    @Override
    public void error(String msg) {
        setRefreshLoading(false);
        ToastUtil.showShortToast(context, msg);
    }

    @Override
    public void showFailed(String msg) {
        setRefreshLoading(false);
        ToastUtil.showShortToast(context, msg);
    }

    /**
     * 设置刷新和加载更多的状态
     *
     * @param isLoading 加载更多状态
     */
    private void setRefreshLoading(boolean isLoading) {
        if (!isLoading) {
            recyclerView.setLoading(false);
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), DetailActivity.class);
        intent.putExtra(BundleKey.PARCELABLE, list.get(position));
        startActivity(intent);
    }
}
