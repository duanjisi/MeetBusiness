package com.atgc.cotton.retrofit;


import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;

/**
 * Created by liw on 2017/7/5.
 */
public interface ApiStores {
    //baseUrl
    String API_SERVER_URL = "https://yuetao.66boss.com/v1/";


    //加载天气
    @GET("adat/sk/{cityId}.html")
    Observable<String> loadDataByRetrofitRxjava(@Path("cityId") String cityId);

    //手机号注册
    @FormUrlEncoded
    @POST("public/register")
    Observable<String> phoneRegister(@FieldMap Map<String, String> params);

    //手机号登录
    @FormUrlEncoded
    @POST("public/login")
    Observable<String> phoneLogin(@FieldMap Map<String, String> params);

    //qq登录
    @FormUrlEncoded
    @POST("public/qqlogin")
    Observable<String> qqLogin(@FieldMap Map<String, String> params);

    //wx登录
    @FormUrlEncoded
    @POST("public/wxlogin")
    Observable<String> wxLogin(@FieldMap Map<String, String> params);

    //获取我的售货架列表
    @GET("goods/mine")
    Observable<String> getMyVendGoods(@Header("Authorization") String token, @Query("page ") int page, @Query("size ") int size);

    //删除商品
    @DELETE("goods/{goodsid}")
    Observable<String> deleteMyVendGoods(@Header("Authorization") String token, @Path("goodsid") String goodsid);

    //上传商品
    @Multipart
    @POST("goods/")
    Observable<String> uploadMyVendGoods(@Header("Authorization") String token, @PartMap Map<String, RequestBody> params);

    //获取商品详情
    @GET("public/goods/{id}")
    Observable<String> getGoodsDetail(@Path("id") int id);

    //获取商品详情
    @Multipart
    @PUT("goods/{goodsid}")
    Observable<String> changeMyGoods(@Header("Authorization") String token, @Path("goodsid") int goodsid, @PartMap Map<String, RequestBody> params);
}
