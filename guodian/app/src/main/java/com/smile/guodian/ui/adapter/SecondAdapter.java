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
import com.smile.guodian.model.entity.Category;

import java.util.List;

public class SecondAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categories;

    public SecondAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
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


    class ViewHolder {
        TextView name;
        ImageView icon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.template_category_wares, null);
        TextView name = convertView.findViewById(R.id.text_title);
        ImageView imageView = convertView.findViewById(R.id.iv_view);
//        ImageView bg = convertView.findViewById(R.id.state_view);

//        if(categories.get(position)
        name.setText(categories.get(position).getName());
        Glide.with(context).load(HttpContants.BASE_URL + categories.get(position).getLogo()).into(imageView);
        return convertView;
    }
}
