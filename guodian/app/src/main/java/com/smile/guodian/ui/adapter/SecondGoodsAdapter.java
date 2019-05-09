package com.smile.guodian.ui.adapter;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Category;
import com.smile.guodian.utils.GlideUtil;

import java.util.List;

public class SecondGoodsAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    private Activity context;

    public SecondGoodsAdapter(List<Category> datas, Activity activity) {
        super(R.layout.template_category_wares, datas);
        this.context = activity;
    }

    @Override
    protected void convert(BaseViewHolder holder, Category bean) {
        holder.setText(R.id.text_title, bean.getName());
//        GlideUtil.load(context,bean.getImgUrl(),(ImageView)holder.getView(R.id.iv_view));
        Glide.with(context).load(HttpContants.BASE_URL + bean.getLogo()).into((ImageView) holder
                .getView(R.id.iv_view));
    }
}
