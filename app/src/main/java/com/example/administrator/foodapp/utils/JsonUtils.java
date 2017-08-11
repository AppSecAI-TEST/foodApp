package com.example.administrator.foodapp.utils;

import com.example.administrator.foodapp.bean.Account;
import com.example.administrator.foodapp.bean.Buy;
import com.example.administrator.foodapp.bean.Love;
import com.example.administrator.foodapp.bean.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */

public class JsonUtils {
    /**
     * 解析json数据转换为Buy对象
     */
    public static Buy parseBuyString(String jsonData) {
        Gson gson = new Gson();
        Buy b = gson.fromJson(jsonData, Buy.class);
        return b;
    }
    /**
     * 解析json数据转换为User对象
     */
    public static Account parseAccountString(String jsonData) {
        Gson gson = new Gson();
        Account a = gson.fromJson(jsonData, Account.class);
        return a;
    }
    /**
     * 解析json数据转换为List<Buy>对象集合
     */
    public static List<Buy> parseBuyListString(String jsonData) {
        Gson gson = new Gson();
        try {
            // TypeToken 是google提供的一个解析Json数据的类库中一个类
            List<Buy> list = gson.fromJson(jsonData, new TypeToken<List<Buy>>(){}.getType());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;
    }
    /**
     * 解析json数据转换为List<Order>对象集合
     */
    public static List<Order> parseOrderListString(String jsonData) {
        Gson gson = new Gson();
        try {
            // TypeToken 是google提供的一个解析Json数据的类库中一个类
            List<Order> list = gson.fromJson(jsonData, new TypeToken<List<Order>>(){}.getType());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;
    }
    public static List<Love> parseLoveListString(String jsonData) {
        Gson gson = new Gson();
        try {
            // TypeToken 是google提供的一个解析Json数据的类库中一个类
            List<Love> list = gson.fromJson(jsonData, new TypeToken<List<Love>>(){}.getType());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;
    }
}
