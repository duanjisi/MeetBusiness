package com.atgc.cotton.activity.vendingRack;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseCompatActivity;
import com.atgc.cotton.entity.AlipayOrder;
import com.atgc.cotton.entity.MyOrderEntity;
import com.atgc.cotton.entity.OrderActionEntity;
import com.atgc.cotton.entity.OrderGoodsEntity;
import com.atgc.cotton.entity.PayResult;
import com.atgc.cotton.entity.PayWx;
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
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
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
    private Resources resources;
    private boolean isBuy, isDelete = false;
//    private Map<String, String> operateMap;

    private Dialog dialog;
    private boolean zhifubao = true; //支付宝购买
    private List<OrderGoodsEntity> goodsEntityList;
    private TextView tv_count;
    private IWXAPI api;
    private static final int SDK_PAY_FLAG = 2;
    private static final int SDK_PAY_WX = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_WX:
                    break;
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult(
                            (Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showToast("支付成功", true);
                        //TODO  刷新当前UI          但是还要和从广联调一下
//                        if (!isAddOneList.contains(curPos)) {
//                        isAddOneList.add(curPos);
                        if (curPos == 4) {
                            mPresenter.getMyBuyEvaluateOrder(1, 20);
                        } else {
                            mPresenter.getMyBuyOrder(curPos + 1, 1, 20);
                        }
//                        }
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToast("支付失败", true);
                    }
                    break;

            }
        }
    };

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
        resources = getResources();
//        api = WXAPIFactory.createWXAPI(this, resources.getString(R.string.weixin_app_id));
        api = WXAPIFactory.createWXAPI(this, resources.getString(R.string.weixin_app_id), false);
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
            case R.id.bt_close:
                if (deleteDialog != null && deleteDialog.isShowing()) {
                    deleteDialog.dismiss();
                }
                break;
            case R.id.bt_delete:
                if (deleteDialog != null && deleteDialog.isShowing()) {
                    deleteDialog.dismiss();
                }
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
        showToast(msg, true);
    }

    /**
     * 通过支付宝拿到订单号
     *
     * @param order
     */

    @Override
    public void alipaySuccess(AlipayOrder order) {
        //TODO 调起支付宝支付
        bindDataAlipay(order);
    }

    @Override
    public void wxpaySuccess(PayWx entity) {
        if (entity != null) {
//            App.getInstance().setTrade_no(entity.getOut_trade_no());
            PayReq req = new PayReq();
            req.appId = resources.getString(R.string.weixin_app_id);
            req.partnerId = entity.getPartnerid();
            req.prepayId = entity.getPrepayid();
            req.packageValue = entity.getPackage();
            req.nonceStr = entity.getNoncestr();
            req.timeStamp = entity.getTimestamp();
            req.sign = entity.getSign();
            // req.extData = "app data"; // optional
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.registerApp(resources.getString(R.string.weixin_app_id));
            api.sendReq(req);
        }
    }

    private void bindDataAlipay(AlipayOrder order) {
        if (order != null) {
            cancelLoadingDialog();
            /**
             * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
             * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
             * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
             * orderInfo的获取必须来自服务端；
             */
            final String orderInfo = order.getData().getOrderStr();
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    PayTask alipay = new PayTask(MyOrderActivity.this);
                    Map<String, String> result = alipay.payV2(orderInfo, true);
                    Log.i("msp", result.toString());

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    handler.sendMessage(msg);
                }
            };
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
    }

    private void updateFragmentPage(boolean isBuy, List<MyOrderEntity.DataBean> list) {
        Log.i("info", "===================list:" + list + "list.size():" + list.size());
        if (list == null) {
            showToast("没有更多数据", false);
            return;
        }
        int parentSize = list.size();
        goodsEntityList = new ArrayList<>();
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
//                            if (operateMap == null) {
//                                operateMap = new HashMap<>();
//                            } else {
//                                operateMap.clear();
//                            }
//                            operateMap.put("orderid", String.valueOf(orderId));
//                            operateMap.put("type", "cancel");
//                            mPresenter.operateMyOrder(operateMap);
                            mPresenter.cancelOrder(orderId);
                            break;
                        case "pay"://付款
                            float allPrice = entity.getAllPrice();
                            int orderid = entity.getOrderid();
                            if (dialog == null) {
                                showPayDialog(allPrice, orderid);
                            } else {
                                tv_count.setText(allPrice + "元");
                                dialog.show();
                            }
                            break;
                        case "confirmGoods"://确认收货
//                            if (operateMap == null) {
//                                operateMap = new HashMap<>();
//                            } else {
//                                operateMap.clear();
//                            }
//                            operateMap.put("orderid", String.valueOf(orderId));
//                            operateMap.put("type", "cancel");
//                            mPresenter.operateMyOrder(operateMap);
                            mPresenter.confirmOrder(orderId);
                            break;
                        case "cancelrefund"://取消退款申请
                            mPresenter.cancelRefund(orderId);
                            break;
                        case "refund"://退款申请
                            mPresenter.refund(orderId);
                            break;
                        case "agreeRefund"://同意退款
                            mPresenter.agreeRefund(orderId);
                            break;
                        case "disAgreeRefund"://拒绝退款
                            mPresenter.disAgreeRefund(orderId);
                            break;
                        case "wxPaySucessed"://加载更多
                            if (curPos == 4) {
                                mPresenter.getMyBuyEvaluateOrder(1, 20);
                            } else {
                                mPresenter.getMyBuyOrder(curPos + 1, 1, 20);
                            }
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

    private void showPayDialog(float allPrice, final int orderid) {
        dialog = new Dialog(this, R.style.Dialog_full);
        View view_dialog = View.inflate(this,
                R.layout.pop_pay, null);
        dialog.setContentView(view_dialog);
        Window dialogWindow = dialog.getWindow();
        final ImageView img_zhifubao_choose = (ImageView) view_dialog.findViewById(R.id.img_zhifubao_choose);
        final ImageView img_wx_choose = (ImageView) view_dialog.findViewById(R.id.img_wx_choose);
        tv_count = (TextView) view_dialog.findViewById(R.id.tv_count);
        tv_count.setText(allPrice + "元");

        view_dialog.findViewById(R.id.rl_zhifubao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_zhifubao_choose.setImageResource(R.drawable.money_choose);
                img_wx_choose.setImageResource(R.drawable.money_nochoose);
                zhifubao = true;
            }
        });
        view_dialog.findViewById(R.id.rl_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_wx_choose.setImageResource(R.drawable.money_choose);
                img_zhifubao_choose.setImageResource(R.drawable.money_nochoose);
                zhifubao = false;
            }
        });
        view_dialog.findViewById(R.id.tv_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (zhifubao) {
//                    requestAlipayTrade();
                    showToast("支付宝", false);
                    mPresenter.aliPay(orderid);
                } else {
//                    requestWxTrade();
                    showToast("微信", false);
                    mPresenter.wxPay(orderid);
                }
                dialog.dismiss();
            }
        });
        view_dialog.findViewById(R.id.img_xx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView tv_buy = (TextView) view_dialog.findViewById(R.id.tv_buy);
        int screenWidth = UIUtils.getScreenWidth(this);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_buy.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 0.9);
        tv_buy.setLayoutParams(layoutParams);

        dialogWindow.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent); //加上可以在底部对其屏幕底部


        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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
                ((OrderEvaluateFragment) evaluateFragment).setOnRefresh(true);
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
