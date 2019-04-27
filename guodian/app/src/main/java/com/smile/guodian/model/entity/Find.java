package com.smile.guodian.model.entity;

public class Find {
    private int article_id;
    private String title;
    private String tumb;
    private String like_num;
    private int isliked;

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTumb() {
        return tumb;
    }

    public void setTumb(String tumb) {
        this.tumb = tumb;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public int getIsliked() {
        return isliked;
    }

    public void setIsliked(int isliked) {
        this.isliked = isliked;
    }
}
