package com.atgc.cotton.presenter;

import com.atgc.cotton.presenter.view.ISingleView;
import com.atgc.cotton.retrofit.MyObserver;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liw on 2017/7/26.
 */

public class GoodsEvaluatePresenter extends BasePresenter<ISingleView> {
    public GoodsEvaluatePresenter(ISingleView mvpView) {
        super(mvpView);
    }


    /**
     * 查询评价
     * @param token
     * @param page
     * @param size
     */
    public void searchEvaluate(String token,int id,int page,int size) {
        mvpView.showLoading();
        addSubscription(api.searchEvaluate(token,id,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String model) {
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
