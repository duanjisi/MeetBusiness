package com.atgc.cotton.activity;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.presenter.LoginPresenter;
import com.atgc.cotton.presenter.view.INormalView;

/**
 * Created by liw on 2017/7/6.
 */

public class LoginActivity extends MvpActivity<LoginPresenter> implements INormalView {
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
}
