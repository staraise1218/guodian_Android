package com.smile.guodian.model.entity;

import java.util.List;

public class CategoryBean {
    private int code;
    private List<Category> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }
}
