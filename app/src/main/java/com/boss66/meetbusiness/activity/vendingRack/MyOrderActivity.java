package com.boss66.meetbusiness.activity.vendingRack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import com.boss66.meetbusiness.fragment.OrderShipmentsFragment;

import java.util.ArrayList;

/**
 * Created by GMARUnity on 2017/6/28.
 */
public class MyOrderActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv_back;
    private TabLayout tablayout;
    private ViewPager viewpager;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private BaseOrderFragment allFragment, obligationFragment, shipmentsFragment, receivingFragment, evaluateFragment;
    private String[] titles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        initData();
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewpager.setOffscreenPageLimit(4);
    }

    private void initData() {
        titles = new String[]{"全部", "待付款", "待发货", "待发货", "待评价"};
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
        PagerAdapter adapter = new PagerAdapter(this.getSupportFragmentManager());
        viewpager.setAdapter(adapter);
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
}
