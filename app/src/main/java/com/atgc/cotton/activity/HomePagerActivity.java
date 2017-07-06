package com.atgc.cotton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.activity.production.mine.MyProductionActivity;
import com.atgc.cotton.activity.shoppingCar.ShoppingCarActivity;
import com.atgc.cotton.activity.vendingRack.MyOrderActivity;
import com.atgc.cotton.activity.vendingRack.VendingRackHomeActivity;
import com.atgc.cotton.activity.videoedit.RecordVideoActivity;
import com.atgc.cotton.fragment.MainDiscoverFragment;
import com.atgc.cotton.fragment.MainFocusFragment;
import com.atgc.cotton.fragment.MainFragment;
import com.atgc.cotton.fragment.MainNearFragment;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/6/1.
 */
public class HomePagerActivity extends BaseActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ImageView ivSwitch;
    private RelativeLayout rl_left, rl_line;
    private ImageLoader imageLoader;
    private CircleImageView ivAvatar;
    private TextView tvActive, tvMsg, tvInfo;

    private static final int VIEW_PAGER_PAGE_1 = 0;
    private static final int VIEW_PAGER_PAGE_2 = 1;
    private static final int VIEW_PAGER_PAGE_3 = 2;
    private static final int PAGE_COUNT = 3;

    private RadioGroup mRadioGroup, mRadioMenu;
    private ImageView mCursorIm;
    private int mCursorImWidth;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private MainFragment mainFocusFragment, mainDiscoverFragment, mainNearFragment;
    private RadioButton mFocus, mDiscover, mNear;

    private RadioButton rb_shopping;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        rl_line = (RelativeLayout) findViewById(R.id.rl_line);
        ViewTreeObserver vto2 = rl_line.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl_line.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                initViews();
            }
        });
    }


    private void initViews() {
        handler = new Handler();
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        mCursorIm = (ImageView) findViewById(R.id.im_cursor);
        mCursorImWidth = UIUtils.setCursorIm(context, rl_line, mCursorIm, PAGE_COUNT);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRadioMenu = (RadioGroup) findViewById(R.id.bottom_navigation_rg);
        mFocus = (RadioButton) findViewById(R.id.rb_focus);
        mDiscover = (RadioButton) findViewById(R.id.rb_discover);
        mNear = (RadioButton) findViewById(R.id.rb_near);

        ivAvatar = (CircleImageView) findViewById(R.id.iv_avatar);
        tvActive = (TextView) findViewById(R.id.tv_active);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        tvInfo = (TextView) findViewById(R.id.tv_info);

        ivAvatar.setOnClickListener(this);
        tvActive.setOnClickListener(this);
        tvMsg.setOnClickListener(this);
        tvInfo.setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        rl_left = (RelativeLayout) findViewById(R.id.rl_left);
        ivSwitch = (ImageView) findViewById(R.id.iv_switch);
        ivSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(rl_left, true);
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new CheckListener());
        mRadioMenu.setOnCheckedChangeListener(new CheckMenuListener());

        addData();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                mFragments);
        mViewPager.setOffscreenPageLimit(PAGE_COUNT - 1);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new MyPageChangeListener());
        mViewPager.setCurrentItem(VIEW_PAGER_PAGE_1);

        rb_shopping = (RadioButton) findViewById(R.id.rb_shopping);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_active:

                break;
            case R.id.tv_msg:
                openActivity(MessageActivity.class);

                break;
            case R.id.tv_info:

                break;
            case R.id.iv_avatar:
                openActivity(testActivity.class);
                break;
        }
    }

    private void addData() {
        mainFocusFragment = new MainFocusFragment();
        mainDiscoverFragment = new MainDiscoverFragment();
        mainNearFragment = new MainNearFragment();

        mFragments.add(mainFocusFragment);
        mFragments.add(mainDiscoverFragment);
        mFragments.add(mainNearFragment);
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentsList;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public PagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentsList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCursorIm.getLayoutParams();
            params.leftMargin = (int) (position * mCursorImWidth + mCursorImWidth * positionOffset);
            mCursorIm.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case VIEW_PAGER_PAGE_1:
                    mFocus.setChecked(true);
                    break;
                case VIEW_PAGER_PAGE_2:
                    mDiscover.setChecked(true);
                    break;
                case VIEW_PAGER_PAGE_3:
                    mNear.setChecked(true);
                    break;
                default:
                    break;
            }
        }
    }

    private class CheckMenuListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup arg0, int arg1) {
            switch (arg1) {
                case R.id.rb_setting:
                    showToast("个人设置", true);
                    break;
                case R.id.rb_production:
                    openActivity(MyProductionActivity.class);
                    break;
                case R.id.rb_video:
                    openActivity(RecordVideoActivity.class);
                    break;
                case R.id.rb_price:
                    openActivity(VendingRackHomeActivity.class);
                    break;
                case R.id.rb_shopping:
//                    showToast("购物车", true);
                    rb_shopping.setChecked(false);


                    Intent intent = new Intent(HomePagerActivity.this, GoodsDetailActivity.class);
                    startActivity(intent);
//                    overridePendingTransition(R.anim.activity_in, R.anim.activity_no);
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            drawerLayout.closeDrawer(rl_left);
//                        }
//                    },500);

                    break;
                case R.id.rb_order:
                    openActivity(MyOrderActivity.class, null);
                    break;
                default:
                    break;
            }
        }
    }

    private class CheckListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup arg0, int arg1) {

            switch (arg1) {
                case R.id.rb_focus:
                    mViewPager.setCurrentItem(VIEW_PAGER_PAGE_1);
                    break;
                case R.id.rb_discover:
                    mViewPager.setCurrentItem(VIEW_PAGER_PAGE_2);
                    break;
                case R.id.rb_near:
                    mViewPager.setCurrentItem(VIEW_PAGER_PAGE_3);
                    break;
                default:
                    break;
            }
        }
    }
}
