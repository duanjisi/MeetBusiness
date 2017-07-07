package com.atgc.cotton.presenter;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
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


    public LoginPresenter(INormalView mvpView) {
        super(mvpView);
    }


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
                            //TODO    把个人信息存在本地
                            UserEntity.DataBean data = entity.getData();
                            PreferenceUtils.putBoolean(App.getInstance().getApplicationContext(), "isThirdLogin", true);
                            //存本地，写个类.

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
