package com.smile.guodian.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smile.guodian.R;

import java.util.List;


public class ProductGenerator {

    public static final int[] mTabRes = new int[]{R.color.white, R.color.white, R.color.white, R.color.white, R.color.white};
    public static final int[] mTabResPressed = new int[]{R.color.font_orange_color, R.color.font_orange_color, R.color.font_orange_color, R.color.font_orange_color, R.color.font_orange_color};
    public static List<String> mTabTitle;

    public static List<String> getmTabTitle() {
        return mTabTitle;
    }

    public static void setmTabTitle(List<String> mTabTitle) {
        ProductGenerator.mTabTitle = mTabTitle;
    }

    //    = new String[]{"首页","发现","分类","购物车","我的"};


    public static View getTabView(Context context, String name, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_tab_content, null);
        View tabIcon = view.findViewById(R.id.tab_content_image);
        tabIcon.setBackgroundResource(ProductGenerator.mTabRes[0]);
//        tabIcon.setImageResource(ProductGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(name);
        return view;
    }
}
