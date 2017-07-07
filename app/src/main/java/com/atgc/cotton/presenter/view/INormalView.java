package com.atgc.cotton.presenter.view;

/**
 * 请求一个接口的基类view
 * Created by liw on 2017/7/5.
 */
public interface INormalView extends IBaseView {


    void getDataSuccess(String s );

    void getDataFail( );
}
