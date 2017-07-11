package com.atgc.cotton.retrofit;



import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
import rx.Observer;

/**
 *  Created by liw on 2017/7/5.
 */
public interface ApiStores {
    //baseUrl
    String API_SERVER_URL = HttpUrl.BASE_URL;



    //加载天气
    @GET("adat/sk/{cityId}.html")
    Observable<String> loadDataByRetrofitRxjava(@Path("cityId") String cityId);

    //手机号注册
    @FormUrlEncoded
    @POST("public/register")
    Observable<String> phoneRegister(@FieldMap Map<String,String> params);

    //手机号登录
    @FormUrlEncoded
    @POST("public/login")
    Observable<String> phoneLogin(@FieldMap Map<String,String> params);

    //qq登录
    @FormUrlEncoded
    @POST("public/qqlogin")
    Observable<String> qqLogin(@FieldMap Map<String,String> params);

    //wx登录
    @FormUrlEncoded
    @POST("public/wxlogin")
    Observable<String> wxLogin(@FieldMap Map<String,String> params);
}
