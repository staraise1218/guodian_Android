package com.smile.guodian.model.entity;

import java.util.List;

public class SearchResultEntity {
    private int total_num;
    private List<ProductGood> list;

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public List<ProductGood> getList() {
        return list;
    }

    public void setList(List<ProductGood> list) {
        this.list = list;
    }
}
