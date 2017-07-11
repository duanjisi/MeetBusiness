package com.atgc.cotton.presenter.view;

import com.atgc.cotton.entity.GoodsDetailEntity;

/**
 * Created by GMARUnity on 2017/7/10.
 */
public interface IVendUploadView extends IBaseView{
    void upLoadSccess();
    void getGoodsDetail(GoodsDetailEntity.DataBean dataBean);
    void changeGoodsSuccess();
    void onError(String msg);
}
