package com.atgc.cotton.activity.goodsDetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseCompatActivity;
import com.atgc.cotton.activity.vendingRack.DeliverGoodsActivity;
import com.atgc.cotton.activity.vendingRack.LogisticsActivity;
import com.atgc.cotton.db.dao.CityDao;
import com.atgc.cotton.db.dao.DistrictDao;
import com.atgc.cotton.db.dao.ProvinceDao;
import com.atgc.cotton.entity.AlipayOrder;
import com.atgc.cotton.entity.MyOrderEntity;
import com.atgc.cotton.entity.OrderActionEntity;
import com.atgc.cotton.entity.OrderGoodsEntity;
import com.atgc.cotton.entity.PayResult;
import com.atgc.cotton.entity.PayWx;
import com.atgc.cotton.presenter.MyOrderPresenter;
import com.atgc.cotton.presenter.view.IMyOrderView;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.PreferenceUtils;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Johnny on 2017-08-26.
 */
public class OrderDetailsActivity extends BaseCompatActivity<MyOrderPresenter> implements IMyOrderView {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_receiver)
    TextView tvReceiver;
    @Bind(R.id.container)
    LinearLayout container;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_order_num)
    TextView tvOrderNum;
    @Bind(R.id.tv_store_name)
    TextView tvStoreName;
    @Bind(R.id.tv_order_state)
    TextView tvOrderState;
    //    @Bind(R.id.iv_icon)
//    RoundImageView ivIcon;
//    @Bind(R.id.tv_goods_name)
//    TextView tvGoodsName;
//    @Bind(R.id.tv_goods_content)
//    TextView tvGoodsContent;
//    @Bind(R.id.tv_goods_price)
//    TextView tvGoodsPrice;
//    @Bind(R.id.tv_goods_num)
//    TextView tvGoodsNum;
    @Bind(R.id.tv_all_price)
    TextView tvAllPrice;
    @Bind(R.id.tv_all_num)
    TextView tvAllNum;
    @Bind(R.id.bt_1)
    Button bt1;
    @Bind(R.id.bt_2)
    Button bt2;
    @Bind(R.id.bt_3)
    Button bt3;
    private int sceenW;
    private ImageLoader imageLoader;
    private List<OrderGoodsEntity> list;
    private OrderGoodsEntity orderGoodsEntity;
    private String orderStr;
    private int orderType;
    private boolean isBuy;
    private int page;
    //    private OrderActionEntity orderActionEntity;
    private Context context;
    private Dialog dialog, deleteDialog;
    private TextView tv_count;
    private boolean zhifubao = true; //支付宝购买
    private static final int SDK_PAY_FLAG = 2;
    private static final int SDK_PAY_WX = 3;
    private Resources resources;
    private IWXAPI api;
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
                        onBackprogress();
                    } else {
                        showToast("支付失败", true);
                    }
                    break;

            }
        }
    };

    private void onBackprogress() {
        Intent intent = new Intent();
        intent.putExtra("isBuy", isBuy);
        intent.putExtra("page", page);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initViews();
    }

    @Override
    protected MyOrderPresenter createPresenter() {
        return new MyOrderPresenter(this);
    }

    private void initViews() {
        context = OrderDetailsActivity.this;
        resources = getResources();
        api = WXAPIFactory.createWXAPI(this, resources.getString(R.string.weixin_app_id), false);
//        orderActionEntity = new OrderActionEntity();
//        orderActionEntity.setChild(1);
//        orderActionEntity.setBuy(true);
        sceenW = UIUtils.getScreenWidth(context);
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderGoodsEntity = (OrderGoodsEntity) bundle.getSerializable("obj");
            orderStr = bundle.getString("orderStr", "");
            orderType = bundle.getInt("orderType", 0);
            isBuy = bundle.getBoolean("isBuy", false);
            page = bundle.getInt("page", 1);
        }

        orderGoodsEntity = (OrderGoodsEntity) getIntent().getSerializableExtra("obj");
        if (orderGoodsEntity != null) {
            list = JSON.parseArray(orderGoodsEntity.getGoodsJson(), OrderGoodsEntity.class);
//            orderActionEntity.setOrderid(orderGoodsEntity.getOrderId());
            String province = ProvinceDao.getInstance().findByRegionId("" + orderGoodsEntity.getProvince());
            String city = CityDao.getInstance().findByRegionId("" + orderGoodsEntity.getCity());
            String district = DistrictDao.getInstance().findByRegionId("" + orderGoodsEntity.getDistrict());
            tvAddress.setText("收货地址： " + province + city + district + orderGoodsEntity.getAddress());
            tvReceiver.setText("收货人： " + orderGoodsEntity.getConsignee());
            tvPhone.setText(orderGoodsEntity.getMobile());
            tvOrderNum.setText("订单号： " + orderGoodsEntity.getOrderSn());

            //头
            tvStoreName.setText(orderGoodsEntity.getStoreName());
            tvOrderState.setText(orderStr);

            //中
//            setTextView(tvGoodsContent, orderGoodsEntity.getGoodsAttr());
//            setTextView(tvGoodsName, orderGoodsEntity.getGoodsName());
//            setTextView(tvGoodsNum, "x" + orderGoodsEntity.getBuyNumber());
//            setTextView(tvGoodsPrice, "￥" + orderGoodsEntity.getShopPrice());
//
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivIcon.getLayoutParams();
//            int scw = sceenW / 4;
//            layoutParams.width = scw;
//            layoutParams.height = scw;
//            ivIcon.setLayoutParams(layoutParams);
//            String goodsImg = orderGoodsEntity.getGoodsImg();
//            if (!TextUtils.isEmpty(goodsImg)) {
//                imageLoader.displayImage(goodsImg, ivIcon, ImageLoaderUtils.getDisplayImageOptions());
//            }

            //尾
//            int allNum = orderGoodsEntity.getAllNum();
//            float allPrice = orderGoodsEntity.getOrderAmount();
//            tvAllNum.setText("共" + allNum + "件：合计");
//            tvAllPrice.setText("￥" + allPrice);
            addContainer();
            initOrderStatus();
        }
    }

    private void addContainer() {
        int allNum = 0;
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                OrderGoodsEntity orderGoodsEntity = list.get(i);
                allNum = allNum + orderGoodsEntity.getBuyNumber();
                View view = View.inflate(context, R.layout.item_order_content, null);
                TextView tvGoodsContent = (TextView) view.findViewById(R.id.tv_goods_content);
                TextView tvGoodsName = (TextView) view.findViewById(R.id.tv_goods_name);
                TextView tvGoodsNum = (TextView) view.findViewById(R.id.tv_goods_num);
                TextView tvGoodsPrice = (TextView) view.findViewById(R.id.tv_goods_price);
                RoundImageView ivIcon = (RoundImageView) view.findViewById(R.id.iv_icon);

                setTextView(tvGoodsContent, orderGoodsEntity.getGoodsAttr());
                setTextView(tvGoodsName, orderGoodsEntity.getGoodsName());
                setTextView(tvGoodsNum, "x" + orderGoodsEntity.getBuyNumber());
                setTextView(tvGoodsPrice, "￥" + orderGoodsEntity.getShopPrice());

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivIcon.getLayoutParams();
                int scw = sceenW / 4;
                layoutParams.width = scw;
                layoutParams.height = scw;
                ivIcon.setLayoutParams(layoutParams);
                String goodsImg = orderGoodsEntity.getGoodsImg();
                if (!TextUtils.isEmpty(goodsImg)) {
                    imageLoader.displayImage(goodsImg, ivIcon, ImageLoaderUtils.getDisplayImageOptions());
                }
                view.setTag("" + orderGoodsEntity.getGoodsId());
                view.setOnClickListener(new clickListener());
                container.addView(view);
            }
        }
        float allPrice = orderGoodsEntity.getOrderAmount();
        tvAllNum.setText("共" + allNum + "件：合计");
        tvAllPrice.setText("￥" + allPrice);
    }

    protected void setTextView(TextView tv, String str) {
        if (!TextUtils.isEmpty(str)) {
            tv.setText(str);
        }
    }

    @Override
    public void getMyOrderSuccess(boolean isBuy, List<MyOrderEntity.DataBean> list) {

    }

    @Override
    public void getMyEvaluateOrderSuccess(List<OrderGoodsEntity> lsit) {

    }

    @Override
    public void deleteOrderSuccess() {
        onBackprogress();
    }

    @Override
    public void onError(String msg) {
        showToast(msg, true);
    }

    @Override
    public void alipaySuccess(AlipayOrder order) {
        //TODO 调起支付宝支付
        BindDataAlipay(order);
    }

    @Override
    public void wxpaySuccess(PayWx entity) {
        if (entity != null) {
            PayReq req = new PayReq();
            req.appId = resources.getString(R.string.weixin_app_id);
            req.partnerId = entity.getPartnerid();
            req.prepayId = entity.getPrepayid();
            req.packageValue = entity.getPackage();
            req.nonceStr = entity.getNoncestr();
            req.timeStamp = entity.getTimestamp();
            req.sign = entity.getSign();
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.registerApp(resources.getString(R.string.weixin_app_id));
            api.sendReq(req);
            PreferenceUtils.putBoolean(context, Constants.WX_POINT_PAY_KEY, true);
        }
    }


    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String goodId = (String) view.getTag();
            if (!TextUtils.isEmpty(goodId)) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("goodId", goodId);
                startActivity(intent);
            }
        }
    }

    private void initOrderStatus() {
        switch (orderType) {//"0:待付款", "1:待发货", "2:待收货", "3:待评价"
            case 0:
                if (isBuy) {
                    bt1.setText("付款");
                    bt2.setText("取消订单");
                    bt1.setVisibility(View.VISIBLE);
                    bt2.setVisibility(View.VISIBLE);
                } else {
                    bt1.setVisibility(View.GONE);
                    bt2.setVisibility(View.GONE);
                }
                bt3.setVisibility(View.GONE);
                break;
            case 1:
                if (isBuy) {
                    bt1.setVisibility(View.VISIBLE);
                    bt1.setText("申请退款");
                } else {
                    bt1.setVisibility(View.VISIBLE);
                    bt2.setVisibility(View.VISIBLE);
                    bt1.setText("确认发货");
                }
                bt2.setVisibility(View.GONE);
                bt3.setVisibility(View.GONE);
                break;
            case 2:
                if (isBuy) {
                    bt1.setText("确认收货");
                    bt1.setVisibility(View.VISIBLE);
                    bt2.setVisibility(View.VISIBLE);
                    bt2.setText("查看物流");
                } else {
                    bt1.setVisibility(View.GONE);
                    bt2.setVisibility(View.GONE);
                }
                bt3.setVisibility(View.GONE);
                break;
            case 3:
                bt1.setText("删除订单");
                bt1.setVisibility(View.VISIBLE);
                bt2.setVisibility(View.GONE);
                bt3.setVisibility(View.GONE);
                break;
            case 6:
                bt3.setVisibility(View.GONE);
                if (!isBuy) {
                    bt1.setText("同意退款");
                    bt1.setVisibility(View.VISIBLE);
                    bt2.setText("拒绝退款");
                    bt2.setVisibility(View.VISIBLE);
                } else {
                    bt1.setVisibility(View.GONE);
                }
                break;
//            case 7:
//                if (isBuy) {
//                    bt1.setVisibility(View.VISIBLE);
//                    bt1.setText("申请退款");
//                    bt2.setVisibility(View.GONE);
//                    bt3.setVisibility(View.GONE);
//                }
//                break;
            default:
                bt1.setVisibility(View.GONE);
                bt2.setVisibility(View.GONE);
                bt3.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick({R.id.img_back, R.id.bt_1, R.id.bt_2, R.id.bt_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.bt_1:
                btnToDo(1);
                break;
            case R.id.bt_2:
                btnToDo(2);
                break;
            case R.id.bt_3:
                btnToDo(3);
                break;
        }
    }

    private void btnToDo(int type) {
        if (orderGoodsEntity == null) {
            return;
        }
//        orderActionEntity.setOrderid(orderGoodsEntity.getOrderId());
        int orderId = orderGoodsEntity.getOrderId();
        Intent intent = null;
        switch (orderType) {//"0:待付款", "1:待发货", "2:待收货", "3:待评价"
            case 0:
                if (isBuy) {
                    if (type == 1) {
                        Log.i("OrderEntity:", "付款");
//                        orderActionEntity.setAllPrice(orderGoodsEntity.getOrderAmount());
//                        orderActionEntity.setDoAction("pay");
                        float allPrice = orderGoodsEntity.getOrderAmount();
//                        int orderid = orderGoodsEntity.getOrderId();
                        if (dialog == null) {
                            showPayDialog(allPrice, orderId);
                        } else {
                            tv_count.setText(allPrice + "元");
                            dialog.show();
                        }
                    } else if (type == 2) {
                        Log.i("OrderEntity:", "取消订单");
//                        orderActionEntity.setDoAction("cancelOrder");
                        mPresenter.cancelOrder(orderId);
                    }
//                    EventBus.getDefault().post(orderActionEntity);
                } else {
                    if (type == 1) {
                        Log.i("OrderEntity:", "确认发货");
                        intent = new Intent(context, DeliverGoodsActivity.class);
                        intent.putExtra("orderid", orderGoodsEntity.getOrderId());
                        startActivity(intent);
                    }
                }
                break;
            case 1:
                if (isBuy) {
                    if (type == 1) {
                        Log.i("OrderEntity:", "申请退款");
                        mPresenter.refund(orderId);
//                        orderActionEntity.setDoAction("refund");
//                        EventBus.getDefault().post(orderActionEntity);
                    }
                }
                break;
            case 2:
                if (isBuy) {
                    if (type == 1) {
                        Log.i("OrderEntity:", "确认收货");
                        mPresenter.confirmOrder(orderId);
//                        orderActionEntity.setDoAction("confirmGoods");
//                        EventBus.getDefault().post(orderActionEntity);
                    } else if (type == 2) {
                        Log.i("OrderEntity:", "查看物流");
                        openActivity(LogisticsActivity.class);
                    }
                }
                break;
            case 3:
                if (isBuy) {
                    if (type == 1) {
                        Log.i("OrderEntity:", "删除订单");
                        if (type == 1) {
                            Log.i("OrderEntity:", "删除订单");
//                            orderActionEntity.setDoAction("delete");
//                            EventBus.getDefault().post(orderActionEntity);
                            showDeleteDialog();
                        }
                    }
                }
                break;
            case 4:
                if (isBuy) {
                    if (type == 1) {
                        Log.i("OrderEntity:", "取消退款申请");
                        mPresenter.cancelRefund(orderId);
//                        orderActionEntity.setDoAction("cancelrefund");
//                        EventBus.getDefault().post(orderActionEntity);
                    }
                }
                break;
            case 6:
                if (!isBuy) {
                    if (type == 1) {
                        Log.i("OrderEntity:", "同意退款");
//                        orderActionEntity.setDoAction("agreeRefund");
//                        EventBus.getDefault().post(orderActionEntity);
                        mPresenter.agreeRefund(orderId);
                    } else if (type == 2) {
                        Log.i("OrderEntity:", "拒绝退款");
//                        orderActionEntity.setDoAction("disAgreeRefund");
//                        EventBus.getDefault().post(orderActionEntity);
                        mPresenter.disAgreeRefund(orderId);
                    }
                }
                break;
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
                    showToast("支付宝", false);
                    mPresenter.aliPay(orderid);
                } else {
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

    private void showDeleteDialog() {
        if (deleteDialog == null) {
            View view = LayoutInflater.from(this).inflate(
                    R.layout.dialog_delete_order, null);
            Button bt_close = (Button) view.findViewById(R.id.bt_close);
            Button bt_delete = (Button) view.findViewById(R.id.bt_delete);
            bt_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (deleteDialog != null && deleteDialog.isShowing()) {
                        deleteDialog.dismiss();
                    }
                }
            });
            bt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (deleteDialog != null && deleteDialog.isShowing()) {
                        deleteDialog.dismiss();
                    }
                    mPresenter.deleteMyBuyOrder(orderGoodsEntity.getOrderId());
                }
            });
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

    private void BindDataAlipay(AlipayOrder order) {
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
                    PayTask alipay = new PayTask(OrderDetailsActivity.this);
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

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getChildAction(OrderActionEntity entity) {
        if (entity != null) {
            String action = entity.getDoAction();
            if (action.equals("wxDetailsPaySucessed")) {
                PreferenceUtils.putBoolean(context, Constants.WX_POINT_PAY_KEY, false);
                onBackprogress();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
