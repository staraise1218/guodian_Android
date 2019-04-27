package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.smile.guodian.R;
import com.smile.guodian.ui.adapter.ViewPagerAdapter;
import com.smile.guodian.widget.DataGenerator;

import java.util.ArrayList;

import butterknife.BindView;


public class MainActivity extends BaseActivity {

    //    @BindView(R.id.statusBarView)
//    View statusBar;
    @BindView(R.id.view_pager)
    AHBottomNavigationViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_search_tips)
    TextView tvSearchTips;


    @BindView(R.id.bottom_tab_layout)
    TabLayout tabLayout;


    private CountDownTimer timer;

    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void init() {
        initView();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void initView() {
        setSupportActionBar(toolbar);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                System.out.println(tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition(), false);
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    View view = tabLayout.getTabAt(i).getCustomView();
                    ImageView icon = (ImageView) view.findViewById(R.id.tab_content_image);
                    TextView text = (TextView) view.findViewById(R.id.tab_content_text);
                    if (i == tab.getPosition()) {
                        icon.setImageResource(DataGenerator.mTabResPressed[i]);
                        text.setTextColor(Color.parseColor("#DDA021"));
                    } else {
                        icon.setImageResource(DataGenerator.mTabRes[i]);
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        for (int i = 0; i < 5; i++) {
            tabLayout.addTab(tabLayout.newTab().setCustomView(DataGenerator.getTabView(this, i)));
        }

//        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.bottom_navigation_item_home, R.drawable.home, R.color.colorBottomNavigationActiveColored);
//        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.bottom_navigation_item_we, R.drawable.found, R.color.colorBottomNavigationActiveColored);
//        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.bottom_navigation_item_help, R.drawable.classify, R.color.colorBottomNavigationActiveColored);
//        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.bottom_navigation_item_cart, R.drawable.shop, R.color.colorBottomNavigationActiveColored);
//        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.bottom_navigation_item_user, R.drawable.me, R.color.colorBottomNavigationActiveColored);

//        bottomNavigationItems.add(item1);
//        bottomNavigationItems.add(item2);
//        bottomNavigationItems.add(item3);
//        bottomNavigationItems.add(item4);
//        bottomNavigationItems.add(item5);
//
//        bottomNavigation.addItems(bottomNavigationItems);
//        bottomNavigation.setForceTitlesDisplay(true);
//        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
//        bottomNavigation.setAccentColor(Color.parseColor("#DDA021"));
//        bottomNavigation.setInactiveColor(Color.parseColor("#949494"));
//
//        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
//            @Override
//            public boolean onTabSelected(int position, boolean wasSelected) {
//                viewPager.setCurrentItem(position, false);
//                return true;
//            }
//        });
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        /**
         * 顶部搜索框内容定时更新，简单的采用倒计时功能
         */
//        final List<String> tips = new ArrayList<>();
//        tips.add("AndroidNexus");
//        tips.add("EasyToForget");
//        tips.add("zhiye.wei@gmail.com");

//        timer = new CountDownTimer(3000000, 5000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                tvSearchTips.setText(tips.get(new Random().nextInt(2)));
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        };
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        timer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        timer.cancel();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}
