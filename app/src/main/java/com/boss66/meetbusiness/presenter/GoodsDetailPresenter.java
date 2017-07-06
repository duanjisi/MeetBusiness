package com.boss66.meetbusiness.presenter;

import android.util.Log;

import com.boss66.meetbusiness.presenter.view.IGoodsDetailView;
import com.boss66.meetbusiness.util.L;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 请求接口，然后通过view来显示隐藏加载框
 * Created by liw on 2017/7/5.
 */
public class GoodsDetailPresenter extends BasePresenter<IGoodsDetailView>{

    public GoodsDetailPresenter(IGoodsDetailView view) {
        attachView(view);
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
