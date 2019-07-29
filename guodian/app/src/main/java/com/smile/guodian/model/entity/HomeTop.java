package com.smile.guodian.model.entity;

import java.util.List;


public class HomeTop {

    private List<Carousel> carousel;
    private List<HomeBase> headlines;
    private List<HomeBase> list;
    private List<Hot> hotlist;
    private List<Hot> reg_about;
    private List<Hot> custom_goods;
    private List<GuessGoods> guessGoodsList;
    private List<CategoryList> categoryLists;

    public List<CategoryList> getCategoryLists() {
        return categoryLists;
    }

    public void setCategoryLists(List<CategoryList> categoryLists) {
        this.categoryLists = categoryLists;
    }

    public List<Hot> getHotlist() {
        return hotlist;
    }

    public void setHotlist(List<Hot> hotlist) {
        this.hotlist = hotlist;
    }

    public List<Hot> getReg_about() {
        return reg_about;
    }

    public void setReg_about(List<Hot> reg_about) {
        this.reg_about = reg_about;
    }

    public List<Hot> getCustom_goods() {
        return custom_goods;
    }

    public void setCustom_goods(List<Hot> custom_goods) {
        this.custom_goods = custom_goods;
    }

    public List<GuessGoods> getGuessGoodsList() {
        return guessGoodsList;
    }

    public void setGuessGoodsList(List<GuessGoods> guessGoodsList) {
        this.guessGoodsList = guessGoodsList;
    }

    public List<HomeBase> getHeadlines() {
        return headlines;
    }

    public void setHeadlines(List<HomeBase> headlines) {
        this.headlines = headlines;
    }


    public List<Carousel> getCarousel() {
        return carousel;
    }

    public void setCarousel(List<Carousel> carousel) {
        this.carousel = carousel;
    }

    public List<HomeBase> getList() {
        return list;
    }

    public void setList(List<HomeBase> list) {
        this.list = list;
    }

    public static class Carousel {
        private String url;
        private String ad_link;
        private int id;

        public Carousel(int id, String url, String ad_link) {
            this.id = id;
            this.url = url;
            this.ad_link = ad_link;
        }

        public String getAd_link() {
            return ad_link;
        }

        public void setAd_link(String ad_link) {
            this.ad_link = ad_link;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
