package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.smile.guodian.R;

public class MyBrowSerAdapter extends BaseAdapter {
    private Context context;

    public MyBrowSerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class Viewholder{
        public CheckBox checkBox;
        public ImageView imageView;
        public TextView name;
        public TextView tip;
        public TextView price;
        public TextView cs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_browser,null);
        return convertView;
    }
}
