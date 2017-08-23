package com.atgc.cotton.presenter.view;

import com.atgc.cotton.entity.AlipayOrder;
import com.atgc.cotton.entity.MyOrderEntity;
import com.atgc.cotton.entity.OrderGoodsEntity;
import com.atgc.cotton.entity.PayWx;

import java.util.List;

/**
 * Created by GMARUnity on 2017/7/14.
 */
public interface IMyOrderView extends IBaseView {
    void getMyOrderSuccess(boolean isBuy, List<MyOrderEntity.DataBean> list);

    void getMyEvaluateOrderSuccess(List<OrderGoodsEntity> lsit);

    void deleteOrderSuccess();

    void onError(String msg);

    void alipaySuccess(AlipayOrder order);

    void wxpaySuccess(PayWx order);
}
