package com.smile.guodian.ui.adapter.search;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.smile.guodian.R;
import com.smile.guodian.model.entity.ProductCategory;

import java.util.List;

public class TextAdapter extends BaseAdapter {

    private List<ProductCategory> strings;
    private Context context;

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    private int selectPosition;

    public TextAdapter(List<ProductCategory> strings, Context context, int selectPosition) {
        this.strings = strings;
        this.context = context;
        this.selectPosition = selectPosition;
    }

    @Override
    public int getCount() {
        return strings.size();
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
        convertView = View.inflate(context, R.layout.item_text, null);
        TextView textView = convertView.findViewById(R.id.item_text);
        textView.setText(strings.get(position).getCat_name());
        if (selectPosition == position) {
            textView.setTextColor(Color.parseColor("#FF9F4D"));
        } else {
            textView.setTextColor(Color.BLACK);
        }
        return convertView;
    }
}
