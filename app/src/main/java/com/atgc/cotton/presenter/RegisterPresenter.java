package com.atgc.cotton.presenter;

import com.atgc.cotton.presenter.view.IRegisterView;
import com.atgc.cotton.retrofit.MyObserver;
import com.atgc.cotton.util.L;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liw on 2017/7/7.
 */
public class RegisterPresenter extends BasePresenter<IRegisterView> {


    public RegisterPresenter(IRegisterView mvpView) {
        super(mvpView);
    }

    public void register(Map<String, String> map) {
        mvpView.showLoading();
        addSubscription(api.phoneRegister(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String model) {
                        L.i(model);
                        mvpView.hideLoading();
                        mvpView.loginSucceed(model);
                    }

                    @Override
                    public void onError_(String msg) {
                        mvpView.hideLoading();
                        mvpView.applyFailure(msg);
                    }

                    @Override
                    public void onCompleted_() {

                    }

                }));
    }

    public void sendCode(Map<String, String> map) {
        mvpView.showLoading();
        addSubscription(api.sendCode(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String model) {
                        L.i(model);
                        mvpView.hideLoading();
                        mvpView.getCodeSucceed(model);
                    }

                    @Override
                    public void onError_(String msg) {
                        mvpView.hideLoading();
                        mvpView.applyFailure(msg);
                    }

                    @Override
                    public void onCompleted_() {

                    }

                }));
    }

}
