package com.example.administrator.foodapp.utils;

import com.example.administrator.foodapp.bean.Account;
import com.example.administrator.foodapp.bean.Buy;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/8/1.
 */

public interface NetUtils {
    static final String URL = "http://192.168.1.186:8080";

    @FormUrlEncoded
    @POST("/foodApp/addAccount.account")
    Call<String> getRegisterInfo(@Field("name") String loginName, @Field("password") String loginPassword
            , @Field("phone") String phone, @Field("picture") String picture);

    @FormUrlEncoded
    @POST("/foodApp/searchAccount.account")
    Call<String> getLoginInfo(@Field("name") String loginName, @Field("password") String loginPassword);

    @FormUrlEncoded
    @POST("/foodApp/getAccountInfo.account")
    Call<String> getAccountInfo(@Field("name") String loginName);

    @FormUrlEncoded
    @POST("/foodApp/updateAccountPhone.account")
    Call<String> getAccountPhone(@Field("name") String loginName, @Field("phone") String phone);

    @Multipart
    @POST("/foodApp/updateAccountPicture.account")
    Call<String> getAccountPicture(@Query("name") String description, @Part("file\"; filename=\"image.png\"") RequestBody imgs);

    @FormUrlEncoded
    @POST("/foodApp/addBuy.buy")
    Call<String> getAddBuyInfo(@Field("address") String address, @Field("loginName") String loginName
            , @Field("text") String text, @Field("name") String name);

    @FormUrlEncoded
    @POST("/foodApp/getBuyInfo.buy")
    Call<String> getBuyInfo( @Field("loginName") String loginName);

    @FormUrlEncoded
    @POST("/foodApp/getBuyIdInfo.buy")
    Call<String> getBuyIdInfo( @Field("id") String id);

    @FormUrlEncoded
    @POST("/foodApp/getBuyUpdate.buy")
    Call<String> getBuyUpdateInfo( @Field("id") String id,@Field("address") String address
            , @Field("text") String text, @Field("name") String name);

    @FormUrlEncoded
    @POST("/foodApp/getBuyDelete.buy")
    Call<String> getBuyDeleteInfo( @Field("id") String id);

    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @FormUrlEncoded
    @POST("/foodApp/addOrder.order")
    Call<String> getOrderAddInfo( @Field("loginName") String loginName
            , @Field("buyId") String buyId, @Field("food") String food);

    @FormUrlEncoded
    @POST("/foodApp/searchOrder.order")
    Call<String> getOrderInfo( @Field("loginName") String loginName);

    @FormUrlEncoded
    @POST("/foodApp/getAddress.buy")
    Call<String> getAddress( @Field("id") String id);

    @FormUrlEncoded
    @POST("/foodApp/addLove.love")
    Call<String> getAddLoveInfo( @Field("loginName") String loginName,@Field("love") String love);


    @FormUrlEncoded
    @POST("/foodApp/getLoveInfo.love")
    Call<String> getLoveInfo( @Field("loginName") String loginName);

    @FormUrlEncoded
    @POST("/foodApp/deleteLove.love")
    Call<String> getDeleteLoveInfo( @Field("id") String id);
}
