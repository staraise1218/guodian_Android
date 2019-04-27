package com.smile.guodian.model.entity;

import java.util.List;

public class Collection {
    private int code;
    private List<MyCollection> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<MyCollection> getData() {
        return data;
    }

    public void setData(List<MyCollection> data) {
        this.data = data;
    }

    public class MyCollection{
        private int collect_id;
        private long add_time;
        private int goods_id;
        private String goods_name;
        private String original_img;
        private int store_count;
        private String shop_price;

        public String getShop_price() {
            return shop_price;
        }

        public void setShop_price(String shop_price) {
            this.shop_price = shop_price;
        }

        public int getCollect_id() {
            return collect_id;
        }

        public void setCollect_id(int collect_id) {
            this.collect_id = collect_id;
        }

        public long getAdd_time() {
            return add_time;
        }

        public void setAdd_time(long add_time) {
            this.add_time = add_time;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getOriginal_img() {
            return original_img;
        }

        public void setOriginal_img(String original_img) {
            this.original_img = original_img;
        }

        public int getStore_count() {
            return store_count;
        }

        public void setStore_count(int store_count) {
            this.store_count = store_count;
        }
    }
}
