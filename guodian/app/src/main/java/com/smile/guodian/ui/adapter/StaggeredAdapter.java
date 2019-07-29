package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.smile.guodian.ui.activity.LoginActivity;
import com.smile.guodian.ui.activity.WebActivity;
import com.smile.guodian.ui.activity.found.FoundDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StaggeredAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String, String>> dataList;
    private List<Find> finds;
    int uid;

    public List<Find> getFinds() {
        return finds;
    }

//    public void setFinds(List<Find> finds) {
//        this.finds = finds;
//    }

    public List<Map<String, String>> getDataList() {
        return dataList;
    }

    public StaggeredAdapter(Context context) {
        mContext = context;
        finds = new ArrayList<>();
    }

    ViewHolder holder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_recycler_view_item, parent, false);
            holder = new ViewHolder();
            holder.zan = convertView.findViewById(R.id.item_zan);
            holder.view = convertView.findViewById(R.id.item_view);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_img);
            holder.content = convertView.findViewById(R.id.item_content);
//            title = (TextView) itemView.findViewById(R.id.title);
            holder.give = convertView.findViewById(R.id.item_give);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebActivity.class);

                intent.putExtra("article_id", finds.get(position).getArticle_id() + "");
                intent.putExtra("type", 19);
                mContext.startActivity(intent);
            }
        });

        holder.content.setText(finds.get(position).getTitle());
        if (finds.get(position).getIsliked() == 1) {
            holder.give.setChecked(true);
            holder.zan.setTextColor(Color.parseColor("#DDA021"));
            holder.give.setEnabled(false);
        } else {
            holder.give.setChecked(false);
            holder.zan.setTextColor(Color.BLACK);
        }

        final TextView textView = holder.zan;
        holder.give.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    uid = mContext.getSharedPreferences("db", Context.MODE_PRIVATE).getInt("uid", -1);

                    if (uid == -1) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.putExtra("where", "found");
                        mContext.startActivity(intent);
                        holder.give.setChecked(false);
                        holder.zan.setTextColor(Color.BLACK);
                    } else {
                        int number = 0;
                        if (holder.zan.getText().toString() != null) {
                            number = Integer.parseInt(holder.zan.getText().toString());
                        }
                        number += 1;
                        holder.zan.setTextColor(Color.parseColor("#DDA021"));
                        holder.zan.setText(number + "");
                        checked(finds.get(position).getArticle_id());
                        holder.give.setEnabled(false);
                    }
                }
            }
        });


        Glide.with(mContext).load(HttpContants.BASE_URL + finds.get(position).getTumb()).into(holder.imageView);
        holder.zan.setText(finds.get(position).getLike_num());

        return convertView;
    }

    class ViewHolder {
        public TextView title;
        public ImageView imageView;
        public RelativeLayout view;
        public TextView zan;
        public CheckBox give;
        public TextView content;
    }


    public void checked(int cat_id) {

        Map<String, String> params = new HashMap<>();

        List<User> userList = BaseApplication.getDaoSession().getUserDao().loadAll();
        User user = new User();

        if (userList.size() > 0) {
            user = userList.get(0);
        }

        System.out.println(user.getUser_id() + "user_id");


        params.put("user_id", user.getUser_id() + "");
        params.put("article_id", cat_id + "");

        OkHttp.post(mContext, HttpContants.BASE_URL + "/Api/find/clickLike", params, new OkCallback() {
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

    @Override
    public int getCount() {
        return finds.size();
    }

    @Override
    public Object getItem(int position) {
        return finds.get(position);
    }


    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public void addItemLast(List<Find> datas) {
        finds.addAll(datas);
    }

    public void addItemTop(List<Find> datas) {
        finds = datas;
    }
}