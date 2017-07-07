package com.atgc.cotton.presenter;


import com.atgc.cotton.retrofit.ApiClient;
import com.atgc.cotton.retrofit.ApiStores;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 *
 * Created by liw on 2017/7/5.
 */

public class BasePresenter<V> implements IBasePresenter {
    protected V mvpView;
    private CompositeSubscription mCompositeSubscription;

    protected ApiStores api;

    public BasePresenter(V mvpView) {
        this.mvpView = mvpView;
        attachView(mvpView);
    }

    public void attachView(V mvpView) {
        this.mvpView = mvpView;
        api = ApiClient.retrofit().create(ApiStores.class);
    }

    public void detachView() {
        this.mvpView = null;
        unsubcrible();
    }


    /**
     * 事件订阅
     * */
    protected void addSubscription(Subscription s){
        if(this.mCompositeSubscription==null){
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    @Override
    public void unsubcrible() {
        if(this.mCompositeSubscription!=null){
            this.mCompositeSubscription.unsubscribe();
        }

    }
}
