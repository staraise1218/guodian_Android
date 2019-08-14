
package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.GuessGoods;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.me.SettingActivity;
import com.smile.guodian.ui.activity.WebActivity;
import com.smile.guodian.ui.activity.me.PersonActivity;
import com.smile.guodian.ui.activity.message.MessageCenterActivity;
import com.smile.guodian.widget.HomeGridView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter {

    private Context context;
    //    private List<>
    List<GuessGoods> guessGoodsList;
    private int waitCount;
    private int receiveCount;
    private int returnCount;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<GuessGoods> getGuessGoodsList() {
        return guessGoodsList;
    }

    public void setGuessGoodsList(List<GuessGoods> guessGoodsList) {
        this.guessGoodsList = guessGoodsList;
    }

    public int getWaitCount() {
        return waitCount;
    }

    public void setWaitCount(int waitCount) {
        this.waitCount = waitCount;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public int getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(int returnCount) {
        this.returnCount = returnCount;
    }

    public UserAdapter(Context context, List<GuessGoods> guessGoodsList) {
        this.context = context;
        this.guessGoodsList = guessGoodsList;
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
        @BindView(R.id.user_setting)
        ImageView setting;
        @BindView(R.id.user_notify)
        ImageView notify;
        @BindView(R.id.wait_count)
        TextView waitCount;
        @BindView(R.id.receive_count)
        TextView receiveCount;
        @BindView(R.id.return_count)
        TextView returnCount;

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
        @BindView(R.id.user_footer_content)
        public HomeGridView gridView;

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
        @BindView(R.id.user_banner)
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

                headerViewHolder.notify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), WebActivity.class);
                        intent.putExtra("type", 20);
                        intent.putExtra("url", HttpContants.BASE_URL + "/page/message.html");
//                        startActivity(intent);
//                        Intent intent = new Intent(context, MessageCenterActivity.class);
                        context.startActivity(intent);
                    }
                });
                headerViewHolder.setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SettingActivity.class);
                        context.startActivity(intent);
                    }
                });

                headerViewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PersonActivity.class);
                        context.startActivity(intent);
                    }
                });


                if (receiveCount == 0) {
                    headerViewHolder.receiveCount.setVisibility(View.GONE);
                } else {
                    headerViewHolder.receiveCount.setVisibility(View.VISIBLE);
                    headerViewHolder.receiveCount.setText(receiveCount + "");
                }
                if (returnCount == 0) {
                    headerViewHolder.returnCount.setVisibility(View.GONE);
                } else {
                    headerViewHolder.returnCount.setVisibility(View.VISIBLE);
                    headerViewHolder.returnCount.setText(returnCount + "");
                }
                if (waitCount == 0) {
                    headerViewHolder.waitCount.setVisibility(View.GONE);
                } else {
                    headerViewHolder.waitCount.setVisibility(View.VISIBLE);
                    headerViewHolder.waitCount.setText(waitCount + "");
                }

                break;
            case 1:
                tipViewHolder = (TipViewHolder) viewHolder;
                tipViewHolder.gridView.setAdapter(new ItemTipAdapter(2, context));
                break;
            case 2:
                MessageViewHolder messageViewHolder = (MessageViewHolder) viewHolder;
                messageViewHolder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("type", 20);
                        intent.putExtra("url", "http://www.guodianjm.com/page/myMember.html");
                        context.startActivity(intent);
                    }
                });
                break;
            case 3:
                tipViewHolder = (TipViewHolder) viewHolder;
                tipViewHolder.gridView.setAdapter(new ItemTipAdapter(4, context));
                break;

            case 4:
                FooterViewHolder holder = (FooterViewHolder) viewHolder;

                HomeHeaderAdapter headerAdapter = new HomeHeaderAdapter(context);
                headerAdapter.setGuessGoods(guessGoodsList);
                holder.gridView.setAdapter(headerAdapter);
                holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("name", guessGoodsList.get(position).getGoods_name());
                        intent.putExtra("goodsId", guessGoodsList.get(position).getGoods_id() + "");
                        context.startActivity(intent);

                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}