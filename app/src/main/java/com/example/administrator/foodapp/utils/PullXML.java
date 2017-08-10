package com.example.administrator.foodapp.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.example.administrator.foodapp.bean.Food;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PullXML {
    private NetUtils net;
    List<Food> foods = null;
    Food f = null;

    public PullXML(List<Food> foods, Food f) {
        this.foods = foods;
        this.f = f;
    }
    /**
     * Pull解析XML
     */
    public List<Food> getNewsListFromInputStream(InputStream is)
            throws Exception {
        XmlPullParser parser = Xml.newPullParser();// 创建解析器
        parser.setInput(is, "utf-8");// 指定解析流，编码
        int eventType = parser.getEventType();// 开始解析
        while (eventType != XmlPullParser.END_DOCUMENT) {// 是否解析结束
            String tagName = parser.getName();// 取得节点名称
            switch (eventType) {
                case XmlPullParser.START_TAG:// 标签的开始 <>
                    if ("foods".equals(tagName)) {
                        foods = new ArrayList<Food>();
                    } else if ("food".equals(tagName)) {
                        f = new Food();
                    } else if ("image".equals(tagName)) {
                        f.setImg(parser.nextText());
                    } else if ("title".equals(tagName)) {
                        f.setTitle(parser.nextText());
                    } else if ("cost".equals(tagName)) {
                        f.setCost(parser.nextText());
                    }

                    break;
                case XmlPullParser.END_TAG:// 标签的结束 </>
                    if ("food".equals(tagName)) {
                        foods.add(f);
                    }
                    break;
                default:
                    break;
            }

            eventType = parser.next();// 下一个事件
        }
        return foods;
    }
}
