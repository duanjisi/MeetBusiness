package com.atgc.cotton.presenter.view;

/**
 * Created by liw on 2017/7/7.
 */

public interface IRegisterView extends IBaseView {

    //获取验证码
    void  getCodeSucceed(String s);

    //注册
    void  loginSucceed(String s);

    //网络不好
    void applyFailure(String s);


}
