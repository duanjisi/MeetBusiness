package com.atgc.cotton.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.presenter.GoodsDetailPresenter;
import com.atgc.cotton.presenter.view.INormalView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 商品详情页
 * Created by liw on 2017/7/5.
 */

public class GoodsDetailActivity extends MvpActivity<GoodsDetailPresenter> implements INormalView {


    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_name2)
    TextView tvName2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.loadDataByRetrofitRxjava("101310222");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected GoodsDetailPresenter createPresenter() {
        return new GoodsDetailPresenter(this);
    }


    @Override
    public void getDataSuccess(String s) {
        //请求网络成功的处理
        tvName.setText(s.toString());
    }

    @Override
    public void getDataFail() {
        //请求网络失败的处理
    }

    @OnClick({R.id.tv_name, R.id.tv_name2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_name:

                break;
            case R.id.tv_name2:
                break;
        }
    }
}
