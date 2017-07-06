package com.atgc.cotton.activity.production.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.fragment.ProductFragment;
import com.atgc.cotton.listenter.ListenerConstans;
import com.atgc.cotton.listenter.ViewPagerListener;
import com.atgc.cotton.widget.SimpleViewPagerIndicator;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/5.
 * 我的作品
 */
public class MyProductionActivity extends BaseActivity implements View.OnClickListener, ViewPagerListener {

    private ImageView iv_back, iv_share;
    private TextView tv_title, tv_focus, tv_fans, tv_edit, tv_intro;

    private String[] mTitles = new String[]{"作品", "喜欢"};
    private SimpleViewPagerIndicator mIndicator;
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_production);
        ListenerConstans.mQunZuPager = this;
        initViews();
        initDatas();
        initEvents();
    }

    private void initViews() {
        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        tv_title = (TextView) findViewById(R.id.tv_name);
        tv_focus = (TextView) findViewById(R.id.tv_focus);
        tv_fans = (TextView) findViewById(R.id.tv_fans);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_intro = (TextView) findViewById(R.id.tv_intro);

        iv_back.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        tv_focus.setOnClickListener(this);
        tv_fans.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_intro.setOnClickListener(this);
    }

    private void initDatas() {
        mIndicator.setTitles(mTitles);
        mFragments.add(new ProductFragment());
        mFragments.add(new ProductFragment());
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
    }

    private void initEvents() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setCurrentItem(int page) {
        mViewPager.setCurrentItem(page);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:

                break;
            case R.id.tv_focus:

                break;
            case R.id.tv_fans:

                break;
            case R.id.tv_edit:
                openActivity(EditDataActivity.class);
                break;
            case R.id.tv_intro:

                break;
        }
    }
}
