package com.atgc.cotton.retrofit;



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
    String API_SERVER_URL = "https://api.hmg66.com/v1/";



    //加载天气
    @GET("adat/sk/{cityId}.html")
    Observable<String> loadDataByRetrofitRxjava(@Path("cityId") String cityId);

    //手机号注册
    @FormUrlEncoded
    @POST("public/register")
    Observable<String> phoneLogin(@FieldMap Map<String,String> params);

}
