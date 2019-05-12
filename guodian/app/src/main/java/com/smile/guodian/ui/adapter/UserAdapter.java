
package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.LoginActivity;
import com.smile.guodian.ui.activity.SettingActivity;
import com.smile.guodian.ui.activity.WebActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter {

    private Context context;

    public UserAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        switch (i) {
            case 0:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_header, viewGroup, false);
                return new HeaderViewHolder(view);
            case 1:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_tips, viewGroup, false);
                return new TipViewHolder(view);
            case 2:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_message, viewGroup, false);
                return new MessageViewHolder(view);
            case 3:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_tips, viewGroup, false);
                return new TipViewHolder(view);
            case 4:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_footer, viewGroup, false);
                return new FooterViewHolder(view);
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_footer_loading, viewGroup, false);
                return new FooterViewHolder(view);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        @BindView(R.id.user_icon)
        public CircleImageView circleImageView;
        @BindView(R.id.user_name)
        TextView name;
        @BindView(R.id.user_rename)
        TextView rename;
        @BindView(R.id.user_wait)
        LinearLayout wait;
        @BindView(R.id.user_receive)
        LinearLayout receive;
        @BindView(R.id.user_return)
        LinearLayout re;
        @BindView(R.id.user_all)
        LinearLayout all;

        @OnClick({R.id.user_wait, R.id.user_receive, R.id.user_return, R.id.user_all})
        public void clickView(View view) {
            Intent intent = new Intent(context, WebActivity.class);
            switch (view.getId()) {
                case R.id.user_wait:
                    intent.putExtra("type", 4);
                    context.startActivity(intent);
                    break;
                case R.id.user_receive:
                    intent.putExtra("type", 5);
                    context.startActivity(intent);
                    break;
                case R.id.user_return:
                    intent.putExtra("type", 6);
                    context.startActivity(intent);
                    break;
                case R.id.user_all:
                    intent.putExtra("type", 7);
                    context.startActivity(intent);
                    break;
            }
        }

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {


        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public class TipViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_about)
        public GridView gridView;

        public TipViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TipViewHolder tipViewHolder;
        System.out.println("holder--t" + i);
        switch (i) {
            case 0:
                List<User> users = BaseApplication.getDaoSession().getUserDao().loadAll();
                User user = new User();
                if (users.size() > 0)
                    user = users.get(0);
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;

                if (user.getHead_pic() != null) {
                    Glide.with(context).load(HttpContants.BASE_URL + user.getHead_pic()).into(headerViewHolder.circleImageView);
                }

                headerViewHolder.name.setText("姓名：" + user.getRealname());
//                headerViewHolder.rename.setText("");

                headerViewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SettingActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case 1:
                tipViewHolder = (TipViewHolder) viewHolder;
                tipViewHolder.gridView.setAdapter(new ItemTipAdapter(2, context));
                break;
            case 3:
                tipViewHolder = (TipViewHolder) viewHolder;
                tipViewHolder.gridView.setAdapter(new ItemTipAdapter(4, context));
                break;

        }

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}