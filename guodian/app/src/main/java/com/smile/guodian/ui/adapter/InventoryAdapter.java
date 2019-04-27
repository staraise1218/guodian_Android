package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Collection;

import java.util.List;


public class InventoryAdapter extends BaseRecyclerViewAdapter<Collection.MyCollection> {

    private OnDeleteClickLister mDeleteClickListener;
    private Context context;


    public InventoryAdapter(Context context, List<Collection.MyCollection> data) {
        super(context, data, R.layout.item_inventroy);
        this.context = context;
    }

    @Override
    protected void onBindData(RecyclerViewHolder holder, Collection.MyCollection bean, int position) {
        View view = holder.getView(R.id.tv_delete);
        view.setTag(position);
        if (!view.hasOnClickListeners()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteClickListener != null) {
                        mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                    }
                }
            });
        }
        ((TextView) holder.getView(R.id.item_browser_name)).setText(bean.getGoods_name());
//        String quantity = bean.getQuantity() + "箱";
        ImageView imageView = ((ImageView) holder.getView(R.id.tv_quantity));
        ((TextView)holder.getView(R.id.item_browser_tip)).setText(bean.getGoods_name());
        Glide.with(context).load(HttpContants.BASE_URL+bean.getOriginal_img()).into(imageView);
//        .setText(quantity);
//        String detail = bean.getItemCode() + "/" + bean.getDate();
        ((TextView) holder.getView(R.id.item_browser_price)).setText("¥"+bean.getShop_price());
//        String volume = bean.getVolume() + "方";
//        ((TextView) holder.getView(R.id.tv_volume)).setText(volume);
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
