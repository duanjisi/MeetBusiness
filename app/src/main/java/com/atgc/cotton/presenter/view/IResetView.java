package com.atgc.cotton.presenter.view;

/**
 * Created by liw on 2017/7/19.
 */

public interface IResetView extends IBaseView {

    void getCodeSucess(String s);

    void resetPswSucess(String s);

    void onError(String msg);
}
