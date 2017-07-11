package com.atgc.cotton.presenter.view;

import com.atgc.cotton.entity.VendGoodsEntity;

import java.util.List;

/**
 * Created by GMARUnity on 2017/7/10.
 */
public interface IVendRackView extends IBaseView {

    void getMyVendGoods(List<VendGoodsEntity.Goods> entity);

    void deleMyGoodsSuccess();

    void onError(String msg);

}
