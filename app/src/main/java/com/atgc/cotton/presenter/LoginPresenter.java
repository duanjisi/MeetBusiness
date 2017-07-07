package com.atgc.cotton.presenter;

import com.atgc.cotton.presenter.view.INormalView;
import com.atgc.cotton.util.L;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liw on 2017/7/6.
 */
public class LoginPresenter extends BasePresenter<INormalView> {


    public LoginPresenter(INormalView mvpView) {
        super(mvpView);
    }


    public void login(Map<String,String> map) {
        mvpView.showLoading();
        addSubscription(api.phoneLogin(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        L.i(s);
                        mvpView.hideLoading();
                        mvpView.getDataSuccess(s);
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
