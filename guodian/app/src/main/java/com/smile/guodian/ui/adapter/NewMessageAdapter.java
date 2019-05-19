package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.model.entity.NewMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewMessageAdapter extends BaseAdapter {

    private Context context;
    private List<NewMessage> messages;

    public NewMessageAdapter(Context context, List<NewMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
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
        convertView = View.inflate(context, R.layout.item_new_message, null);

        ImageView tip = convertView.findViewById(R.id.item_new_message_tip);
        TextView time = convertView.findViewById(R.id.item_new_message_time);
        TextView content = convertView.findViewById(R.id.item_new_message_content);

        content.setText(messages.get(position).getMessage());
        if (messages.get(position).getStatus() == 0) {
            tip.setVisibility(View.VISIBLE);
        } else {
            tip.setVisibility(View.GONE);
        }


        Date date = new Date(messages.get(position).getSend_time());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        String dateTime = dateFormat.format(date);
        time.setText(dateTime);

        return null;
    }
}
