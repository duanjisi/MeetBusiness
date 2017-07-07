package com.atgc.cotton.presenter;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;

/**
 * 处理网络请求结果的基类
 * Created by liw on 2017/7/5.
 */

public abstract class MyObserver<T> implements Observer<T>{

    @Override
    public void onCompleted() {
        onCompleted_();
    }

    @Override
    public void onError(Throwable e) {
        //处理错误信息
        e.printStackTrace();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            String msg = httpException.getMessage();
            if (code == 504) {
                msg = "网络不给力";
            }
            if (code == 502 || code == 404) {
                msg = "服务器异常，请稍后再试";
            }
            onError_(msg);
        } else {
            onError_(e.getMessage());
        }
        onCompleted_();
    }

    @Override
    public void onNext(T model) {
        onNext_(model);
    }


    public abstract void onNext_(T model);

    public abstract void onError_(String msg);

    public abstract void onCompleted_();
}
