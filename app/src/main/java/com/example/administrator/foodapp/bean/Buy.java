package com.example.administrator.foodapp.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/4.
 */

public class Buy implements Serializable {
    private String name;
    private String text;
    private String loginName;
    private String address;
    private String id;

    public Buy() {
    }

    public Buy(String name, String loginName, String address,
               String text, String id) {
        this.name = name;
        this.text = text;
        this.loginName = loginName;
        this.address = address;
        this.id = id;

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return "name:" + name + ",loginName:" + loginName + ",address:"
                + address + ",text:" + text+",id:"+id;
    }
}
