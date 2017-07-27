package com.atgc.cotton.presenter;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.entity.GoodsDetailEntity;
import com.atgc.cotton.presenter.view.IBaseView;
import com.atgc.cotton.presenter.view.IGoodsDetailView;
import com.atgc.cotton.presenter.view.INormalView;
import com.atgc.cotton.retrofit.MyObserver;
import com.atgc.cotton.util.L;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 请求接口，然后通过view来显示隐藏加载框
 * Created by liw on 2017/7/5.
 */
public class GoodsDetailPresenter extends BasePresenter<IGoodsDetailView>{


    public GoodsDetailPresenter(IGoodsDetailView mvpView ) {
        super(mvpView);
    }

    public void getGoodsDetail(int id) {
        mvpView.showLoading();
        addSubscription(api.getGoodsDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String model) {
                        mvpView.hideLoading();
                        GoodsDetailEntity entity = JSON.parseObject(model, GoodsDetailEntity.class);
                        if(entity!=null){
                            GoodsDetailEntity.DataBean bean = entity.getData();
                            mvpView.getGoodsSuccess(bean);
                        }
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
                        mvpView.getEvaluteSuccess(model);
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
