package com.atgc.cotton.presenter.view;

/**
 * Created by liw on 2017/7/22.
 */

public interface IMsgView extends IBaseView {
    /**
     * 查询消息接口访问成功
     * @param s
     */
    void  searchMsgSuccess(String s);

    /**
     * 删除消息接口访问成功
     * @param s
     */
    void deleteMsgSuccess(String s);


    //网络不好
    void applyFailure(String s);

}
