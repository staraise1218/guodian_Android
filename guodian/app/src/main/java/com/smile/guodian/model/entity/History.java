package com.smile.guodian.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class History {
    @Id(autoincrement = true)
    long id;

    private String history;

    @Generated(hash = 619429370)
    public History(long id, String history) {
        this.id = id;
        this.history = history;
    }

    @Generated(hash = 869423138)
    public History() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
