package com.smile.guodian.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class History {
    @Id(autoincrement = true)
    Long id;

    private String history;

    @Generated(hash = 2123561833)
    public History(Long id, String history) {
        this.id = id;
        this.history = history;
    }

    @Generated(hash = 869423138)
    public History() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
