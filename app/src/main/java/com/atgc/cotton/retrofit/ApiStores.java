package com.atgc.cotton.retrofit;



import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 *  Created by liw on 2017/7/5.
 */
public interface ApiStores {
    //baseUrl
    String API_SERVER_URL = "http://www.weather.com.cn/";



    //加载天气
    @GET("adat/sk/{cityId}.html")
    Observable<String> loadDataByRetrofitRxjava(@Path("cityId") String cityId);
}
