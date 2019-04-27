package com.smile.guodian.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smile.guodian.R;


public class DataGenerator {

    public static final int []mTabRes = new int[]{R.drawable.home_page,R.drawable.found_no,R.drawable.classify,R.drawable.shopping_cart,R.drawable.my};
    public static final int []mTabResPressed = new int[]{R.drawable.chosse,R.drawable.found_chosse,R.drawable.classify_chose,R.drawable.choose_no,R.drawable.my_choose};
    public static final String []mTabTitle = new String[]{"首页","发现","分类","购物车","我的"};


    /**
     * 获取Tab 显示的内容
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context,int position){
        View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }
}