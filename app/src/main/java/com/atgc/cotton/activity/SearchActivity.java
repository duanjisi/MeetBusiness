package com.atgc.cotton.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.fragment.MainGoodFragment;
import com.atgc.cotton.fragment.MainUserFragment;
import com.atgc.cotton.fragment.MainVideoFragment;
import com.atgc.cotton.util.UIUtils;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017-10-23.
 */
public class SearchActivity extends BaseActivity {

    private static final int VIEW_PAGER_PAGE_1 = 0;
    private static final int VIEW_PAGER_PAGE_2 = 1;
    private static final int VIEW_PAGER_PAGE_3 = 2;
    private static final int PAGE_COUNT = 3;
    private RelativeLayout rl_line;
    private EditText etKeywords;
    private RadioGroup mRadioGroup;
    private ImageView mCursorIm, ivClose;
    private int mCursorImWidth;
    private ViewPager mViewPager;
    private RadioButton mUser, mVideo, mGood;

    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private MainUserFragment mainUserFragment;
    private MainVideoFragment mainVideoFragment;
    private MainGoodFragment mainGoodFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
        mCursorIm = (ImageView) findViewById(R.id.im_cursor);
        mCursorImWidth = UIUtils.setCursorIm(context, rl_line, mCursorIm, PAGE_COUNT);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mUser = (RadioButton) findViewById(R.id.rb_user);
        mVideo = (RadioButton) findViewById(R.id.rb_video);
        mGood = (RadioButton) findViewById(R.id.rb_goods);
        mRadioGroup.setOnCheckedChangeListener(new CheckListener());
        etKeywords = (EditText) findViewById(R.id.et_keyword);
        etKeywords.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    String keyWords = getText(etKeywords);
                    if (!TextUtils.isEmpty(keyWords)) {
                        search(keyWords);
                    } else {
                        showToast("关键字为空!", true);
                    }
                    return true;
                }
                return false;
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addData();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                mFragments);
        mViewPager.setOffscreenPageLimit(PAGE_COUNT - 1);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new MyPageChangeListener());
        mViewPager.setCurrentItem(VIEW_PAGER_PAGE_2);
    }


    private void search(String keyWords) {
        int currentIndex = mViewPager.getCurrentItem();
        switch (currentIndex) {
            case VIEW_PAGER_PAGE_1:
                mainUserFragment.refresh(keyWords);
                break;
            case VIEW_PAGER_PAGE_2:
                mainVideoFragment.refresh(keyWords);
                break;
            case VIEW_PAGER_PAGE_3:
                mainGoodFragment.refresh(keyWords);
                break;
        }
    }

    private void addData() {
        mainUserFragment = new MainUserFragment();
        mainVideoFragment = new MainVideoFragment();
        mainGoodFragment = new MainGoodFragment();

        mFragments.add(mainUserFragment);
        mFragments.add(mainVideoFragment);
        mFragments.add(mainGoodFragment);
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
                    mUser.setChecked(true);
                    break;
                case VIEW_PAGER_PAGE_2:
                    mVideo.setChecked(true);
                    break;
                case VIEW_PAGER_PAGE_3:
                    mGood.setChecked(true);
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
                case R.id.rb_user:
                    mViewPager.setCurrentItem(VIEW_PAGER_PAGE_1);
                    break;
                case R.id.rb_video:
                    mViewPager.setCurrentItem(VIEW_PAGER_PAGE_2);
                    break;
                case R.id.rb_goods:
                    mViewPager.setCurrentItem(VIEW_PAGER_PAGE_3);
                    break;
                default:
                    break;
            }
        }
    }
}
