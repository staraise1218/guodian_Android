package com.smile.guodian.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.base.BundleKey;
import com.smile.guodian.model.entity.HomeBase;
import com.smile.guodian.model.entity.HomeTop;
import com.smile.guodian.ui.adapter.GoodsDetailPagerAdapter;
import com.smile.guodian.ui.adapter.ImageHomeAdapter;
import com.smile.guodian.utils.StatusBarUtil;
import com.smile.guodian.widget.CirclePageIndicator;
import com.smile.guodian.widget.GradientScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.status_bar)
    View statusBar;
    @BindView(R.id.scroll_view)
    GradientScrollView scrollView;
    @BindView(R.id.auto_view_pager)
    AutoScrollViewPager autoViewPager;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private Activity activity;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        activity = this;
        context = getApplicationContext();

        initView();
    }

    private void initView() {
        toolbar.setTitle("商品详情");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar.setTitleTextColor(Color.argb(0, 255, 255, 255));
        toolbar.getBackground().mutate().setAlpha(0);
        statusBar.getBackground().mutate().setAlpha(0);
        scrollView.setHeader(toolbar, statusBar);
        StatusBarUtil.setTransparentForImageView(this, tvName);

        HomeBase bean = getIntent().getParcelableExtra(BundleKey.PARCELABLE);

        tvName.setText(bean.getName());
        tvPrice.setText("¥" + bean.getPrice());
        List<HomeTop.Carousel> list = new ArrayList<>();
//        list.add(new HomeTop.Carousel(1, "https://img.alicdn.com/bao/uploaded/i1/TB1sDqKLXXXXXanXXXXXXXXXXXX_!!0-item_pic.jpg_800x800.jpg"));
//        list.add(new HomeTop.Carousel(2, "https://img.alicdn.com/bao/uploaded/i2/2171322350/TB2SqaTaNaJ.eBjSsziXXaJ_XXa_!!2171322350.jpg_800x800.jpg"));
//        list.add(new HomeTop.Carousel(3, "https://img.alicdn.com/bao/uploaded/i4/TB1Ke9CJpXXXXa9XpXXXXXXXXXX_!!0-item_pic.jpg_800x800.jpg"));
//        list.add(new HomeTop.Carousel(4, "https://asearch.alicdn.com/bao/uploaded/i2/159530341092325317/TB2mMMbaHBnpuFjSZFGXXX51pXa_!!0-saturn_solar.jpg_800x800.jpg"));
//        list.add(new HomeTop.Carousel(5, "https://img.alicdn.com/bao/uploaded/i3/20821802/TB2S7E9abVkpuFjSspcXXbSMVXa_!!20821802.jpg_800x800.jpg"));

        autoViewPager.setAdapter(new ImageHomeAdapter(context, activity, list));
        indicator.setViewPager(autoViewPager);
        autoViewPager.setInterval(4000);
        autoViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);

        GoodsDetailPagerAdapter adapter = new GoodsDetailPagerAdapter(getSupportFragmentManager(),
                new String[]{"商品描述", "评论(20)"});
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
