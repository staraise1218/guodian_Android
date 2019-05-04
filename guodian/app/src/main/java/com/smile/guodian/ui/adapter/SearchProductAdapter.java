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
import com.smile.guodian.model.entity.ProductGood;

import java.util.List;

public class SearchProductAdapter extends BaseAdapter {

    private List<GuessGoods> goodList;
    private Context context;
    private String name;

    public SearchProductAdapter(List<GuessGoods> goodList, Context context, String name) {
        this.goodList = goodList;
        this.context = context;
        this.name = name;
    }

    @Override
    public int getCount() {
        return goodList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        public ImageView imageView;
        public TextView name;
        public TextView price;
        public TextView title;
    }

    ViewHolder viewHolder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_home_product, null);
            viewHolder.imageView = convertView.findViewById(R.id.item_home_product_img);
            viewHolder.name = convertView.findViewById(R.id.item_home_product_name);
            viewHolder.price = convertView.findViewById(R.id.item_home_product_price);
            viewHolder.title = convertView.findViewById(R.id.item_home_product_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(HttpContants.BASE_URL + goodList.get(position).getOriginal_img()).into(viewHolder.imageView);
        viewHolder.name.setText(goodList.get(position).getGoods_name());
        String price = goodList.get(position).getShop_price();
        viewHolder.price.setText("销售价:¥"+price.substring(0,price.length()-3));
        viewHolder.title.setText(name);

        return convertView;
    }
}
