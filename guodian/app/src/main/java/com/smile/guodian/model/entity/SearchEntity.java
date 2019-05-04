package com.smile.guodian.model.entity;


import java.util.List;

public class SearchEntity {

    public static final int HOTSEARCH = 1;
    public static final int SEARCHHISTORY = 2;
    public static final int GUESS = 3;
    public static final int EMPTY = 4;
    public static final int RESULT = 5;

    int code;
    private List<String> hotKeyword;
    private List<FavouriteGoods> favourite_goods;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getHotKeyword() {
        return hotKeyword;
    }

    public void setHotKeyword(List<String> hotKeyword) {
        this.hotKeyword = hotKeyword;
    }

    public List<FavouriteGoods> getFavourite_goods() {
        return favourite_goods;
    }

    public void setFavourite_goods(List<FavouriteGoods> favourite_goods) {
        this.favourite_goods = favourite_goods;
    }
}
