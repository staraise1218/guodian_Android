package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        if (guessGoods == null)
            return 0;
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
        convertView = View.inflate(context, R.layout.item_header_show, null);
        ImageView imageView = convertView.findViewById(R.id.item_headline_show_content);
        TextView name = convertView.findViewById(R.id.header_show_name);
        TextView price = convertView.findViewById(R.id.header_show_price);
        TextView normalPrice = convertView.findViewById(R.id.header_show_normal_price);
        name.setText(guessGoods.get(position).getGoods_name());

        String shopPrice = guessGoods.get(position).getShop_price();
        String marketPrice = guessGoods.get(position).getMarket_price();
        if (marketPrice != null)
            normalPrice.setText("官方价格：¥" + marketPrice.substring(0, marketPrice.length() - 3));
        price.setText("价格：¥" + shopPrice.substring(0, shopPrice.length() - 3));
//        System.out.println(guessGoods.get(position).getOriginal_img());
        Glide.with(context).load(HttpContants.BASE_URL + guessGoods.get(position).getOriginal_img()).into(imageView);
        return convertView;
    }
}
