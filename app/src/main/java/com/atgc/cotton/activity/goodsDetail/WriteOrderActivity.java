package com.atgc.cotton.activity.goodsDetail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.activity.vendingRack.MyOrderActivity;
import com.atgc.cotton.adapter.GoodsOrderAdapter;
import com.atgc.cotton.entity.AddressListEntity;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.entity.GoodsJsonEntity;
import com.atgc.cotton.entity.OrderGoods;
import com.atgc.cotton.entity.OrderGoodsListEntity;
import com.atgc.cotton.event.ChangeAddressState;
import com.atgc.cotton.event.RefreshShoopingCar;
import com.atgc.cotton.presenter.PutOrderPresenter;
import com.atgc.cotton.presenter.view.IPutOrderView;
import com.atgc.cotton.presenter.view.ISingleView;
import com.atgc.cotton.util.L;
import com.atgc.cotton.util.MoneyUtil;
import com.atgc.cotton.util.PreferenceUtils;
import com.atgc.cotton.util.UIUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 填写订单        //后面要改成多列表的结算。
 * Created by liw on 2017/7/11.
 */
public class WriteOrderActivity extends MvpActivity<PutOrderPresenter> implements ISingleView {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.rl_add_address)
    RelativeLayout rlAddAddress;
    @Bind(R.id.btn_order)
    Button btnOrder;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.rl_has_address)
    RelativeLayout rlHasAddress;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_content)
    TextView tvContent;

    private String goodsJson;
    private OrderGoodsListEntity entity;
    private int province;
    private int city;
    private int district;
    private String address;    //详细地址
    private String consignee; //联系人姓名
    private String contact; //联系人电话
    private List<OrderGoods> datas;

    private DbUtils mDbUtils;
    private List<OrderGoods> dbList;

    private  String allPrice = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_writer_order);
        ButterKnife.bind(this);
        initUI();
        initData();
    }

    private void initData() {
        initAddress();

        mDbUtils = DbUtils.create(this);
        try {
            dbList = mDbUtils.findAll(OrderGoods.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected PutOrderPresenter createPresenter() {
        return new PutOrderPresenter(this);
    }

    private void initUI() {
        Intent intent = getIntent();

        if (intent != null) {
            goodsJson = intent.getStringExtra("goodsJson");
        }
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        GoodsOrderAdapter adapter = new GoodsOrderAdapter(this);
        rvContent.setAdapter(adapter);

        entity = JSON.parseObject(goodsJson, OrderGoodsListEntity.class);
        datas = entity.getData();

        adapter.setDatas(datas);
        adapter.notifyDataSetChanged();


        for (int i = 0; i < datas.size(); i++) {
            int head = datas.get(i).getHead();
            if (head == 0) {
                int buyNum = datas.get(i).getBuyNum();
                Double goodsPrice = datas.get(i).getGoodsPrice();
                String singlePrice = MoneyUtil.moneyMul(buyNum + "", goodsPrice + "");
                allPrice = MoneyUtil.moneyAdd(allPrice, singlePrice);
            }

        }
        tvPrice.setText("¥ " + allPrice);

    }


    @OnClick({R.id.img_back, R.id.rl_add_address, R.id.btn_order, R.id.rl_has_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_add_address:
                //如果没地址
                openActivity(ChooseAddressActivity.class);
                break;
            case R.id.btn_order:  //提交订单,提交后，移除购物车里数据
                List<OrderGoods> datas = entity.getData();
                List<OrderGoods> newDatas = new ArrayList<>();

                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getHead() == 0) {
                        newDatas.add(datas.get(i));
                    }
                }

//                L.i(newDatas.toString());
                String s = JSON.toJSONString(newDatas);


                List<GoodsJsonEntity> jsonList = new ArrayList<>();

                //先把每一个商品，都做成一个entity，然后再合并
                for (int i = 0; i < newDatas.size(); i++) {
                    OrderGoods orderGoods = newDatas.get(i);

                    String userId = orderGoods.getUserId();
                    GoodsJsonEntity goodsJsonEntity = new GoodsJsonEntity();
                    goodsJsonEntity.setSupplierid(Integer.parseInt(userId));
                    GoodsJsonEntity.GoodslistBean bean = new GoodsJsonEntity.GoodslistBean();

                    bean.setBuynumber(orderGoods.getBuyNum());
                    bean.setAttrs(orderGoods.getType());
                    bean.setGoodsid(orderGoods.getGoodsId());

                    List<GoodsJsonEntity.GoodslistBean> list = new ArrayList<>();
                    list.add(bean);

                    goodsJsonEntity.setGoodslist(list);
                    jsonList.add(goodsJsonEntity);
                }
                String s2 = JSON.toJSONString(jsonList);

                //把店铺id相同的的数据合并
                List<GoodsJsonEntity> newJsonList = new ArrayList<>();
                List<Integer> ids = new ArrayList<>();

                for (int i = 0; i < jsonList.size(); i++) {

                    GoodsJsonEntity goodsJsonEntity = jsonList.get(i);
                    int supplierid = goodsJsonEntity.getSupplierid();

                    //如果没有直接add进去对象
                    if (!ids.contains(supplierid)) {
                        newJsonList.add(goodsJsonEntity);
                        ids.add(supplierid);

                    } else {
                        //如果有这个店铺了,找到该店铺
                        for (int j = 0; j < newJsonList.size(); j++) {

                            GoodsJsonEntity newEntitiy = newJsonList.get(j);
                            if (newEntitiy.getSupplierid() == (supplierid)) {

                                List<GoodsJsonEntity.GoodslistBean> goodslist = goodsJsonEntity.getGoodslist();
                                newEntitiy.getGoodslist().addAll(goodslist);

                            }

                        }

                    }
                }
                String s1 = JSON.toJSONString(newJsonList);
                Log.i("hahah", s1);
                Map<String, String> map = new HashMap<>();
                map.put("cart", s1);
                map.put("province", province + "");
                map.put("city", city + "");
                map.put("district", district + "");
                map.put("address", address);
                map.put("consignee", consignee);
                map.put("mobile", contact);
                map.put("paytype", 1 + "");    //1 在线支付，2银行转账
                String token = App.getInstance().getToken();
                mPresenter.order(token, map);

                break;
            case R.id.rl_has_address:
                openActivity(ChooseAddressActivity.class);
                break;
        }
    }

    @Subscribe
    public void ChangeAddressState(ChangeAddressState data) {

        initAddress();
    }

    private void initAddress() {
        String addressJson = PreferenceUtils.getString(context, "addressJson", "");
        if (addressJson != null && addressJson.length() > 0) {

            rlHasAddress.setVisibility(View.VISIBLE);
            rlAddAddress.setVisibility(View.GONE);

            AddressListEntity.DataBean bean = JSON.parseObject(addressJson, AddressListEntity.DataBean.class);

            address = bean.getAddress();
            String location = bean.getLocation();

            consignee = bean.getConsignee();
            contact = bean.getContact();

            province = bean.getProvince();
            city = bean.getCity();
            district = bean.getDistrict();
            tvName.setText(consignee + "  " + contact);
            tvContent.setText(location + "  " + address);
        } else {
            rlHasAddress.setVisibility(View.GONE);
            rlAddAddress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(String s) {
        //TODO     需要拿到订单id
        BaseResult result = JSON.parseObject(s, BaseResult.class);
        if (result != null) {
            int code = result.getCode();
            if (code == 0) {
                //提交成功
                try {

                    List<Integer> ids = new ArrayList<>();
                    for (int i = 0; i < datas.size(); i++) {
                        int id = datas.get(i).get_id();
                        ids.add(id);
                    }

                    mDbUtils.deleteAll(dbList);

                    if (dbList != null && dbList.size() > 0) {
                        for (int j = 0; j < dbList.size(); j++) {
                            int id = dbList.get(j).get_id();
                            if (ids.contains(id)) {
                                dbList.remove(j);
                            }

                        }
                    }
                    if (dbList.size() > 0) {
                        mDbUtils.saveAll(dbList);
                    }
                    EventBus.getDefault().post(new RefreshShoopingCar(""));
                    //  先弹出付款页面，如果付款成功，跳到订单页面，如果不付款了，跳会购物车，但是这个页面都要finish。 防止多次点击多次提交订单
//                    if(dialog==null){
//
//                        showDialog();
//                    }else{
//                        dialog.show();
//                    }
                    openActivity(MyOrderActivity.class);
                    finish();

                } catch (DbException e) {
                    e.printStackTrace();
                }


            } else {
                showToast("提交失败");
            }

        }

    }

    @Override
    public void onError(String msg) {

        showToast(msg);
    }

    private Dialog dialog;
    private boolean zhifubao = true; //支付宝购买

    private void showDialog() {
        dialog = new Dialog(this, R.style.Dialog_full);
        View view_dialog = View.inflate(this,
                R.layout.pop_pay, null);


        dialog.setContentView(view_dialog);


        Window dialogWindow = dialog.getWindow();

        final ImageView img_zhifubao_choose = (ImageView) view_dialog.findViewById(R.id.img_zhifubao_choose);
        final ImageView img_wx_choose = (ImageView) view_dialog.findViewById(R.id.img_wx_choose);
        TextView tv_count = (TextView) view_dialog.findViewById(R.id.tv_count);
        tv_count.setText(allPrice+"元");

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
//                    mPresenter.order();

                } else {
//                    requestWxTrade();
                    showToast("微信", false);
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
}
