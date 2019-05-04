package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.smile.guodian.model.entity.Category;

import java.util.List;

public class ProductTitleAdapter extends BaseAdapter {

    Context context;
    List<Category> categories;

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public ProductTitleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return categories.size();
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
        return null;
    }
}
