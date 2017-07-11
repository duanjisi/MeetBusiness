package com.atgc.cotton.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.config.LoginStatus;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.entity.UserEntity;
import com.atgc.cotton.presenter.view.INormalView;
import com.atgc.cotton.util.L;
import com.atgc.cotton.util.PreferenceUtils;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liw on 2017/7/6.
 */
public class LoginPresenter extends BasePresenter<INormalView> {


    private Context context;


    public LoginPresenter(INormalView mvpView,Context context) {
        super(mvpView);
        this.context = context;
    }


    /**
     * 手机登录
     * @param map
     */
    public void login(Map<String,String> map) {
        mvpView.showLoading();
        addSubscription(api.phoneLogin(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        UserEntity entity = JSON.parseObject(s, UserEntity.class);

                        if(entity.getCode()==0){  //登录成功,
                            initUser(entity);

                            mvpView.getDataSuccess(entity.getMessage());
                        }else{
                            //登录失败
                            mvpView.getDataFail();
                        }
                        mvpView.hideLoading();
                    }

                    @Override
                    public void onError_(String msg) {
                        mvpView.hideLoading();
                        mvpView.getDataFail();
                    }

                    @Override
                    public void onCompleted_() {

                    }
                }));
    }

    /**
     * 用户数据存手机本地
     * @param entity
     */
    private void initUser(UserEntity entity) {
        UserEntity.DataBean data = entity.getData();
        PreferenceUtils.putBoolean(context, "isThirdLogin", false);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAvatar(data.getAvatar());
        accountEntity.setMobilePhone(data.getMobilePhone());
        accountEntity.setSex(data.getSex());
        accountEntity.setToken(data.getToken());
        accountEntity.setUserId(data.getUserId());
        accountEntity.setUserName(data.getUserName());
        //存本地
        LoginStatus.getInstance().login(accountEntity,false);


    }

    /**
     * qq登录
     * @param map
     */
    public void qqLogin(Map<String,String> map) {
        mvpView.showLoading();
        addSubscription(api.qqLogin(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        UserEntity entity = JSON.parseObject(s, UserEntity.class);

                        if(entity.getCode()==0){  //登录成功,
                            initUser(entity);

                            mvpView.getDataSuccess(entity.getMessage());
                        }else{
                            //登录失败
                            mvpView.getDataFail();
                        }
                        mvpView.hideLoading();
                    }

                    @Override
                    public void onError_(String msg) {
                        mvpView.hideLoading();
                        mvpView.getDataFail();
                    }

                    @Override
                    public void onCompleted_() {

                    }
                }));
    }

    /**
     * wx登录
     * @param map
     */
    public void wxLogin(Map<String,String> map) {
        mvpView.showLoading();
        addSubscription(api.wxLogin(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        UserEntity entity = JSON.parseObject(s, UserEntity.class);

                        if(entity.getCode()==0){  //登录成功,
                            initUser(entity);
                            mvpView.getDataSuccess(entity.getMessage());
                        }else{
                            //登录失败
                            mvpView.getDataFail();
                        }
                        mvpView.hideLoading();
                    }

                    @Override
                    public void onError_(String msg) {
                        mvpView.hideLoading();
                        mvpView.getDataFail();
                    }

                    @Override
                    public void onCompleted_() {

                    }
                }));
    }




}
