package com.boss66.meetbusiness;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 2017/5/16.
 */
public class App extends Application {
    private static App mApplication;
    private List<Activity> tempActivityList;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public synchronized static App getInstance() {
        return mApplication;
    }


    public boolean isLogin() {
        return false;
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
