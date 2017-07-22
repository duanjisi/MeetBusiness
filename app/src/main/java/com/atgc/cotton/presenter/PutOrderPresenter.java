package com.atgc.cotton.presenter;

import com.atgc.cotton.presenter.view.IPutOrderView;
import com.atgc.cotton.presenter.view.ISingleView;
import com.atgc.cotton.retrofit.MyObserver;
import com.atgc.cotton.util.L;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liw on 2017/7/14.
 */

public class PutOrderPresenter extends BasePresenter<ISingleView> {

    public PutOrderPresenter(ISingleView mvpView) {
        super(mvpView);
    }

    public void order(String token,Map<String, String> map) {
        mvpView.showLoading();
        addSubscription(api.order(token,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String model) {
                        L.i(model);
                        mvpView.hideLoading();
                        mvpView.onSuccess(model);
                    }

                    @Override
                    public void onError_(String msg) {
                        mvpView.hideLoading();
                        mvpView.onError(msg);
                    }

                    @Override
                    public void onCompleted_() {

                    }

                }));
    }
}
