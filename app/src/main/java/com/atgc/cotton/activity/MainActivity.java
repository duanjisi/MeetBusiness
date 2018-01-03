package com.atgc.cotton.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.UpdateInfoEntity;
import com.atgc.cotton.fragment.MainDiscoverFragment;
import com.atgc.cotton.fragment.MainFragment;
import com.atgc.cotton.fragment.MainHotFragment;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.CheckUpdateRequest;
import com.atgc.cotton.listener.PermissionListener;
import com.atgc.cotton.util.AutoUpdateUtil;
import com.atgc.cotton.util.PermissonUtil.PermissionUtil;
import com.atgc.cotton.util.ToastUtil;
import com.atgc.cotton.util.UIUtils;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/7.
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView iv_lock;
    private static final int VIEW_PAGER_PAGE_1 = 0;
    private static final int VIEW_PAGER_PAGE_2 = 1;
    private static final int PAGE_COUNT = 2;
    private ImageView mCursorIm;
    private int mCursorImWidth;
    private RelativeLayout rl_line;
    private RadioGroup mRadioGroup;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private MainFragment mainDiscoverFragment, mainHotFragment;
    private RadioButton mDiscover, mHot;
    private PermissionListener permissionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        iv_lock = (ImageView) findViewById(R.id.iv_lock);
        iv_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(LoginActivity.class);
                finish();
            }
        });
        mCursorIm = (ImageView) findViewById(R.id.im_cursor);
        mCursorImWidth = UIUtils.setCursorIm(context, rl_line, mCursorIm, PAGE_COUNT);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mDiscover = (RadioButton) findViewById(R.id.rb_discover);
        mHot = (RadioButton) findViewById(R.id.rb_hot);
        mRadioGroup.setOnCheckedChangeListener(new CheckListener());
        addData();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                mFragments);
        mViewPager.setOffscreenPageLimit(PAGE_COUNT - 1);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new MyPageChangeListener());
        mViewPager.setCurrentItem(VIEW_PAGER_PAGE_1);
        getPermission();
    }

    private void addData() {
        mainDiscoverFragment = new MainDiscoverFragment();
        mainHotFragment = new MainHotFragment();
        mFragments.add(mainDiscoverFragment);
        mFragments.add(mainHotFragment);
    }

    private void checkUpdate() {
        CheckUpdateRequest request = new CheckUpdateRequest(TAG);
        request.send(new BaseDataRequest.RequestCallback<UpdateInfoEntity>() {
            @Override
            public void onSuccess(UpdateInfoEntity response) {
                AutoUpdateUtil.update(response.getAppVersion(), context, response.getAPKUrl(),
                        response.getUpMsg(), response.isUpForce(), false);
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    private void getPermission() {
        permissionListener = new PermissionListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, permissionListener);
            }

            @Override
            public void onRequestPermissionSuccess() {
                checkUpdate();
            }

            @Override
            public void onRequestPermissionError() {
                ToastUtil.showShort(context, "请给予定位权限");
            }
        };
        PermissionUtil
                .with(this)
                .permissions(
                        PermissionUtil.PERMISSIONS_SD_READ_WRITE //读写手机sd权限
                ).request(permissionListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, permissionListener);
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
                    mDiscover.setChecked(true);
                    break;
                case VIEW_PAGER_PAGE_2:
                    mHot.setChecked(true);
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
                case R.id.rb_discover:
                    mViewPager.setCurrentItem(VIEW_PAGER_PAGE_1);
                    break;
                case R.id.rb_hot:
                    mViewPager.setCurrentItem(VIEW_PAGER_PAGE_2);
                    break;
                default:
                    break;
            }
        }
    }
}
