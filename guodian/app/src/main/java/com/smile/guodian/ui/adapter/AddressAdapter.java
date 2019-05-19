package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smile.guodian.R;
import com.smile.guodian.model.HttpContants;
import com.smile.guodian.model.entity.Address;
import com.smile.guodian.model.entity.User;
import com.smile.guodian.okhttp.OkCallback;
import com.smile.guodian.okhttp.OkHttp;
import com.smile.guodian.ui.BaseApplication;
import com.smile.guodian.ui.activity.me.AddressActivity;
import com.smile.guodian.ui.activity.me.EditAddressActivity;
import com.smile.guodian.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressAdapter extends BaseAdapter {

    private Context context;
    private List<Address> addressList;

    public AddressAdapter(Context context, List<Address> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    User user;
    Address address;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        address = addressList.get(position);
        user = BaseApplication.getDaoSession().getUserDao().loadAll().get(0);

        if (address.getIs_default() == 1) {
            convertView = View.inflate(context, R.layout.item_address_default, null);
            TextView name = convertView.findViewById(R.id.address_default_name);
            TextView add = convertView.findViewById(R.id.address_default_address);
            name.setText(user.getRealname() + "  " + user.getMobile());
            add.setText(address.getFulladdress());
            ImageView edit = convertView.findViewById(R.id.address_default_edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditAddressActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        if (address.getIs_default() == 0) {
            convertView = View.inflate(context, R.layout.item_address_normal, null);

            ImageView edit = convertView.findViewById(R.id.address_edit);
            ImageView del = convertView.findViewById(R.id.address_delete);
            TextView name = convertView.findViewById(R.id.address_name);
            TextView add = convertView.findViewById(R.id.address_add);
            name.setText(user.getRealname() + "  " + user.getMobile());
            add.setText(address.getFulladdress());
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditAddressActivity.class);
                    context.startActivity(intent);
                }
            });
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initData(user.getUser_id(), address.getAddress_id(), position);
                }
            });

        }


        return convertView;
    }


    public void initData(String uid, int addressid, final int position) {

        Map<String, String> params = new HashMap<>();

        OkHttp.post(context, HttpContants.BASE_URL + "/Api/Address/del_address?user_id=" + uid + "&address_id=" + addressid, params, new OkCallback() {
            @Override
            public void onResponse(String response) {

                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    int code = object.getInt("code");

                    if (code == 200) {
                        addressList.remove(position);
                        AddressAdapter.this.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String state, String msg) {
                ToastUtil.showShortToast(context, msg);
            }
        });


    }


}
