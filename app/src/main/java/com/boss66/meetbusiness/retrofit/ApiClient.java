package com.boss66.meetbusiness.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * C Created by liw on 2017/7/5.
 */
public class ApiClient {
    public static Retrofit mRetrofit;

    public static Retrofit retrofit() {
        if (mRetrofit == null) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            if (true) {
                // Log信息拦截器       发布时改为false 或者 Level.NONE
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置 Debug Log 模式
                builder.addInterceptor(loggingInterceptor);
            }
            //加缓存的话再给okhttpClient设置
            OkHttpClient okHttpClient = builder.build();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(ApiStores.API_SERVER_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit;
    }


}
