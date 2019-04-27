package com.smile.guodian.ui.adapter;

import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smile.guodian.R;
import com.smile.guodian.model.entity.Category;

import java.util.List;

public class CategoryAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    List<Category> datas;
    private int position = 0;
    private int current = 0;

    public void setPosition(int position) {

        this.position = position;
    }

    public CategoryAdapter(List<Category> datas) {
        super(R.layout.template_single_text, datas);
        this.datas = datas;
    }

    @Override
    protected void convert(BaseViewHolder holder, Category item) {
        if (datas.get(position).equals(item)) {
            holder.getView(R.id.textView).setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.getView(R.id.line).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.textView).setBackgroundColor(Color.parseColor("#E7E7E7"));
            holder.getView(R.id.line).setVisibility(View.GONE);
        }
        holder.setText(R.id.textView, item.getCat_name());
    }

    @Override
    public int getItemViewType(int position) {

//       System.out.println(position);

        return super.getItemViewType(position);
    }
}
