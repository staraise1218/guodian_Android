package com.smile.guodian.model.entity;

import java.util.List;

public class CategoryBean2 {
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private Category2Bean data;

    public Category2Bean getData() {
        return data;
    }

    public void setData(Category2Bean data) {
        this.data = data;
    }

    public class Category2Bean {
        private List<Category> list;

        public List<Category> getList() {
            return list;
        }

        public void setList(List<Category> list) {
            this.list = list;
        }

        private CategoryBanner category;

        public CategoryBanner getCategory() {
            return category;
        }

        public void setCategory(CategoryBanner category) {
            this.category = category;
        }


    }

    public class CategoryBanner {
        private String name;
        private String image2;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage2() {
            return image2;
        }

        public void setImage2(String image2) {
            this.image2 = image2;
        }
    }
}
