package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Find;

import java.util.List;
import java.util.Map;


public class StaggeredRecycleViewAdapter extends RecyclerView.Adapter<StaggeredRecycleViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String, String>> dataList;
    private List<Find> finds;

    public List<Find> getFinds() {
        return finds;
    }

    public void setFinds(List<Find> finds) {
        this.finds = finds;
    }

    public List<Map<String, String>> getDataList() {
        return dataList;
    }

    public StaggeredRecycleViewAdapter(Context context, List<Find> finds) {
        this.finds = finds;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imageView;
        public LinearLayout view;
        public TextView zan;

        public ViewHolder(View itemView) {
            super(itemView);
            zan = itemView.findViewById(R.id.item_zan);
            view = itemView.findViewById(R.id.item_view);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        System.out.println(position);
        holder.title.setText(finds.get(position).getTitle());
        System.out.println(HttpContants.BASE_URL + finds.get(position).getTumb());
        Glide.with(mContext).load(HttpContants.BASE_URL + finds.get(position).getTumb()).into(holder.imageView);
        holder.zan.setText(finds.get(position).getLike_num());

        //
// if (position % 2 == 0)
//            holder.imageView.setBackgroundResource(R.drawable.give_choose);
//        else {
//            holder.imageView.setBackgroundResource(R.drawable.timg);
//        }
//        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
//        params.height = Integer.parseInt(dataList.get(position).get("height"));
//        holder.imageView.setLayoutParams(params);//高度随机，下拉刷新高度会变
//        Drawable drawable = mContext.getResources().getDrawable(R.drawable.give);
//        // 设置图片的大小
//        drawable.setBounds(0, 0, 10, 10);
//        // 设置图片的位置，左、上、右、下
//        holder.zan.setCompoundDrawables(null, drawable, null, null);
    }

    @Override
    public int getItemCount() {
        return finds.size();
    }
}
