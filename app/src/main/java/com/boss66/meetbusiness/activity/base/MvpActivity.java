package com.boss66.meetbusiness.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.boss66.meetbusiness.presenter.BasePresenter;
import com.boss66.meetbusiness.presenter.view.IBaseView;

import butterknife.ButterKnife;

/**
 * 用acitivity，/fragment实现view里的方法，包含一个presenter的引用
 * Created by liw on 2017/7/5.
 */

public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity implements IBaseView {


     protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();
        setContentView(getLayoutId());
        ButterKnife.bind(this);

    }

    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected abstract P createPresenter();


    public void showLoading() {
        showLoadingDialog();
    }

    public void hideLoading() {
        cancelLoadingDialog();
    }


}
