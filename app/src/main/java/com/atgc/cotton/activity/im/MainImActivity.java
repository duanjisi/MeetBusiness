package com.atgc.cotton.activity.im;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.fragment.BaseFragment;
import com.atgc.cotton.fragment.ImFriendFragment;
import com.atgc.cotton.fragment.ImInfoFragment;
import com.atgc.cotton.fragment.ImMsgFragment;
import com.atgc.cotton.util.UIUtils;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017-08-22.
 */
public class MainImActivity extends BaseActivity {
    private static final int VIEW_PAGER_PAGE_1 = 0;
    private static final int VIEW_PAGER_PAGE_2 = 1;
    private static final int VIEW_PAGER_PAGE_3 = 2;
    private static final int PAGE_COUNT = 3;
    private RelativeLayout rl_line;
    private ImageView iv_back, iv_msg;
    private ImageView mCursorIm;
    private int mCursorImWidth;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private BaseFragment
            imFriendFragment, imInfoFragment, imMsgFragment;
    private RadioGroup mRadioGroup;
    private RadioButton mFriend, mInfo, mMsg;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_im_pager);
        initViews();
    }

    private void initViews() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentIndex = bundle.getInt("currentIndex", 0);
        }
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_msg = (ImageView) findViewById(R.id.iv_msg);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iv_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(ContactsActivity.class);
            }
        });
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        rl_line = (RelativeLayout) findViewById(R.id.rl_line);
        mCursorIm = (ImageView) findViewById(R.id.im_cursor);
        mCursorImWidth = UIUtils.setCursorIm(context, rl_line, mCursorIm, PAGE_COUNT);
        mFriend = (RadioButton) findViewById(R.id.rb_friend);
        mInfo = (RadioButton) findViewById(R.id.rb_info);
        mMsg = (RadioButton) findViewById(R.id.rb_msg);

        mRadioGroup.setOnCheckedChangeListener(new CheckListener());
        addData();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                mFragments);
        mViewPager.setOffscreenPageLimit(PAGE_COUNT - 1);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new MyPageChangeListener());
        switch (currentIndex) {
            case 0:
                mViewPager.setCurrentItem(VIEW_PAGER_PAGE_1);
                break;
            case 1:
                mViewPager.setCurrentItem(VIEW_PAGER_PAGE_2);
                break;
            case 2:
                mViewPager.setCurrentItem(VIEW_PAGER_PAGE_3);
                break;
        }
    }


    private void addData() {
        imFriendFragment = new ImFriendFragment();
        imInfoFragment = new ImInfoFragment();
        imMsgFragment = new ImMsgFragment();

        mFragments.add(imFriendFragment);
        mFragments.add(imInfoFragment);
        mFragments.add(imMsgFragment);
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
                    mFriend.setChecked(true);
                    break;
                case VIEW_PAGER_PAGE_2:
                    mInfo.setChecked(true);
                    break;
                case VIEW_PAGER_PAGE_3:
                    mMsg.setChecked(true);
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
                case R.id.rb_friend:
                    mViewPager.setCurrentItem(VIEW_PAGER_PAGE_1);
                    break;
                case R.id.rb_info:
                    mViewPager.setCurrentItem(VIEW_PAGER_PAGE_2);
                    break;
                case R.id.rb_msg:
                    mViewPager.setCurrentItem(VIEW_PAGER_PAGE_3);
                    break;
                default:
                    break;
            }
        }
    }
}
