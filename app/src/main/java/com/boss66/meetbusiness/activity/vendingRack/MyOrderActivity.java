package com.boss66.meetbusiness.activity.vendingRack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.fragment.BaseOrderFragment;
import com.boss66.meetbusiness.fragment.OrderAllFragment;
import com.boss66.meetbusiness.fragment.OrderEvaluateFragment;
import com.boss66.meetbusiness.fragment.OrderObligationFragment;
import com.boss66.meetbusiness.fragment.OrderReceivingFragment;
import com.boss66.meetbusiness.fragment.OrderSellerAllFragment;
import com.boss66.meetbusiness.fragment.OrderSellerObligationFragment;
import com.boss66.meetbusiness.fragment.OrderSellerRefundFragment;
import com.boss66.meetbusiness.fragment.OrderSellerShipmentsFragment;
import com.boss66.meetbusiness.fragment.OrderShipmentsFragment;

import java.util.ArrayList;

/**
 * Created by GMARUnity on 2017/6/28.
 */
public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView tv_back, tv_buyer, tv_seller;
    //private TabLayout tablayout,tablayout_1;
    private ViewPager viewpager, viewpager_1;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<Fragment> mTwoFragments = new ArrayList<>();
    private BaseOrderFragment allFragment, obligationFragment, shipmentsFragment, receivingFragment, evaluateFragment;
    private String[] titles, twotitles;
    private PagerTwoAdapter pagerTwoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        initData();
    }

    private void initView() {
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
        viewpager_1.setOffscreenPageLimit(4);
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

        OrderSellerAllFragment orderSellerAllFragment = new OrderSellerAllFragment();
        OrderSellerObligationFragment orderSellerObligationFragment = new OrderSellerObligationFragment();
        OrderSellerShipmentsFragment orderSellerShipmentsFragment = new OrderSellerShipmentsFragment();
        OrderSellerRefundFragment orderSellerRefundFragment = new OrderSellerRefundFragment();
        mTwoFragments.add(orderSellerAllFragment);
        mTwoFragments.add(orderSellerObligationFragment);
        mTwoFragments.add(orderSellerShipmentsFragment);
        mTwoFragments.add(orderSellerRefundFragment);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
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
                break;
            case R.id.tv_buyer:
                viewpager.setVisibility(View.VISIBLE);
                viewpager_1.setVisibility(View.GONE);
                tv_buyer.setSelected(true);
                tv_seller.setSelected(false);
                break;
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
}
