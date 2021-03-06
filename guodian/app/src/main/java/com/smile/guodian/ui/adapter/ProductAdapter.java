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
import com.smile.guodian.model.entity.ProductGood;

import java.util.List;

import butterknife.BindView;

public class ProductAdapter extends BaseAdapter {

    public List<ProductGood> getGoodList() {
        return goodList;
    }

    private List<ProductGood> goodList;
    private Context context;
    private String name;

    public ProductAdapter(List<ProductGood> goodList, Context context, String name) {
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
        public TextView huiyuan;
        public TextView market;
        public ImageView stateImage;
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
            viewHolder.huiyuan = convertView.findViewById(R.id.huiyuan);
            viewHolder.market = convertView.findViewById(R.id.item_home_product_price1);
            viewHolder.stateImage = convertView.findViewById(R.id.item_home_state_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        viewHolder.huiyuan.setVisibility(View.GONE);
        Glide.with(context).load(HttpContants.BASE_URL + goodList.get(position).getOriginal_img()).into(viewHolder.imageView);
        viewHolder.name.setText(goodList.get(position).getGoods_name());
        String price = goodList.get(position).getShop_price();
        viewHolder.price.setText("销售价:¥" + price.substring(0, price.length() - 3));
        viewHolder.title.setText(name);

        viewHolder.stateImage.setVisibility(View.GONE);

        if (goodList.get(position).getStore_count() == 0) {
            viewHolder.stateImage.setVisibility(View.VISIBLE);
            viewHolder.stateImage.setBackgroundResource(R.drawable.yishouqing);
        }
        if (goodList.get(position).getReserved() == 1) {
            viewHolder.stateImage.setVisibility(View.VISIBLE);
            viewHolder.stateImage.setBackgroundResource(R.drawable.yiyuding);
        }

        String marketPrice = goodList.get(position).getMarket_price();
        if (marketPrice != null)
            viewHolder.market.setText("官方公价:¥" + marketPrice.substring(0, marketPrice.length() - 3));

        return convertView;
    }
}
