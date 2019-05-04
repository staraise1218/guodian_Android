package com.smile.guodian.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cjj.MaterialRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Category;
import com.smile.guodian.model.entity.CategoryBean;
import com.smile.guodian.model.entity.CategoryBean2;
import com.smile.guodian.ui.activity.CategoryProductActivity;
import com.smile.guodian.ui.activity.MainActivity;
import com.smile.guodian.ui.adapter.CategoryAdapter;
import com.smile.guodian.ui.adapter.SecondGoodsAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class NavCategoryFragment extends BaseFragment {

    public static NavCategoryFragment newInstance() {
        NavCategoryFragment fragment = new NavCategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;       //正常情况

    @BindView(R.id.recyclerview_category)
    RecyclerView mRecyclerView;
    @BindView(R.id.recyclerview_wares)
    RecyclerView mRecyclerviewWares;
    @BindView(R.id.refresh_layout)
    MaterialRefreshLayout mRefreshLaout;
    @BindView(R.id.tv_city)
    TextView mCityName;
    @BindView(R.id.tv_day_weather)
    TextView mDayWeather;
    @BindView(R.id.tv_night_weather)
    TextView mNightWeather;

    @BindView(R.id.catergory_web)
    WebView webView;
    @BindView(R.id.catergory_content)
    RelativeLayout content;
    @BindView(R.id.catergory_banner)
    ImageView banner;


    private Gson mGson = new Gson();
    private List<Category> categoryFirst = new ArrayList<>();      //一级菜单
    private CategoryAdapter mCategoryAdapter;                      //一级菜单
    private SecondGoodsAdapter mSecondGoodsAdapter;              //二级菜单
    private List<Category> datas;
    private List<String> mVFMessagesList;                 //上下轮播的信息

    private String provinceName;                                  //省份
    private String cityName;                                      //城市名
    private String dayWeather;
    private String nightWeather;
    private int current = 0;
//    private LocationService locationService;

    private int currPage = 1;     //当前是第几页
    private int totalPage = 1;    //一共有多少页
    private int pageSize = 10;     //每页数目

    private int categoryId;

    @Override
    protected int getContentResourseId() {
        return R.layout.fragment_category;
    }


    @Override
    protected void init() {

//        MainActivity mainActivity = (MainActivity) getActivity();
//        if (mainActivity.getCategoryList() != null) {
//            categoryId = mainActivity.getCategoryList().getId();
//            mainActivity.setCategoryList(null);
//        }
        mVFMessagesList = new ArrayList<>();
        requestCategoryData();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    public void refreshData(int categoryId){
//        MainActivity mainActivity = (MainActivity) getActivity();
//        if (mainActivity.getCategoryList() != null) {
//            categoryId = mainActivity.getCategoryList().getId();
//            mainActivity.setCategoryList(null);
//        }
//        System.out.println(categoryId);
        this.categoryId = categoryId;
        requestCategoryData();
    }

    public void setStateRefreh() {
//        MainActivity mainActivity = (MainActivity) getActivity();
//        if (mainActivity.getCategoryList() != null) {
//            categoryId = mainActivity.getCategoryList().getId();
//            mainActivity.setCategoryList(null);
//        }
//        System.out.println(categoryId);
//        requestCategoryData();

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("resume");
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println(hidden);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("created");
    }

    private void requestCategoryData() {
        categoryFirst.clear();

        OkHttpUtils.post().url(HttpContants.CATEGORY_LIST).build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        LogUtil.e("分类一级", e + "", true);true
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response + "");
                        int position = 0;
                        int currentPosition = 0;
                        Type collectionType = new TypeToken<CategoryBean>() {
                        }.getType();
                        CategoryBean enums = mGson.fromJson(response, collectionType);
                        Iterator<Category> iterator = enums.getData().iterator();
                        while (iterator.hasNext()) {
                            Category bean = iterator.next();
                            position++;
                            if ((categoryId + "" ).equals( bean.getId())) {
                                currentPosition = position;
                            }
                            categoryFirst.add(bean);
                        }
                        System.out.println(currentPosition);
                        if(currentPosition!=0) {
                            showCategoryData(currentPosition - 1);
                        }else {
                            showCategoryData(0);
                        }

                        if (categoryId == 0)
                            defaultClick();
                        else {
                            NavCategoryFragment.this.requestWares(categoryId + "");
                        }

                    }
                });

    }


    /**
     * 展示一级菜单数据
     */
    private boolean isclick = false;

    private void showCategoryData(int position) {
//        System.out.println(position);
        mCategoryAdapter = new CategoryAdapter(categoryFirst);

        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Category category = (Category) adapter.getData().get(position);
                String id = category.getId();
                String name = category.getCat_name();
                mCategoryAdapter.setPosition(position);
                mCategoryAdapter.notifyDataSetChanged();
                isclick = true;
                defaultClick();
                requestWares(id);
            }
        });


        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(mContext, R.color.white)));
        mRecyclerView.addItemDecoration(itemDecoration);
        mCategoryAdapter.setPosition(position);
        mCategoryAdapter.notifyDataSetChanged();


    }


    private void defaultClick() {

        //默认选中第0个
        if (!isclick && categoryId == 0) {
            Category category = categoryFirst.get(0);
            String id = category.getId();
            requestWares(id);
        }
    }


    /**
     * 二级菜单数据
     *
     * @param firstCategorId 一级菜单的firstCategorId
     */
    private void requestWares(String firstCategorId) {
//        if (datas != null)
//            datas.clear();

        String url = HttpContants.WARES_LIST;

        OkHttpUtils.post().addParams("cat_id", firstCategorId + "").url(url).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
//                LogUtil.e("二级菜单", e + "", true);
            }

            @Override
            public void onResponse(String response, int id) {
//                LogUtil.e("二级菜单", response + "", true);


                CategoryBean2 hotGoods = mGson.fromJson(response, CategoryBean2.class);
//                totalPage = hotGoods.getTotalPage();
//                currPage = hotGoods.getCurrentPage();
                datas = hotGoods.getData().getList();

                Glide.with(getContext()).load(HttpContants.BASE_URL + hotGoods.getData().getCategory().getImage2()).into(banner);

                showData();

            }
        });
    }


    /**
     * 展示二级菜单的数据
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:

                mSecondGoodsAdapter = new SecondGoodsAdapter(datas, getActivity());
                mSecondGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter
                        .OnItemClickListener() {

                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                        Intent intent = new Intent(getContext(), CategoryProductActivity.class);
                        intent.putExtra("branch_id", datas.get(position).getId());
                        intent.putExtra("name", datas.get(position).getName());
                        getContext().startActivity(intent);

//                        Category category = (Category) adapter.getData().get
//                                (position);
//                        content.setVisibility(View.GONE);
//                        webView.setVisibility(View.VISIBLE);
//                        webView.loadUrl("http://guodian.staraise.com.cn/page/commodity.html?goods_id="+category.getId());

                    }
                });


                mRecyclerviewWares.setAdapter(mSecondGoodsAdapter);
                mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 3));
                DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),
                        DividerItemDecoration.VERTICAL);
                itemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(mContext, R.color.white)));
                mRecyclerviewWares.addItemDecoration(itemDecoration);
                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
