package com.smile.guodian.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Hot;
import com.smile.guodian.utils.GlideUtil;

import java.util.List;

public class HeadlineShowAdapter extends BaseAdapter {
    private Context context;
    private List<Hot> hots;
    private Activity activity;

    public void setHots(List<Hot> hots) {
        this.hots = hots;
    }

    public HeadlineShowAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return hots.size();
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
        convertView = View.inflate(context, R.layout.item_headline_show, null);
        TextView title = convertView.findViewById(R.id.item_headline_show_title);
        ImageView imageView = convertView.findViewById(R.id.item_headline_show_content);
        title.setText(hots.get(position).getAd_name());
//        System.out.println(HttpContants.BASE_URL + hots.get(position).getAd_code());
        GlideUtil.load(context, activity, HttpContants.BASE_URL + hots.get(position).getAd_code(), imageView);
//        Glide.with(context).load(HttpContants.BASE_URL+hots.get(position).getAd_code()).into(imageView);
        return convertView;
    }
}
