package com.smile.guodian.model.entity;

public class ArticleDetail {
    private String title;
    private String thumb;
    private String content;
    private int isliked;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsliked() {
        return isliked;
    }

    public void setIsliked(int isliked) {
        this.isliked = isliked;
    }
}
