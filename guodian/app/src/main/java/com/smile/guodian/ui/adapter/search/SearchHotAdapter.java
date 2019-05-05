package com.smile.guodian.ui.adapter.search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smile.guodian.R;

import java.util.List;

public class SearchHotAdapter extends BaseAdapter {

    private List<String> strings;
    private Context context;

    public SearchHotAdapter(List<String> strings, Context context) {
        this.strings = strings;
        this.context = context;
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
        convertView = View.inflate(context, R.layout.item_search_text, null);
        TextView textView = convertView.findViewById(R.id.item_search_text);
        textView.setText(strings.get(position));
        return convertView;
    }
}
