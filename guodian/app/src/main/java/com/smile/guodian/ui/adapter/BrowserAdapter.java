package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Browser;

import org.w3c.dom.Text;

import java.util.List;

public class BrowserAdapter extends BaseAdapter {

    private Context context;
    private List<Browser> browsers;
    private boolean showCheck = false;
    private String title;

    public void setShowCheck(boolean showCheck) {
        this.showCheck = showCheck;
        title = "";
    }

    public List<Browser> getBrowsers() {
        return browsers;
    }

    private boolean checkAll;

    public void setCheckAll(boolean checkAll) {
        this.checkAll = checkAll;
    }

    public void setBrowsers(List<Browser> browsers) {
        title = "";
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
    public View getView(final int position, View convertView, ViewGroup parent) {


        convertView = View.inflate(context, R.layout.item_browser, null);


        if (browsers.get(position).getTitle() != title) {
            TextView time = convertView.findViewById(R.id.item_browser_time);
            TextView divider = convertView.findViewById(R.id.divider);
            time.setText(browsers.get(position).getTitle());
            time.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
            title = browsers.get(position).getTitle();
        }


        CheckBox checkBox = convertView.findViewById(R.id.item_browser_check);
        if (checkAll) {
//            System.out.println(position + "+++");
            browsers.get(position).setChecked(true);
            checkBox.setChecked(true);
        } else {
            browsers.get(position).setChecked(false);
            checkBox.setChecked(false);
        }

        if (showCheck) {
            checkBox.setVisibility(View.VISIBLE);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    browsers.get(position).setChecked(true);
                } else {
                    browsers.get(position).setChecked(false);
                }

            }
        });

        ImageView imageView = convertView.findViewById(R.id.item_browser_img);
        TextView name = convertView.findViewById(R.id.item_browser_name);
        TextView price = convertView.findViewById(R.id.item_browser_price);
        name.setText(browsers.get(position).getGoods_name());
        price.setText("¥" + browsers.get(position).getShop_price());
        Glide.with(context).load(HttpContants.BASE_URL + browsers.get(position).getOriginal_img()).into(imageView);

        return convertView;
    }
}
