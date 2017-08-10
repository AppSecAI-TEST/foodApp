package com.example.administrator.foodapp.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Administrator on 2017/7/31.
 */

public class TokenUtils {
    public static final String KEY_TOKEN = "token";
    public static final String APP_ID = "com.example.administrator.foodapp.utils";

    public static String getCachedToken(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_TOKEN, null);
    }

    public static void cachedToken(Context context, String token) {
        SharedPreferences.Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putString(KEY_TOKEN, token);
        e.commit();
    }

}
