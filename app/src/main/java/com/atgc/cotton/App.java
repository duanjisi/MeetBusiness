package com.atgc.cotton;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.atgc.cotton.config.LoginStatus;
import com.atgc.cotton.entity.AccountEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 2017/5/16.
 */
public class App extends Application {
    private static App mApplication;
    private List<Activity> tempActivityList;
    private AccountEntity sAccount;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mApplication = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public synchronized static App getInstance() {
        return mApplication;
    }

    public String getUid() {
        if (sAccount == null) {
            return LoginStatus.getInstance().getUserid();
        }
        return sAccount.getUserId();
    }

    public String getToken() {
        if (sAccount == null) {
            return LoginStatus.getInstance().getToken();
        }
        return sAccount.getToken();
    }

    public AccountEntity getAccountEntity() {
        if (sAccount == null) {
            LoginStatus loginStatus = LoginStatus.getInstance();
            sAccount = new AccountEntity();
            sAccount.setAvatar(loginStatus.getAvatar());
            sAccount.setSex(loginStatus.getSex());
            sAccount.setMobilePhone(loginStatus.getPhone());
            sAccount.setSex(loginStatus.getSex());
            sAccount.setToken(loginStatus.getToken());
            sAccount.setUserId(loginStatus.getUserid());
            sAccount.setUserName(loginStatus.getUsername());
        }
        return sAccount;
    }

    public boolean isLogin() {
        return LoginStatus.getInstance().hadLogged();
    }

    public void initUser(AccountEntity account) {
        sAccount = account;
        LoginStatus.getInstance().login(account, false);
    }

    public void logout() {
        sAccount = null;
        LoginStatus.getInstance().clear();
    }


    public void addTempActivity(Activity activity) {
        if (tempActivityList == null) {
            tempActivityList = new ArrayList<>();
        }
        tempActivityList.add(activity);
    }

    public List<Activity> getTempActivityList() {
        return tempActivityList;
    }


    //遍历所有Activity并finish
    public void exit() {
        if (tempActivityList != null) {
            for (Activity activity : tempActivityList) {
                activity.finish();
            }
            System.exit(0);
        }
    }
}
