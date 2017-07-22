package com.atgc.cotton.presenter;

import com.atgc.cotton.presenter.view.IRegisterView;
import com.atgc.cotton.presenter.view.IResetView;
import com.atgc.cotton.presenter.view.ISingleView;
import com.atgc.cotton.retrofit.MyObserver;
import com.atgc.cotton.util.L;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liw on 2017/7/19.
 */

public class ResetPwsPresenter extends BasePresenter<IResetView> {
    public ResetPwsPresenter(IResetView mvpView) {
        super(mvpView);
    }

    /**
     * 重置密码
     * @param map
     */
    public void resetPsw(Map<String, String> map) {
        mvpView.showLoading();
        addSubscription(api.resetPsw(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        mvpView.resetPswSucess(s);
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
     * 发送找回密码验证码
      * @param map
     */
    public void sendRestCode(final Map<String, String> map) {
        mvpView.showLoading();
        addSubscription(api.sendRestCode(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        mvpView.getCodeSucess(s);
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
