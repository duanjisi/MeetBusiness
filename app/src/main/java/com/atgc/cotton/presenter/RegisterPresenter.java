package com.atgc.cotton.presenter;

import com.atgc.cotton.presenter.view.IRegisterView;
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


    public void login(Map<String,String> map) {
        mvpView.showLoading();
        addSubscription(api.phoneLogin(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String model) {
                        L.i(model);
                        mvpView.hideLoading();
                        mvpView.getCodeSucceed();
                    }

                    @Override
                    public void onError_(String msg) {
                        mvpView.hideLoading();
                    }

                    @Override
                    public void onCompleted_() {

                    }

                }));
    }

}
