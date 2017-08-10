package com.example.administrator.foodapp.bean;

/**
 * Created by Administrator on 2017/8/1.
 */

public class Account {
    private String name;
    private String password;
    private String phone;
    private String picture;


    public Account() {
    }

    public Account(String name, String password, String phone, String picture) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.picture = picture;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "name:" + name + ",password:" + password + ",phone:" + phone
                + ",picture:" + picture;
    }
}
