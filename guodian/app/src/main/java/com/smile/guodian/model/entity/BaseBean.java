package com.smile.guodian.model.entity;

import java.io.Serializable;


public class BaseBean implements Serializable {

    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
