package com.atgc.cotton.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atgc.cotton.presenter.BasePresenter;
import com.atgc.cotton.presenter.view.IBaseView;

import butterknife.ButterKnife;

/**
 * Created by liw on 2017/7/6.
 */

public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment implements IBaseView {


    protected Context context;

    private View rootView;

    protected P mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mPresenter = createPresenter();
        context = getActivity();
        rootView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected abstract int getLayoutId();

    protected abstract P createPresenter();


    public void showLoading() {
        showLoadingDialog();
    }

    public void hideLoading() {
        cancelLoadingDialog();
    }

}
