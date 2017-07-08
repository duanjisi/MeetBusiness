package com.atgc.cotton.presenter;

import android.content.Context;

import com.atgc.cotton.presenter.view.INormalView;
import com.atgc.cotton.util.L;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 请求接口，然后通过view来显示隐藏加载框
 * Created by liw on 2017/7/5.
 */
public class GoodsDetailPresenter extends BasePresenter<INormalView>{


    public GoodsDetailPresenter(INormalView mvpView ) {
        super(mvpView);
    }

    public void loadDataByRetrofitRxjava(String cityId) {
        mvpView.showLoading();
        addSubscription(api.loadDataByRetrofitRxjava(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String model) {
                        L.i(model);
                        mvpView.hideLoading();
                        mvpView.getDataSuccess(model);
                    }

                    @Override
                    public void onError_(String msg) {
                        mvpView.hideLoading();
                        mvpView.getDataFail();
                    }

                    @Override
                    public void onCompleted_() {

                    }

                }));
    }

}
