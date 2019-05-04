package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Hot;

import java.util.List;

public class ItemRecommendAdapter extends BaseAdapter {

    public ItemRecommendAdapter(Context context) {
        this.context = context;
    }

    public List<Hot> getCustom() {
        return custom;
    }

    public void setCustom(List<Hot> custom) {
        this.custom = custom;
    }

    private List<Hot> custom;

    private Context context;

    @Override
    public int getCount() {
        return custom.size();
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
        convertView = View.inflate(context,R.layout.item_recommend,null);
        ImageView imageView = convertView.findViewById(R.id.item_recommend_content);

        Glide.with(context).load(HttpContants.BASE_URL+custom.get(position).getAd_code()).into(imageView);
        return convertView;
    }
}
