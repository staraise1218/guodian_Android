package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.ui.activity.MyBrowserActivity;
import com.smile.guodian.ui.activity.MyCollectionActivity;
import com.smile.guodian.ui.activity.WebActivity;
import com.smile.guodian.ui.activity.message.MessageCenterActivity;

public class ItemTipAdapter extends BaseAdapter {
    private int type;
    private Context context;

    private int icon1[] = new int[]{R.drawable.collection, R.drawable.browse, R.drawable.vip, R.drawable.coupons, R.drawable.integral};
    private String tip1[] = new String[]{"我的收藏", "我的浏览", "会员中心", "优惠券", "我的积分"};

    private int icon2[] = new int[]{R.drawable.xianzhi, R.drawable.gaozhi, R.drawable.keep, R.drawable.kefu, R.drawable.jingying, R.drawable.yinsi};
    private String tip2[] = new String[]{"我的闲置", "消费者告知书", "我的养护", "客服与帮助", "经营信息", "隐私权策略"};

    public ItemTipAdapter(int type, Context context) {
        this.type = type;
        this.context = context;
    }

    @Override
    public int getCount() {

        return 6;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_tips, null);
        TextView textView = convertView.findViewById(R.id.item_tip_text);
        ImageView imageView = convertView.findViewById(R.id.item_tip_img);
        LinearLayout tips = convertView.findViewById(R.id.item_tips);

        if (type == 2 && position != 5) {
            textView.setText(tip1[position]);
            imageView.setBackgroundResource(icon1[position]);
        } else if (type == 4) {
            textView.setText(tip2[position]);
            imageView.setBackgroundResource(icon2[position]);
        }

        if (type == 2 && position == 2) {
            tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("type", 18);
                    context.startActivity(intent);
                }
            });
        }

        if (type == 2 && position == 0) {
            tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MyCollectionActivity.class);
                    context.startActivity(intent);
                }
            });
        }
        if (type == 2 && position == 1) {
            tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MyBrowserActivity.class);
                    context.startActivity(intent);
                }
            });
        }
        if (type == 4 && position == 0) {
            tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("type", 16);
                    context.startActivity(intent);
                }
            });
        }

        if (type == 4 && position == 1) {
            tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("type", 3);
                    context.startActivity(intent);
                }
            });
        }
        if (type == 4 && position == 2) {
            tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("type", 17);
                    context.startActivity(intent);
                }
            });
        }
        if (type == 4 && position == 4) {
            tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("type", 10);
                    context.startActivity(intent);
                }
            });
        }
        if (type == 4 && position == 5) {
            tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("type", 11);
                    context.startActivity(intent);
                }
            });
        }

        if (type == 2 && position == 3) {
            tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("type", 8);
                    context.startActivity(intent);
                }
            });
        }
        if (type == 2 && position == 4) {
            tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("type", 9);
                    context.startActivity(intent);
                }
            });
        }

        if (type == 4 && position == 3) {
            tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("type", 20);
                    intent.putExtra("url", HttpContants.BASE_URL + "/page/kfAndHelp.html");
                    context.startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
