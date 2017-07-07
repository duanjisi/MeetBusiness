package com.atgc.cotton.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.fragment.MainDiscoverFragment;
import com.atgc.cotton.fragment.MainFocusFragment;
import com.atgc.cotton.fragment.MainFragment;
import com.atgc.cotton.fragment.MainNearFragment;
import com.atgc.cotton.util.UIUtils;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private static final int VIEW_PAGER_PAGE_1 = 0;
    private static final int VIEW_PAGER_PAGE_2 = 1;
    private static final int VIEW_PAGER_PAGE_3 = 2;
    private static final int PAGE_COUNT = 3;

    private RadioGroup mRadioGroup;
    private ImageView mCursorIm;
    private int mCursorImWidth;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private MainFragment mainFocusFragment, mainDiscoverFragment, mainNearFragment;
    private RadioButton mFocus, mDiscover, mNear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }


    private void initViews() {
        mCursorIm = (ImageView) findViewById(R.id.im_cursor);
        mCursorImWidth = UIUtils.setCursorIm(context, mCursorIm, PAGE_COUNT);

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);

        mFocus = (RadioButton) findViewById(R.id.rb_focus);
        mDiscover = (RadioButton) findViewById(R.id.rb_discover);
        mNear = (RadioButton) findViewById(R.id.rb_near);

        mRadioGroup.setOnCheckedChangeListener(new CheckListener());

        addData();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                mFragments);
        mViewPager.setOffscreenPageLimit(PAGE_COUNT - 1);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new MyPageChangeListener());
        mViewPager.setCurrentItem(VIEW_PAGER_PAGE_1);
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
