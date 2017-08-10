package com.example.administrator.foodapp.bean;

/**
 * Created by Administrator on 2017/8/7.
 */

public class AccountOrder {
    private String address;
    private String buy_name;
    private String food;

    public AccountOrder() {
    }

    public AccountOrder(String address, String buy_name, String food) {
        this.address = address;
        this.buy_name = buy_name;
        this.food = food;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBuy_name() {
        return buy_name;
    }

    public void setBuy_name(String buy_name) {
        this.buy_name = buy_name;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    @Override
    public String toString() {
        return "address:" + address + ",buy_name:" + buy_name + ",food:" + food;
    }
}
