package com.boss66.meetbusiness.fragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.boss66.meetbusiness.activity.vendingRack.LogisticsActivity;
import com.boss66.meetbusiness.adapter.OrderAdapter;
import com.boss66.meetbusiness.entity.OrderEntity;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMARUnity on 2017/6/28.
 * 待付款
 */
public class OrderObligationFragment extends BaseOrderFragment {
    private OrderAdapter orderAdapter;
    private int postion;

    @Override
    protected void setAdapterAndDecor(LRecyclerView list) {
        setHasOptionsMenu(true);
        orderAdapter = new OrderAdapter(getActivity());
        List<OrderEntity> dataList = new ArrayList<>();
        setData(dataList);
        orderAdapter.setDatas(dataList);
        orderAdapter.setOnBtnListener(new OrderAdapter.onBtnListener() {
            @Override
            public void onOrdBtn1(OrderEntity orderEntity) {
                btnToDo(orderEntity, 1);
            }

            @Override
            public void onOrdBtn2(OrderEntity orderEntity) {
                btnToDo(orderEntity, 2);
            }

            @Override
            public void onOrdBtn3(OrderEntity orderEntity, int pos) {
                btnToDo(orderEntity, 3);
                postion = pos;
            }
        });
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(orderAdapter);
        list.setAdapter(mLRecyclerViewAdapter);
    }

    private void setData(List<OrderEntity> dataList) {
        for (int i = 0; i < 12; i++) {
            OrderEntity item = new OrderEntity();
            if (i % 4 == 0) {
                item.setContentType(0);
                item.setShopName("店铺：" + i);
                item.setHeadTag(i);
            } else if (i % 4 == 3) {
                item.setContentType(2);
                item.setAllNum(2);
                item.setFootTag(i);
            } else {
                item.setGoodsNum("" + (i + 1));
                int price = 5 + i % 2;
                item.setGoodsPrice(String.valueOf(price));
                int allprice = price * (i + 1);
                item.setAllPrice(String.valueOf(allprice));
                item.setContentType(1);
            }
            item.setOrderType(0);
            item.setGoodsName("商品：" + i);
            item.setGoodsContent("商品介绍：的顶顶顶顶多多多多多多多多多过过过过个二二二啊啊啊啊啊啊啊啊啊" + i);
            dataList.add(item);
        }
    }

    private void btnToDo(OrderEntity entity, int type) {
        int orderType = entity.getOrderType();
        switch (orderType) {//"0:待付款", "1:待发货", "2:待收货", "3:待评价"
            case 0:
                if (type == 1) {
                    Log.i("OrderEntity:", "付款");
                } else if (type == 2) {
                    Log.i("OrderEntity:", "取消订单");
                }
                break;
//            case 1:
//                break;
            case 2:
                if (type == 1) {
                    Log.i("OrderEntity:", "确认收货");
                } else if (type == 2) {
                    Log.i("OrderEntity:", "查看物流LogisticsActivity");
                    openActivity(LogisticsActivity.class);
                }
                break;
            case 3:
                if (type == 1) {
                    Log.i("OrderEntity:", "评价");
                } else if (type == 2) {
                    Log.i("OrderEntity:", "查看物流");
                    openActivity(LogisticsActivity.class);
                } else {
                    Log.i("OrderEntity:", "删除订单");
                }
                break;
        }
    }
}
