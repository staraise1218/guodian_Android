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

public class HomeLiveAdapter extends BaseAdapter {

    private Context context;

    public void setBannars(List<Hot> bannars) {
        this.bannars = bannars;
    }

    private List<Hot> bannars;

    public HomeLiveAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return bannars.size();
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
        convertView = View.inflate(context, R.layout.item_home_type_live_content,null);
        ImageView imageView = convertView.findViewById(R.id.live_content);
        Glide.with(context).load(HttpContants.BASE_URL+bannars.get(position).getAd_code()).into(imageView);
        return convertView;
    }
}
