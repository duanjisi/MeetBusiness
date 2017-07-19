package com.atgc.cotton.activity.goodsDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.adapter.GoodsOrderAdapter;
import com.atgc.cotton.entity.OrderGoods;
import com.atgc.cotton.entity.OrderGoodsListEntity;
import com.atgc.cotton.presenter.PutOrderPresenter;
import com.atgc.cotton.presenter.view.IPutOrderView;
import com.atgc.cotton.util.MoneyUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 填写订单        //后面要改成多列表的结算。
 * Created by liw on 2017/7/11.
 */
public class WriteOrderActivity extends MvpActivity<PutOrderPresenter> implements IPutOrderView {

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

    private String buyNum;
    private String type;
    private String imgUrl;
    private String goodsName;
    private int goodsPrice;
    private String goodsJson;
    private OrderGoodsListEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_writer_order);
        ButterKnife.bind(this);
        initUI();
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
        List<OrderGoods> datas = entity.getData();

        adapter.setDatas(datas);
        adapter.notifyDataSetChanged();

        String allPrice = "0";

        for (int i = 0; i < datas.size(); i++) {
            int head = datas.get(i).getHead();
            if(head==0){
                int buyNum = datas.get(i).getBuyNum();
                Double goodsPrice = datas.get(i).getGoodsPrice();
                String singlePrice = MoneyUtil.moneyMul(buyNum + "", goodsPrice + "");
                allPrice = MoneyUtil.moneyAdd(allPrice, singlePrice);
            }

        }
        tvPrice.setText("¥ "+allPrice);

    }


    @OnClick({R.id.img_back, R.id.rl_add_address, R.id.btn_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_add_address:
                //如果没地址
                openActivity(ChooseAddressActivity.class);
                break;
            case R.id.btn_order:
                showToast("提交订单");
                break;
        }
    }
}
