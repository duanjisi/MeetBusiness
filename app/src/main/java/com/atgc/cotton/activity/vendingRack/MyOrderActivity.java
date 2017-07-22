package com.atgc.cotton.activity.vendingRack;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseCompatActivity;
import com.atgc.cotton.entity.MyOrderEntity;
import com.atgc.cotton.entity.OrderActionEntity;
import com.atgc.cotton.entity.OrderGoodsEntity;
import com.atgc.cotton.fragment.BaseOrderFragment;
import com.atgc.cotton.fragment.OrderAllFragment;
import com.atgc.cotton.fragment.OrderEvaluateFragment;
import com.atgc.cotton.fragment.OrderObligationFragment;
import com.atgc.cotton.fragment.OrderReceivingFragment;
import com.atgc.cotton.fragment.OrderSellerAllFragment;
import com.atgc.cotton.fragment.OrderSellerObligationFragment;
import com.atgc.cotton.fragment.OrderSellerRefundFragment;
import com.atgc.cotton.fragment.OrderSellerShipmentsFragment;
import com.atgc.cotton.fragment.OrderShipmentsFragment;
import com.atgc.cotton.presenter.MyOrderPresenter;
import com.atgc.cotton.presenter.view.IMyOrderView;
import com.atgc.cotton.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by GMARUnity on 2017/6/28.
 */
public class MyOrderActivity extends BaseCompatActivity<MyOrderPresenter> implements View.OnClickListener, IMyOrderView {

    private Toolbar toolbar;
    private TextView tv_back, tv_buyer, tv_seller;
    //private TabLayout tablayout,tablayout_1;
    private ViewPager viewpager, viewpager_1;
    private ArrayList<BaseOrderFragment> mFragments = new ArrayList<>();
    private ArrayList<BaseOrderFragment> mTwoFragments = new ArrayList<>();
    private BaseOrderFragment allFragment, obligationFragment, shipmentsFragment, receivingFragment, evaluateFragment;
    private String[] titles, twotitles;
    private PagerTwoAdapter pagerTwoAdapter;
    private List<Integer> isAddOneList, isAddTwoList;
    private int curPos = 0, curTwoPos = 0;
    private Dialog deleteDialog;
    private int orderId;
    private boolean isBuy, isDelete = false;
    private Map<String, String> operateMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    @Override
    protected MyOrderPresenter createPresenter() {
        return new MyOrderPresenter(this);
    }

    private void initView() {
        isAddOneList = new ArrayList<>();
        isAddTwoList = new ArrayList<>();
        isAddOneList.add(0);
        tv_seller = (TextView) findViewById(R.id.tv_seller);
        tv_buyer = (TextView) findViewById(R.id.tv_buyer);
        viewpager_1 = (ViewPager) findViewById(R.id.viewpager_1);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //tablayout = (TabLayout) findViewById(R.id.tablayout);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewpager.setOffscreenPageLimit(5);
        viewpager.setCurrentItem(0);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("viewpager", "onPageScrolled:" + position);
            }

            @Override
            public void onPageSelected(int position) {
                curPos = position;
                if (!isAddOneList.contains(position)) {
                    isAddOneList.add(position);
                    if (position == 4) {
                        mPresenter.getMyBuyEvaluateOrder(1, 20);
                    } else {
                        mPresenter.getMyBuyOrder(position + 1, 1, 20);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("viewpager", "onPageChanged:" + state);
            }
        });
        viewpager_1.setOffscreenPageLimit(4);
        viewpager_1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curTwoPos = position;
                if (!isAddTwoList.contains(position)) {
                    isAddTwoList.add(position);
                    mPresenter.getMySellOrder(position + 1, 1, 20);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tv_seller.setOnClickListener(this);
        tv_buyer.setOnClickListener(this);
        tv_buyer.setSelected(true);
    }

    private void initData() {
        twotitles = new String[]{"全部", "待买家付款", "待发货", "待退款"};
        titles = new String[]{"全部", "待付款", "待发货", "待收货", "待评价"};
        allFragment = new OrderAllFragment();
        obligationFragment = new OrderObligationFragment();
        shipmentsFragment = new OrderShipmentsFragment();
        receivingFragment = new OrderReceivingFragment();
        evaluateFragment = new OrderEvaluateFragment();
        mFragments.add(allFragment);
        mFragments.add(obligationFragment);
        mFragments.add(shipmentsFragment);
        mFragments.add(receivingFragment);
        mFragments.add(evaluateFragment);

        BaseOrderFragment orderSellerAllFragment = new OrderSellerAllFragment();
        BaseOrderFragment orderSellerObligationFragment = new OrderSellerObligationFragment();
        BaseOrderFragment orderSellerShipmentsFragment = new OrderSellerShipmentsFragment();
        BaseOrderFragment orderSellerRefundFragment = new OrderSellerRefundFragment();
        mTwoFragments.add(orderSellerAllFragment);
        mTwoFragments.add(orderSellerObligationFragment);
        mTwoFragments.add(orderSellerShipmentsFragment);
        mTwoFragments.add(orderSellerRefundFragment);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        mPresenter.getMyBuyOrder(1, 1, 20);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_seller:
                if (pagerTwoAdapter == null) {
                    pagerTwoAdapter = new PagerTwoAdapter(getSupportFragmentManager());
                    viewpager_1.setAdapter(pagerTwoAdapter);
                }
                viewpager.setVisibility(View.GONE);
                viewpager_1.setVisibility(View.VISIBLE);
                tv_buyer.setSelected(false);
                tv_seller.setSelected(true);
                if (!isAddTwoList.contains(0)) {
                    isAddTwoList.add(0);
                    mPresenter.getMySellOrder(1, 1, 20);
                }
                break;
            case R.id.tv_buyer:
                viewpager.setVisibility(View.VISIBLE);
                viewpager_1.setVisibility(View.GONE);
                tv_buyer.setSelected(true);
                tv_seller.setSelected(false);
                break;
            case R.id.bt_delete:
                mPresenter.deleteMyBuyOrder(orderId);
                break;
        }
    }

    @Override
    public void getMyOrderSuccess(boolean isBuy, List<MyOrderEntity.DataBean> list) {
        updateFragmentPage(isBuy, list);
    }

    @Override
    public void getMyEvaluateOrderSuccess(List<OrderGoodsEntity> list) {
        if (evaluateFragment != null) {
            ((OrderEvaluateFragment) evaluateFragment).getData(list);
        }
    }

    @Override
    public void deleteOrderSuccess() {
        isDelete = true;
        if (isBuy) {
            mPresenter.getMyBuyOrder(curPos + 1, 1, 20);
        } else {
            mPresenter.getMySellOrder(curTwoPos + 1, 1, 20);
        }
    }

    @Override
    public void onError(String msg) {

    }

    private void updateFragmentPage(boolean isBuy, List<MyOrderEntity.DataBean> list) {
        if (list == null) {
            showToast("没有更多数据", false);
            return;
        }
        int parentSize = list.size();
        List<OrderGoodsEntity> goodsEntityList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            MyOrderEntity.DataBean pater = list.get(i);
            if (pater != null) {
                String orderState = pater.getOrdState();
                OrderGoodsEntity entity = new OrderGoodsEntity();
                entity.setOrdState(orderState);
                entity.setContentType(0);
                entity.setStoreName(pater.getSupplierName());
                entity.setPayStatus(pater.getPayStatus());
                entity.setOrderStatus(pater.getOrderStatus());
                entity.setShippingStatus(pater.getShippingStatus());
                goodsEntityList.add(entity);
                List<OrderGoodsEntity> GoodsList = pater.getGoodsList();
                goodsEntityList.addAll(GoodsList);
                int allNum = 0;
                for (OrderGoodsEntity goodsEntity : GoodsList) {
                    goodsEntity.setContentType(1);
                    goodsEntity.setOrdState(orderState);
                    allNum = allNum + goodsEntity.getBuyNumber();
                }
                OrderGoodsEntity endEntity = new OrderGoodsEntity();
                endEntity.setOrderId(pater.getOrderId());
                endEntity.setOrdState(orderState);
                endEntity.setContentType(2);
                endEntity.setAllNum(allNum);
                endEntity.setOrderAmount(pater.getOrderAmount());
                endEntity.setPayStatus(pater.getPayStatus());
                endEntity.setOrderStatus(pater.getOrderStatus());
                endEntity.setShippingStatus(pater.getShippingStatus());
                goodsEntityList.add(endEntity);
            }
        }
        if (isBuy) {
            switch (curPos) {
                case 0:
                    if (isDelete) {
                        isDelete = false;
                        ((OrderAllFragment) mFragments.get(curPos)).setPage(2);
                    }
                    ((OrderAllFragment) mFragments.get(curPos)).getData(goodsEntityList, parentSize);
                    break;
                case 1:
                    if (isDelete) {
                        isDelete = false;
                        ((OrderObligationFragment) mFragments.get(curPos)).setPage(2);
                    }
                    ((OrderObligationFragment) mFragments.get(curPos)).getData(goodsEntityList, parentSize);
                    break;
                case 2:
                    if (isDelete) {
                        isDelete = false;
                        ((OrderShipmentsFragment) mFragments.get(curPos)).setPage(2);
                    }
                    ((OrderShipmentsFragment) mFragments.get(curPos)).getData(goodsEntityList, parentSize);
                    break;
                case 3:
                    if (isDelete) {
                        isDelete = false;
                        ((OrderReceivingFragment) mFragments.get(curPos)).setPage(2);
                    }
                    ((OrderReceivingFragment) mFragments.get(curPos)).getData(goodsEntityList, parentSize);
                    break;
                case 4:
                    break;
            }
        } else {
            switch (curTwoPos) {
                case 0:
                    if (isDelete) {
                        isDelete = false;
                        ((OrderSellerAllFragment) mFragments.get(curPos)).setPage(2);
                    }
                    ((OrderSellerAllFragment) mTwoFragments.get(curTwoPos)).getData(goodsEntityList, parentSize);
                    break;
                case 1:
                    if (isDelete) {
                        isDelete = false;
                        ((OrderSellerObligationFragment) mFragments.get(curPos)).setPage(2);
                    }
                    ((OrderSellerObligationFragment) mTwoFragments.get(curTwoPos)).getData(goodsEntityList, parentSize);
                    break;
                case 2:
                    if (isDelete) {
                        isDelete = false;
                        ((OrderSellerShipmentsFragment) mFragments.get(curPos)).setPage(2);
                    }
                    ((OrderSellerShipmentsFragment) mTwoFragments.get(curTwoPos)).getData(goodsEntityList, parentSize);
                    break;
                case 3:
                    if (isDelete) {
                        isDelete = false;
                        ((OrderSellerRefundFragment) mFragments.get(curPos)).setPage(2);
                    }
                    ((OrderSellerRefundFragment) mTwoFragments.get(curTwoPos)).getData(goodsEntityList, parentSize);
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getChildAction(OrderActionEntity entity) {
        if (entity != null) {
            String action = entity.getDoAction();
            orderId = entity.getOrderid();
            isBuy = entity.isBuy();
            if (entity.getChild() == 4) {//订单评价
                mPresenter.getMyBuyEvaluateOrder(entity.getPage(), 20);
            } else {
                if (!TextUtils.isEmpty(action)) {
                    switch (action) {
                        case "delete"://删除订单
                            showDeleteDialog();
                            break;
                        case "cancelOrder"://取消订单
                            if (operateMap == null) {
                                operateMap = new HashMap<>();
                            } else {
                                operateMap.clear();
                            }
                            operateMap.put("orderid", String.valueOf(orderId));
                            operateMap.put("type", "cancel");
                            mPresenter.operateMyOrder(operateMap);
                            break;
                        case "pay"://付款

                            break;
                        case "confirmGoods"://确认收货
                            if (operateMap == null) {
                                operateMap = new HashMap<>();
                            } else {
                                operateMap.clear();
                            }
                            operateMap.put("orderid", String.valueOf(orderId));
                            operateMap.put("type", "cancel");
                            mPresenter.operateMyOrder(operateMap);
                            break;
                        case "OnLoadMore"://加载更多
                        case "OnRefresh"://刷新
                            if (entity.isBuy()) {
                                mPresenter.getMyBuyOrder(entity.getChild() + 1, entity.getPage(), 20);
                            } else {
                                mPresenter.getMySellOrder(entity.getChild() + 1, entity.getPage(), 20);
                            }
                            break;
                    }
                }
            }
        }

    }

    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    class PagerTwoAdapter extends FragmentPagerAdapter {

        public PagerTwoAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return mTwoFragments.get(arg0);
        }

        @Override
        public int getCount() {
            return mTwoFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return twotitles[position];
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            if (evaluateFragment != null) {
                ((OrderEvaluateFragment) evaluateFragment).setPage(1);
                mPresenter.getMyBuyEvaluateOrder(1, 20);
            }
        }
    }

    private void showDeleteDialog() {
        if (deleteDialog == null) {
            View view = LayoutInflater.from(this).inflate(
                    R.layout.dialog_delete_order, null);
            Button bt_close = (Button) view.findViewById(R.id.bt_close);
            Button bt_delete = (Button) view.findViewById(R.id.bt_delete);
            bt_close.setOnClickListener(this);
            bt_delete.setOnClickListener(this);
            deleteDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
            deleteDialog.setContentView(view);
            Window dialogWindow = deleteDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            int sceenW = UIUtils.getScreenWidth(this);
            lp.width = sceenW / 3 * 2;
            dialogWindow.setAttributes(lp);
            deleteDialog.setCanceledOnTouchOutside(false);
        }
        deleteDialog.show();
    }
}
