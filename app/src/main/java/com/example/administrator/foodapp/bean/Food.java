package com.example.administrator.foodapp.bean;

/**
 * Created by Administrator on 2017/8/6.
 */

public class Food {
    private String title;
    private String img;
    private String cost;

    public Food() {
    }

    public Food(String title, String img, String cost) {
        this.title = title;
        this.img = img;
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String toString() {
        return "title:" + title + ",img:" + img + ",cost:" + cost;
    }
}
