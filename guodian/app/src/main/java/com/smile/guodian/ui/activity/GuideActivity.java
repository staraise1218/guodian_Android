package com.smile.guodian.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.trinea.android.common.util.PreferencesUtils;


public class GuideActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.guide)
    ImageView imageView;

    private List<String> imgUrl = new ArrayList<>();

    //获取图片资源
    int[] imgRes = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private List<View> mViewList = new ArrayList<>();


    @Override
    protected int getContentResourseId() {

        //必须写在这里,不能写在 init 中.先全屏,再加载试图
//        requestWindowFeature(Window.FEATURE_NO_TITLE);       // 无标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
//                .LayoutParams.FLAG_FULLSCREEN);    //全屏

        return R.layout.activity_guide;
    }


    /**
     * 必须重写base中的setStatusBar方法.要不然用继承父类的沉浸式状态栏
     */
    @Override
    protected void setStatusBar() {
        //里面什么东西都没有
    }


    @Override
    protected void init() {
        boolean isFirst = PreferencesUtils.getBoolean(GuideActivity.this, "isFirst", true);
        //默认为第一次

        if (isFirst) {
            PreferencesUtils.putBoolean(GuideActivity.this, "isFirst", false);
            MyPagerAdapter adapter = new MyPagerAdapter();
            imageView.setVisibility(View.GONE);
            mViewPager.setAdapter(adapter);
            mViewPager.setOffscreenPageLimit(0);
            mViewPager.setOnPageChangeListener(new PagePositionLister());
            initData();
        } else {
//            SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putInt("uid", -1);
//            editor.commit();
            startActivity(new Intent(GuideActivity.this, MainActivity.class));
        }
//        for(int i=0;i<3;i++) {
//            View inflate = getLayoutInflater().inflate(R.layout.guide_item, null);
//            ImageView ivGuide = (ImageView) inflate.findViewById(R.id.iv_guide);
//            ivGuide.setBackgroundResource(imgRes[i]);
////        Glide.with(GuideActivity.this).load("http://guodian.staraise.com.cn" + array.getJSONObject(i).getString("ad_code")).into(ivGuide);
////                       GlideUtil.load(GuideActivity.this,GuideActivity.this,"http://guodian.staraise.com.cn"+array.getJSONObject(i).getString("ad_code"),ivGuide);
//            mViewList.add(inflate);
//        }

    }


    /**
     * 初始化数据
     */
    private void initData() {


        OkHttp.post(this, HttpContants.BASE_URL + "/Api/index/startBanner", null, new OkCallback() {
            @Override
            public void onResponse(String response) {
//                System.out.println(response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        imgUrl.add(array.getJSONObject(i).getString("ad_code"));
//                       System.out.println(array.getJSONObject(i).getString("ad_code"));
                        View inflate = getLayoutInflater().inflate(R.layout.guide_item, null);
                        ImageView ivGuide = (ImageView) inflate.findViewById(R.id.iv_guide);
                        Glide.with(GuideActivity.this).load(HttpContants.BASE_URL + array.getJSONObject(i).getString("ad_code")).into(ivGuide);
//                       GlideUtil.load(GuideActivity.this,GuideActivity.this,"http://guodian.staraise.com.cn"+array.getJSONObject(i).getString("ad_code"),ivGuide);
                        mViewList.add(inflate);
                    }

                    MyPagerAdapter adapter = new MyPagerAdapter();
                    imageView.setVisibility(View.GONE);
                    mViewPager.setAdapter(adapter);
                    mViewPager.setOffscreenPageLimit(0);
                    mViewPager.setOnPageChangeListener(new PagePositionLister());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String state, String msg) {
                System.out.println(msg);
            }
        });


//        for (int i = 0; i < imgRes.length; i++) {
//            View inflate = getLayoutInflater().inflate(R.layout.guide_item, null);
//            ImageView ivGuide = (ImageView) inflate.findViewById(R.id.iv_guide);
//            GlideUtil.load(this,GuideActivity.this,"http://guodian.staraise.com.cn"+imgUrl.get(i),ivGuide);
//
////            ivGuide.setBackgroundResource(imgRes[i]);
//            mViewList.add(inflate);
//        }
    }

    @OnClick(R.id.btn_start)
    public void jumpActivity(View view) {
//        SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("uid", -1);
//        editor.commit();
        startActivity(new Intent(GuideActivity.this, MainActivity.class));
        finish();
    }


    /**
     * viewPager适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViewList == null ? 0 : mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }

    private class PagePositionLister implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //如果滑动到最后一张,显示按钮
            if (position == mViewList.size() - 1) {
                mBtnStart.setVisibility(View.VISIBLE);
            } else {
                mBtnStart.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
