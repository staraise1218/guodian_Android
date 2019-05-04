package com.smile.guodian.model.entity;

import java.util.List;

public class ProductBean {
    private int code;
    private Product data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Product getData() {
        return data;
    }

    public void setData(Product data) {
        this.data = data;
    }

    public class Product {

        private List<ProductCategory> categoryList;
        private List<ProductGood> goodsList;

        public List<ProductCategory> getCategoryList() {
            return categoryList;
        }

        public void setCategoryList(List<ProductCategory> categoryList) {
            this.categoryList = categoryList;
        }

        public List<ProductGood> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<ProductGood> goodsList) {
            this.goodsList = goodsList;
        }
    }

}
