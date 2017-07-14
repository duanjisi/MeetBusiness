package com.atgc.cotton.presenter.view;

import com.atgc.cotton.entity.GoodsDetailEntity;

/**
 * Created by liw on 2017/7/11.
 */

public interface IGoodsDetailView extends IBaseView {

    void getGoodsSuccess(GoodsDetailEntity.DataBean  bean);
}
