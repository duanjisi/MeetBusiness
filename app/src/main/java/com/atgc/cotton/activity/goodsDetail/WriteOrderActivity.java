package com.atgc.cotton.activity.goodsDetail;

import android.os.Bundle;
import android.view.View;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;

/**
 * 填写订单
 * Created by liw on 2017/7/11.
 */
public class WriteOrderActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_writer_order);
        initUI();
    }

    private void initUI() {

        //如果没地址
        findViewById(R.id.rl_add_address).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        openActivity(ChooseAddressActivity.class);

    }
}
