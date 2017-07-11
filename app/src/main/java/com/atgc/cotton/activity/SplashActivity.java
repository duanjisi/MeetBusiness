package com.atgc.cotton.activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;

/**
 * Created by Johnny on 2016/7/6.
 */
public class SplashActivity extends BaseActivity {

    private ImageView ivSplash;
    private Animation mFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews();
    }

    public void initViews() {
        ivSplash = (ImageView) findViewById(R.id.iv_splash);
        mFadeIn = AnimationUtils.loadAnimation(context, R.anim.wecome_animi);
        mFadeIn.setFillAfter(true);
        ivSplash.startAnimation(mFadeIn);
        setListener();
    }

    private void setListener() {
        mFadeIn.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (App.getInstance().isLogin()) {
                    openActivity(HomePagerActivity.class);
                } else {
                    openActivity(MainActivity.class);
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
