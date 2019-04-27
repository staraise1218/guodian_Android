package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.CategoryList;

import java.util.List;

public class CategoryListAdapter extends BaseAdapter {

    public void setCategoryLists(List<CategoryList> categoryLists) {
        this.categoryLists = categoryLists;
    }

    private List<CategoryList> categoryLists;
    private Context context;


    public CategoryListAdapter(List<CategoryList> categoryLists, Context context) {
        this.categoryLists = categoryLists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categoryLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context,R.layout.item_home_type_categorylsit_content,null);
        ImageView imageView = convertView.findViewById(R.id.catergorylist_content);
        Glide.with(context).load(HttpContants.BASE_URL+categoryLists.get(position).getImage()).into(imageView);
        return convertView;
    }
}
