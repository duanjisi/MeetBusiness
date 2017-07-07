package com.atgc.cotton.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.presenter.LoginPresenter;
import com.atgc.cotton.presenter.view.INormalView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liw on 2017/7/6.
 */

public class LoginActivity extends MvpActivity<LoginPresenter> implements INormalView {
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.img_logo)
    ImageView im_logo;
    @Bind(R.id.et_acconut)
    EditText et_account;
    @Bind(R.id.ll_count)
    View ll_count;
    @Bind(R.id.et_pw)
    EditText et_pw;
    @Bind(R.id.tv_Reset)
    TextView tv_reset;
    @Bind(R.id.view_pw)
    View view_pw;
    @Bind(R.id.btn_login)
    Button btn_login;
    @Bind(R.id.tv_register)
    TextView tv_register;
    @Bind(R.id.img_qq)
    ImageView imgQq;
    @Bind(R.id.img_wx)
    ImageView imgWx;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;
    @Bind(R.id.tv_bottom)
    TextView tvBottom;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void getDataSuccess(String s) {

    }

    @Override
    public void getDataFail() {

    }


    @OnClick({R.id.img_back, R.id.et_acconut, R.id.et_pw, R.id.tv_Reset, R.id.btn_login, R.id.tv_register, R.id.img_qq, R.id.img_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.et_acconut:
                break;
            case R.id.et_pw:

                break;
            case R.id.tv_Reset:
                showToast("重置密码");
                break;
            case R.id.btn_login:
                showToast("登录");
                break;
            case R.id.tv_register:
                showToast("注册");
                openActivity(RegisterActivity.class);
                break;
            case R.id.img_qq:
                showToast("qq");
                break;
            case R.id.img_wx:
                showToast("wx");
                break;
        }
    }
}
