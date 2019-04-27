package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.GuessGoods;

import java.util.List;

public class HomeHeaderAdapter extends BaseAdapter {
    private Context context;

    private List<GuessGoods> guessGoods;

    public void setGuessGoods(List<GuessGoods> guessGoods) {
        this.guessGoods = guessGoods;
    }

    public HomeHeaderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return guessGoods.size();
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
        convertView = View.inflate(context, R.layout.item_header_show,null);
        ImageView imageView = convertView.findViewById(R.id.item_headline_show_content);
//        System.out.println(guessGoods.get(position).getOriginal_img());
        Glide.with(context).load(HttpContants.BASE_URL+guessGoods.get(position).getOriginal_img()).into(imageView);
        return convertView;
    }
}
