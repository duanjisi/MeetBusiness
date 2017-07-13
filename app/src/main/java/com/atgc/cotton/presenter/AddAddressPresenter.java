package com.atgc.cotton.presenter;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.presenter.view.IBaseView;
import com.atgc.cotton.presenter.view.INormalView;
import com.atgc.cotton.retrofit.MyObserver;
import com.atgc.cotton.util.L;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liw on 2017/7/12.
 */

public class AddAddressPresenter extends BasePresenter<INormalView> {
    public AddAddressPresenter(INormalView mvpView) {
        super(mvpView);
    }

    /**
     * 保存地址
     * @param token
     * @param map
     */
    public void addAddress(String token,Map<String,String> map) {
        mvpView.showLoading();
        addSubscription(api.addAddress(token,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String model) {
                        mvpView.hideLoading();
                        BaseResult result = JSON.parseObject(model, BaseResult.class);
                        if(result!=null){
                            if(result.getCode()==0){
                                mvpView.getDataSuccess(result.getMessage());
                            }else{
                                mvpView.getDataFail();
                            }

                        }else{
                            mvpView.getDataFail();
                        }

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

    /**
     * 编辑地址
     * @param token
     * @param id
     * @param map
     */
    public void editAddress(String token,int id,Map<String,String> map) {
        mvpView.showLoading();
        addSubscription(api.editAddress(token,id,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String model) {
                        mvpView.hideLoading();
                        BaseResult result = JSON.parseObject(model, BaseResult.class);
                        if(result!=null){
                            if(result.getCode()==0){
                                mvpView.getDataSuccess(result.getMessage());
                            }else{
                                mvpView.getDataFail();
                            }

                        }else{
                            mvpView.getDataFail();
                        }

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
