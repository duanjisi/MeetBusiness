package com.atgc.cotton.fragment;

import android.os.Handler;
import android.util.Log;

import com.atgc.cotton.activity.vendingRack.LogisticsActivity;
import com.atgc.cotton.adapter.OrderAdapter;
import com.atgc.cotton.entity.OrderActionEntity;
import com.atgc.cotton.entity.OrderGoodsEntity;
import com.atgc.cotton.util.ToastUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by GMARUnity on 2017/6/28.
 */
public class OrderAllFragment extends BaseOrderFragment {
    private List<OrderGoodsEntity> dataList;
    private int page = 1;
    private LRecyclerView rv_content;
    private boolean isOnRefresh;
    private OrderActionEntity orderActionEntity;
    private OrderAdapter orderAdapter;

    @Override
    protected void setAdapterAndDecor(LRecyclerView list) {
        setHasOptionsMenu(true);
        orderActionEntity = new OrderActionEntity();
        orderActionEntity.setChild(0);
        orderActionEntity.setBuy(true);
        rv_content = list;
        dataList = new ArrayList<>();
//        setData();
        orderAdapter = new OrderAdapter(getActivity(), 1);
        orderAdapter.setDatas(dataList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(orderAdapter);
        list.setAdapter(mLRecyclerViewAdapter);
        list.setLoadMoreEnabled(true);
        list.refreshComplete(20);
        rv_content.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isOnRefresh = true;
                        rv_content.refreshComplete(20);
                        orderActionEntity.setRefresh(true);
                        orderActionEntity.setDoAction("OnRefresh");
                        page = 1;
                        orderActionEntity.setPage(1);
                        EventBus.getDefault().post(orderActionEntity);
                    }
                }, 1000);
            }
        });
        rv_content.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isOnRefresh = false;
                        orderActionEntity.setRefresh(false);
                        orderActionEntity.setPage(page);
                        orderActionEntity.setDoAction("OnLoadMore");
                        EventBus.getDefault().post(orderActionEntity);
                    }
                }, 1000);
            }
        });
        orderAdapter.setOnBtnListener(new OrderAdapter.onBtnListener() {
            @Override
            public void onOrdBtn1(OrderGoodsEntity orderEntity) {
                btnToDo(orderEntity, 1);
            }

            @Override
            public void onOrdBtn2(OrderGoodsEntity orderEntity) {
                btnToDo(orderEntity, 2);
            }

            @Override
            public void onOrdBtn3(OrderGoodsEntity orderEntity, int pos) {
                btnToDo(orderEntity, 3);
            }
        });
    }

    public void getData(List<OrderGoodsEntity> list, int parentSize) {
        if (list != null) {
            if (parentSize == 20) {
                rv_content.setNoMore(false);
                page++;
            } else {
                ToastUtil.showShort(getActivity(), "没人更多数据啦~");
                rv_content.setNoMore(true);
            }
            if (!isOnRefresh) {
                orderAdapter.addAll(list);
                orderAdapter.notifyDataSetChanged();
            } else {
                orderAdapter.setDataList(list);
            }
        } else {
            ToastUtil.showShort(getActivity(), "没人更多数据啦~");
            rv_content.setNoMore(true);
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private void btnToDo(OrderGoodsEntity entity, int type) {
        orderActionEntity.setOrderid(entity.getOrderId());
        int orderType = entity.getOrderStatus();
        switch (orderType) {//"0:待付款", "1:待发货", "2:待收货", "3:待评价", "4:取消退款申请"
            case 0:
                if (type == 1) {
                    Log.i("OrderEntity:", "付款");
                    orderActionEntity.setAllPrice(entity.getOrderAmount());
                    orderActionEntity.setDoAction("pay");
                } else if (type == 2) {
                    Log.i("OrderEntity:", "取消订单");
                    orderActionEntity.setDoAction("cancelOrder");
                }
                EventBus.getDefault().post(orderActionEntity);
                break;
            case 1:
                if (type == 1) {
                    Log.i("OrderEntity:", "申请退款");
                    orderActionEntity.setDoAction("refund");
                    EventBus.getDefault().post(orderActionEntity);
                }
                break;
            case 2:
                if (type == 1) {
                    Log.i("OrderEntity:", "确认收货");
                    orderActionEntity.setDoAction("confirmGoods");
                    EventBus.getDefault().post(orderActionEntity);
                } else if (type == 2) {
                    Log.i("OrderEntity:", "查看物流");
                    openActivity(LogisticsActivity.class);
                }
                break;
            case 3:
                if (type == 1) {
                    Log.i("OrderEntity:", "删除订单");
                    orderActionEntity.setDoAction("delete");
                    EventBus.getDefault().post(orderActionEntity);
                }
                break;
            case 4:
                if (type == 1) {
                    Log.i("OrderEntity:", "取消退款申请");
                    orderActionEntity.setDoAction("cancelrefund");
                    EventBus.getDefault().post(orderActionEntity);
                }
                break;
        }
    }
}
