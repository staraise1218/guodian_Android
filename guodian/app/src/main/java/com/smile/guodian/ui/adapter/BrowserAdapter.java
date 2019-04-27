package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.smile.guodian.R;
import com.smile.guodian.model.entity.Browser;

import java.util.List;

public class BrowserAdapter extends BaseAdapter {

    private Context context;
    private List<Browser> browsers;

    public void setBrowsers(List<Browser> browsers) {
        this.browsers = browsers;
    }

    public BrowserAdapter(Context context, List<Browser> browsers) {
        this.context = context;
        this.browsers = browsers;
    }

    @Override
    public int getCount() {
        return browsers.size();
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
        convertView = View.inflate(context,R.layout.item_browser,null);
        return convertView;
    }
}
