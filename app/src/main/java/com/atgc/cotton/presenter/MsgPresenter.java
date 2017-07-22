package com.atgc.cotton.presenter;

import com.atgc.cotton.presenter.view.IMsgView;
import com.atgc.cotton.retrofit.MyObserver;
import com.atgc.cotton.util.L;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liw on 2017/7/22.
 */

public class MsgPresenter extends BasePresenter<IMsgView> {
    public MsgPresenter(IMsgView mvpView) {
        super(mvpView);
    }

    /**
     * 查询消息
     * @param token
     * @param map
     */
    public void order(String token,int page,int size) {
        mvpView.showLoading();
        addSubscription(api.searchMsg(token,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String model) {
                        mvpView.hideLoading();
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

