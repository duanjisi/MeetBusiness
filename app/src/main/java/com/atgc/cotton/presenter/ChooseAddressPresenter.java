package com.atgc.cotton.presenter;

import com.atgc.cotton.presenter.view.IChooseAddressView;
import com.atgc.cotton.retrofit.MyObserver;
import com.atgc.cotton.util.L;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liw on 2017/7/12.
 */

public class ChooseAddressPresenter extends BasePresenter<IChooseAddressView> {
    public ChooseAddressPresenter(IChooseAddressView mvpView) {
        super(mvpView);
    }

    /**
     * 查询地址列表
     * @param token
     * @param page
     * @param size
     */
    public void searchAddress(String token,int page,int size) {
        mvpView.showLoading();
        addSubscription(api.searchAddress(token,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String model) {
                        mvpView.hideLoading();
                        mvpView.searchAddreshSuccess(model);
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

    /**
     * 删除地址
     * @param token
     * @param id
     */
    public void deleteAddress(String token,int id) {
        mvpView.showLoading();
        addSubscription(api.deleteAddress(token,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String model) {
                        mvpView.hideLoading();
                        mvpView.deleteSuccess();
                    }

                    @Override
                    public void onError_(String msg) {
                        mvpView.hideLoading();
                        mvpView.deleteFailue();
                    }

                    @Override
                    public void onCompleted_() {

                    }

                }));
    }

}
