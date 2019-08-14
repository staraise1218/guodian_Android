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
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Brand;
import com.smile.guodian.model.entity.Category;
import com.smile.guodian.model.entity.CategoryBean;
import com.smile.guodian.model.entity.CategoryBean2;
import com.smile.guodian.model.entity.SortModel;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.activity.CategoryProductActivity;
import com.smile.guodian.ui.activity.MainActivity;
import com.smile.guodian.ui.activity.SearchActivity;
import com.smile.guodian.ui.activity.WebActivity;
import com.smile.guodian.ui.activity.me.MessageActivity;
import com.smile.guodian.ui.activity.message.MessageCenterActivity;
import com.smile.guodian.ui.adapter.CategoryAdapter;
import com.smile.guodian.ui.adapter.SecondAdapter;
import com.smile.guodian.ui.adapter.SecondGoodsAdapter;
import com.smile.guodian.ui.adapter.category.SortAdapter;
import com.smile.guodian.utils.CharacterParser;
import com.smile.guodian.utils.PinyinComparator;
import com.smile.guodian.widget.ClearEditText;
import com.smile.guodian.widget.SideBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class NavCategoryFragment extends BaseFragment {


    @OnClick({R.id.category_notify, R.id.category_search})
    public void clickView(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.category_notify:
                intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("type", 20);
                intent.putExtra("url", HttpContants.BASE_URL + "/page/message.html");
//                Intent intent = new Intent(getContext(), MessageCenterActivity.class);
                startActivity(intent);
//                intent = new Intent(getContext(), MessageCenterActivity.class);
//                startActivity(intent);
                break;
            case R.id.category_search:
                intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                break;
        }
    }


    public NavCategoryFragment() {
    }

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
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> sourceDataList;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @BindView(R.id.recyclerview_category)
    RecyclerView mRecyclerView;
    @BindView(R.id.gridview_wares)
    GridView mGridViewWares;

    //    @BindView(R.id.recyclerview_wares)
//    RecyclerView mRecyclerviewWares;
//    @BindView(R.id.refresh_layout)
//    MaterialRefreshLayout mRefreshLaout;
//    @BindView(R.id.catergory_web)
//    WebView webView;
    @BindView(R.id.catergory_content)
    RelativeLayout content;
    @BindView(R.id.catergory_banner)
    ImageView banner;

    @BindView(R.id.sidrbar)
    SideBar sideBar;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.name_listview)
    ListView sortListView;

    @BindView(R.id.category_second)
    LinearLayout second;
    @BindView(R.id.sort)
    FrameLayout sort;


    private Gson mGson = new Gson();
    private List<Category> categoryFirst = new ArrayList<>();      //一级菜单
    private CategoryAdapter mCategoryAdapter;                      //一级菜单
    private SecondGoodsAdapter mSecondGoodsAdapter;              //二级菜单
    private SecondAdapter mSecondAdapter;//二级菜单
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
    List<Brand> brands = new ArrayList<>();
    private int categoryId;

    @Override
    protected int getContentResourseId() {
        return R.layout.fragment_category;
    }


    @Override
    protected void init() {
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
//        MainActivity mainActivity = (MainActivity) getActivity();
//        if (mainActivity.getCategoryList() != null) {
//            categoryId = mainActivity.getCategoryList().getId();
//            mainActivity.setCategoryList(null);
//        }
        mVFMessagesList = new ArrayList<>();
        requestCategoryData();
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);

        sideBar.setTextView(dialog);
        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                if (adapter == null) {
                    return;
                }
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        //单击名称列表后toast提示

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Intent intent = new Intent(getContext(), CategoryProductActivity.class);
                intent.putExtra("branch_id", brands.get(position).getId() + "");
                intent.putExtra("name", brands.get(position).getName());
                getContext().startActivity(intent);
            }
        });


    }

    public void refreshData(int categoryId) {
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
//        System.out.println("resume");
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        System.out.println(hidden);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        System.out.println("created");
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
                        Category category = new Category();
                        category.setCat_name("品牌");
                        categoryFirst.add(category);
                        while (iterator.hasNext()) {
                            Category bean = iterator.next();
                            position++;
                            if ((categoryId + "").equals(bean.getId())) {
                                currentPosition = position;
                            }
                            categoryFirst.add(bean);
                        }
//                        System.out.println(currentPosition);
                        if (currentPosition != 0) {
                            showCategoryData(currentPosition);
                            second.setVisibility(View.VISIBLE);
                            sort.setVisibility(View.GONE);
                        } else {
                            second.setVisibility(View.GONE);
                            sort.setVisibility(View.VISIBLE);
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
                mCategoryAdapter.setPosition(position);
                Category category = (Category) adapter.getData().get(position);
                String id = category.getId();
                String name = category.getCat_name();
                mCategoryAdapter.notifyDataSetChanged();
                isclick = true;
                if (position == 0) {
                    second.setVisibility(View.GONE);
                    sort.setVisibility(View.VISIBLE);
                    defaultClick();
                } else {
                    second.setVisibility(View.VISIBLE);
                    sort.setVisibility(View.GONE);
                    requestWares(id);
                }
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
            Category category = categoryFirst.get(1);
            String id = category.getId();
            requestWares(id);
            getBanchID();
        }
//        sourceDataList = filledData();
////         根据a-z进行排序源数据
//        Collections.sort(sourceDataList, pinyinComparator);
//        adapter = new SortAdapter(getContext(), sourceDataList);
//        sortListView.setAdapter(adapter);

    }

    public void getBanchID() {
        brands.clear();
        sourceDataList = new ArrayList<>();

        OkHttpUtils.post().url(HttpContants.BASE_URL + "/Api/category/allBrandList").build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        LogUtil.e("分类一级", e + "", true);true
                        System.out.println(e.getMessage() + "");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println(response + "");

                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);

                            JSONObject data = object.getJSONObject("data");
                            Iterator<String> iterator = data.keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();

                                JSONArray array = data.getJSONArray(key);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject branObject = array.getJSONObject(i);
                                    Brand brand1 = new Brand();
                                    brand1.setCat_name(branObject.getString("cat_name"));
                                    brand1.setId(branObject.getInt("id"));
                                    brand1.setInitials(branObject.getString("initials"));
                                    brand1.setName(branObject.getString("name"));
                                    brands.add(brand1);
                                    SortModel sortModel = new SortModel();
                                    sortModel.setName(brand1.getName() + "  " + brand1.getCat_name());
                                    sortModel.setSortLetters(((JSONObject) branObject).getString("initials"));
                                    sourceDataList.add(sortModel);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Collections.sort(sourceDataList, pinyinComparator);
                        adapter = new SortAdapter(getContext(), sourceDataList);
                        sortListView.setAdapter(adapter);
                    }
                });

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
                CategoryBean2 hotGoods = mGson.fromJson(response, CategoryBean2.class);
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

                mSecondAdapter = new SecondAdapter(getActivity(), datas);
                mGridViewWares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), CategoryProductActivity.class);
                        intent.putExtra("branch_id", datas.get(position).getId());
                        intent.putExtra("name", datas.get(position).getName());
                        getContext().startActivity(intent);
                    }
                });
                mGridViewWares.setAdapter(mSecondAdapter);
//                mRecyclerviewWares.setAdapter(mSecondGoodsAdapter);
//                mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 3));
//                DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),
//                        DividerItemDecoration.VERTICAL);
//                itemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(mContext, R.color.white)));
//                mRecyclerviewWares.addItemDecoration(itemDecoration);
                break;

        }
    }


    /**
     * 为ListView填充数据
     *
     * @param data
     * @return
     */
    private List<SortModel> filledData(String[] data) {

        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < data.length; i++) {

            SortModel sortModel = new SortModel();
            sortModel.setName(data[i]);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(data[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }
}
