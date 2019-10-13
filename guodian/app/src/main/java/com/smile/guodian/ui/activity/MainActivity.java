package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.smile.guodian.R;
import com.smile.guodian.model.entity.CategoryList;
import com.smile.guodian.ui.adapter.ViewPagerAdapter;
import com.smile.guodian.ui.fragment.NavCartFragment;
import com.smile.guodian.ui.fragment.NavCategoryFragment;
import com.smile.guodian.ui.fragment.NavHomeFragment;
import com.smile.guodian.ui.fragment.NavUserFragment;
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
    private int uid;

    private int type = -1;

    @BindView(R.id.bottom_tab_layout)
    TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    public void setCategoryList(int categoryId) {
//        this.categoryList = categoryList;
//        System.out.print(categoryId);
        ((NavCategoryFragment) viewPagerAdapter.getItem(2)).refreshData(categoryId);
    }

    public CategoryList getCategoryList() {
        return categoryList;
    }

    public CategoryList categoryList;

    private CountDownTimer timer;

    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static MainActivity getInstance() {
        return new MainActivity();
    }


    @Override
    protected void init() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);

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

//                type = tab.getPosition();/

                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("db", MODE_PRIVATE);
                uid = sharedPreferences.getInt("uid", -1);

//                System.out.println(uid + "---" + tab.getPosition());
                if (tab.getPosition() == 3 && viewPagerAdapter != null) {
                    NavCartFragment fragment = (NavCartFragment) viewPagerAdapter.getFragments().get(3);
                    fragment.reload();
                }

                if (uid >= 0 && tab.getPosition() == 4) {
                    NavUserFragment fragment = (NavUserFragment) viewPagerAdapter.getFragments().get(4);
                    fragment.updateData();
                }
//                tabLayout.getTabAt(type).select();
                if (uid <= 0 && (tab.getPosition() == 4 || tab.getPosition() == 3)) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("where", "main");
                    startActivity(intent);
//                    return;
                }
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
                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("db", MODE_PRIVATE);
                uid = sharedPreferences.getInt("uid", -1);

//                System.out.println(uid + "---" + tab.getPosition());

                if (tab.getPosition() == 3 && viewPagerAdapter != null) {
                    NavCartFragment fragment = (NavCartFragment) viewPagerAdapter.getFragments().get(3);
                    fragment.reload();
//                    tabLayout.getTabAt(3).select();
                }

                if (uid <= 0 && (tab.getPosition() == 4 || tab.getPosition() == 3)) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("where", "main");
                    startActivity(intent);
                    return;
                }

            }
        });
        for (int i = 0; i < 5; i++) {
            tabLayout.addTab(tabLayout.newTab().setCustomView(DataGenerator.getTabView(this, i)));
        }

        viewPager.setOffscreenPageLimit(5);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        if (type != -1) {
            viewPager.setCurrentItem(type);
            tabLayout.getTabAt(type).select();
        }

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

/*    public void switchPosition(int position, CategoryList categoryList) {
        viewPager.setCurrentItem(position);
        this.categoryList = categoryList;
    }*/

    FragmentSkipInterface mFragmentSkipInterface;

    public void setFragmentSkipInterface(FragmentSkipInterface fragmentSkipInterface) {
        mFragmentSkipInterface = fragmentSkipInterface;
    }

    /**
     * Fragment跳转
     */
    public void skipToFragment(int position) {
        if (mFragmentSkipInterface != null) {
            mFragmentSkipInterface.gotoFragment(viewPager);
        }
        tabLayout.getTabAt(position).select();
    }

    public interface FragmentSkipInterface {
        /**
         * ViewPager中子Fragment之间跳转的实现方法
         */
        void gotoFragment(AHBottomNavigationViewPager viewPager);
    }
}
