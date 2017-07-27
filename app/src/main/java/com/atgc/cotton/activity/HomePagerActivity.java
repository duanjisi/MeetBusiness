package com.atgc.cotton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.App;
import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.activity.production.mine.EditDataActivity;
import com.atgc.cotton.activity.production.mine.MyProductionActivity;
import com.atgc.cotton.activity.shoppingCar.ShoppingCarActivity;
import com.atgc.cotton.activity.vendingRack.MyOrderActivity;
import com.atgc.cotton.activity.vendingRack.VendingRackHomeActivity;
import com.atgc.cotton.activity.videoedit.RecordVideoActivity;
import com.atgc.cotton.config.LoginStatus;
import com.atgc.cotton.entity.ActionEntity;
import com.atgc.cotton.fragment.MainDiscoverFragment;
import com.atgc.cotton.fragment.MainFocusFragment;
import com.atgc.cotton.fragment.MainFragment;
import com.atgc.cotton.fragment.MainNearFragment;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Johnny on 2017/6/1.
 */
public class HomePagerActivity extends BaseActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ImageView ivSwitch;
    private RelativeLayout rl_left, rl_line;
    private ImageLoader imageLoader;
    private CircleImageView ivAvatar;
    private TextView tvMsg;
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
    private RadioButton rb_setting, rb_production, rb_video, rb_shopping, rb_price, rb_order;
    private Handler handler;
    private Button btn_login;
    private TextView tv_name;
    private boolean isExit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.home_activity);
        EventBus.getDefault().register(this);
        App.getInstance().addTempActivity(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
//        tvActive = (TextView) findViewById(R.id.tv_active);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
//        tvInfo = (TextView) findViewById(R.id.tv_info);

        ivAvatar.setOnClickListener(this);
//        tvActive.setOnClickListener(this);
        tvMsg.setOnClickListener(this);
//        tvInfo.setOnClickListener(this);

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

        rb_setting = (RadioButton) findViewById(R.id.rb_setting);
        rb_production = (RadioButton) findViewById(R.id.rb_production);
        rb_video = (RadioButton) findViewById(R.id.rb_video);
        rb_shopping = (RadioButton) findViewById(R.id.rb_shopping);
        rb_price = (RadioButton) findViewById(R.id.rb_price);
        rb_order = (RadioButton) findViewById(R.id.rb_order);

        tv_name = (TextView) findViewById(R.id.tv_name);
        String avatar = LoginStatus.getInstance().getAvatar();
        String username = LoginStatus.getInstance().getUsername();
        if (!TextUtils.isEmpty(avatar)) {
            Glide.with(context).load(avatar).into(ivAvatar);
        }
        if (!TextUtils.isEmpty(username)) {
            tv_name.setText(username);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_active:
//                break;
            case R.id.tv_msg:
                openActivity(MessageActivity.class);
                break;
//            case R.id.tv_info:
//                break;
            case R.id.iv_avatar:
//                openActivity(testActivity.class);
                openActivity(EditDataActivity.class);
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
                    rb_setting.setChecked(false);
                    openActivity(PersonalSetActivity.class);
                    rb_setting.setChecked(false);
                    closeDrawer();
                    break;
                case R.id.rb_production:
                    rb_production.setChecked(false);
                    openActivity(MyProductionActivity.class);
                    rb_production.setChecked(false);
                    closeDrawer();
//                    openActivity(OtherPlayerActivity.class);
                    break;
                case R.id.rb_video:
                    rb_video.setChecked(false);
                    openActivity(RecordVideoActivity.class);
                    rb_video.setChecked(false);
                    closeDrawer();
                    break;
                case R.id.rb_price:
                    rb_price.setChecked(false);
                    openActivity(VendingRackHomeActivity.class);
                    rb_price.setChecked(false);
                    closeDrawer();
                    break;
                case R.id.rb_shopping:
                    rb_shopping.setChecked(false);


                    Intent intent = new Intent(HomePagerActivity.this, ShoppingCarActivity.class);
                    startActivity(intent);

                    overridePendingTransition(R.anim.activity_in, R.anim.activity_no);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawerLayout.closeDrawer(rl_left);
                        }
                    }, 500);
//                    openActivity(ShoppingCarActivity.class);
                    break;
                case R.id.rb_order:
                    rb_order.setChecked(false);
                    openActivity(MyOrderActivity.class);
                    rb_order.setChecked(false);
                    closeDrawer();
                    break;
                default:
                    break;
            }
        }
    }

    private void closeDrawer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawerLayout.closeDrawer(rl_left);
            }
        }, 500);
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

    @Subscribe
    public void onMessageEvent(ActionEntity event) {
        if (event != null) {
            String action = event.getAction();
            String tag = (String) event.getData();
            if (action.equals(Constants.Action.UPDATE_ACCOUNT_INFORM)) {
                LoginStatus sLoginStatus = LoginStatus.getInstance();
                if (tag.equals("avatar")) {
                    String avatar = sLoginStatus.getAvatar();
                    imageLoader.displayImage(avatar, ivAvatar, ImageLoaderUtils.getDisplayImageOptions());
                } else if (tag.equals("name")) {
                    String name = sLoginStatus.getUsername();
                    tv_name.setText(name);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {

        if(!isExit){
           showToast("再按一次退出应用");
            isExit = true;
            EventBus.getDefault().post(isExit);
        }else{
            super.onBackPressed();
        }

    }

    @Subscribe(threadMode = ThreadMode.Async)
    public void onEventExit(Boolean isBool){
        SystemClock.sleep(1000);
        isExit = false;
    }


    @Subscribe
    public void onEvent(ActionEntity entity){
      if(entity.getAction().equals(Constants.Action.UPDATE_ACCOUNT_INFORM )){
          String avatar = LoginStatus.getInstance().getAvatar();
          String username = LoginStatus.getInstance().getUsername();
          if (!TextUtils.isEmpty(avatar)) {
              Glide.with(context).load(avatar).into(ivAvatar);
          }
          if (!TextUtils.isEmpty(username)) {
              tv_name.setText(username);
          }
       }
    }
}
