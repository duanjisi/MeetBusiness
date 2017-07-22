package com.atgc.cotton.presenter.view;

/**
 * 单个请求基类
 * Created by liw on 2017/7/19.
 */

public interface ISingleView extends IBaseView {


    /**
     * 请求网络成功，不管code=0，还是1
     * @param s
     */
    void onSuccess(String s);

    /**
     * 网络请求失败
     * @param msg
     */
    void onError(String msg);
}
