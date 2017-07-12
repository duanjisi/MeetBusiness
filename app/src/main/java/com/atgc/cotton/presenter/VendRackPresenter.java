package com.atgc.cotton.presenter;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.entity.UserEntity;
import com.atgc.cotton.entity.VendGoodsEntity;
import com.atgc.cotton.presenter.view.INormalView;
import com.atgc.cotton.presenter.view.IVendRackView;
import com.atgc.cotton.retrofit.MyObserver;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by GMARUnity on 2017/7/10.
 */
public class VendRackPresenter extends BasePresenter<IVendRackView> {
    public VendRackPresenter(IVendRackView mvpView) {
        super(mvpView);
    }

    /**
     * 获取我的售货架列表
     *
     * @param page,size
     */
    public void getMyVendGoods(int page, int size) {
        mvpView.showLoading();
        addSubscription(api.getMyVendGoods(App.getInstance().getToken(), page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        VendGoodsEntity entity = JSON.parseObject(s, VendGoodsEntity.class);
                        if (entity.getCode() == 0 && entity != null && entity.getData() != null) {
                            mvpView.getMyVendGoods(entity.getData());
                        } else {
                            mvpView.onError(entity.getMessage());
                        }
                        mvpView.hideLoading();
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
     * 删除商品
     *
     * @param goodsid
     */
    public void deleteMyVendGoods(String goodsid) {
        mvpView.showLoading();
        addSubscription(api.deleteMyVendGoods(App.getInstance().getToken(), goodsid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        BaseResult baseResult = BaseResult.parse(s);
                        if (baseResult.getCode() == 0 && baseResult.getStatus() == 200) {
                            mvpView.deleMyGoodsSuccess();
                        } else {
                            mvpView.onError(baseResult.getMessage());
                        }
                        mvpView.hideLoading();
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
