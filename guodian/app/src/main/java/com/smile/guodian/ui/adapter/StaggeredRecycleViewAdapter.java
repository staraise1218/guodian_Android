package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Find;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.RegisterActivity;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
        public RelativeLayout view;
        public TextView zan;
        public CheckBox give;

        public ViewHolder(View itemView) {
            super(itemView);
            zan = itemView.findViewById(R.id.item_zan);
            view = itemView.findViewById(R.id.item_view);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
//            title = (TextView) itemView.findViewById(R.id.title);
            give = itemView.findViewById(R.id.item_give);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_recycler_view_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        System.out.println(position);
//        holder.title.setText(finds.get(position).getTitle());

//        if (finds.get(position).getTitle() == null) {
//            holder.give.setBackgroundColor(Color.WHITE);
//        } else {

        if (finds.get(position).getIsliked() == 1) {
            holder.give.setChecked(true);
            holder.zan.setTextColor(Color.parseColor("#DDA021"));
            holder.give.setEnabled(false);
        } else {
            holder.give.setChecked(false);
            holder.zan.setTextColor(Color.BLACK);
        }

        final TextView textView = holder.zan;
        final int number = Integer.parseInt(holder.zan.getText().toString());

        holder.give.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    int number = Integer.parseInt(holder.zan.getText().toString()) + 1;
                    System.out.println(number);
                    holder.zan.setTextColor(Color.parseColor("#DDA021"));
                    holder.zan.setText(number + "");
                    checked(finds.get(position).getArticle_id());
                    holder.give.setEnabled(false);
                }
            }
        });

        Glide.with(mContext).load(HttpContants.BASE_URL + finds.get(position).getTumb()).into(holder.imageView);
        holder.zan.setText(finds.get(position).getLike_num());
    }


    @Override
    public int getItemCount() {
        return finds.size();
    }


    public void checked(int cat_id) {

        Map<String, String> params = new HashMap<>();

        List<User> userList = BaseApplication.getDaoSession().getUserDao().loadAll();
        User user = new User();
        if (userList.size() > 0) {
            user = userList.get(0);
        }

        params.put("user_id", user.getUser_id() + "");
        params.put("article_id", cat_id + "");

        OkHttp.post(mContext, HttpContants.BASE_URL + "/Api/like/clickLike", params, new OkCallback() {
            @Override
            public void onResponse(String response) {
//                System.out.println(response);
            }

            @Override
            public void onFailure(String state, String msg) {
                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
            }
        });

    }

}
