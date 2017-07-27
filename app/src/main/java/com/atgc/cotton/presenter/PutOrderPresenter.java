package com.atgc.cotton.presenter;

import com.atgc.cotton.presenter.view.IPutOrderView;
import com.atgc.cotton.presenter.view.ISingleView;
import com.atgc.cotton.retrofit.MyObserver;
import com.atgc.cotton.util.L;

import java.util.HashMap;
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

    /**
     * 下订单
     * @param token
     * @param map
     */
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

    /**
     * 生成支付宝支付所需要的已签名的订单信息
     * @param token
     * @param orderid
     */
    public void alipay(String token,int orderid) {
        mvpView.showLoading();
        Map<String,String> map  = new HashMap<>();
        map.put("1","1");
        addSubscription(api.alipay(token,orderid,map)
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
