package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.ui.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        System.out.println("holder--" + i);
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

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            title = (TextView) itemView.findViewById(R.id.title);
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
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
                headerViewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context,LoginActivity.class);
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
