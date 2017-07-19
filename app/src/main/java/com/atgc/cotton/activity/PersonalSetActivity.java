package com.atgc.cotton.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.http.HttpUrl;
import com.atgc.cotton.util.L;
import com.atgc.cotton.util.OkManager;

/**
 * Created by liw on 2017/7/8.
 */
public class PersonalSetActivity extends BaseActivity implements View.OnClickListener {
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        findViewById(R.id.tv_back).setOnClickListener(this);
        findViewById(R.id.tv_exit_login).setOnClickListener(this);
        findViewById(R.id.rl_about).setOnClickListener(this);
        handler = new Handler();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_exit_login:    //退出登录
//                OkManager.getInstance().doGet(HttpUrl.LOGOUT, new OkManager.Funcl() {
//                    @Override
//                    public void onResponse(String result) {
//                        L.i(result);
//                        BaseResult bean = JSON.parseObject(result, BaseResult.class);
//                        if (bean.getCode() == 0) {
//                            App.getInstance().logout();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    openActivity(MainActivity.class);
//                                    App.getInstance().exit();
//                                }
//                            }, 100);
//                        }else {
//                            showToast(bean.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure() {
//
//                    }
//                });


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        App.getInstance().logout();
                        openActivity(MainActivity.class);
                        App.getInstance().exit();
                    }
                },100);
                break;
            case R.id.rl_about:
                break;


        }

    }
}
