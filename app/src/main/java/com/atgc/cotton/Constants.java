package com.atgc.cotton;

import android.os.Environment;

/**
 * Created by Johnny on 2017/5/16.
 */
public class Constants {
    public static final String CACHE_IMG_DIR = Environment.getExternalStorageDirectory() + "/meetBus/";
    public static final String ACTION_LOGOUT_RESETING = "com.boss66.meetbusiness.logout.reseting";

    public static final class Action {
        public static final String UPDATE_ACCOUNT_INFORM = "com.atgc.cotton.update.account.inform";
    }
}
