package com.atgc.cotton;

import android.os.Environment;

/**
 * Created by Johnny on 2017/5/16.
 */
public class Constants {
    public static final String CACHE_IMG_DIR = Environment.getExternalStorageDirectory() + "/meetBus/";
    public static final String ACTION_LOGOUT_RESETING = "com.boss66.meetbusiness.logout.reseting";
    public static final String INIT_ADDRESS_DATA = "com.atgc.cotton.initdata";
    public static final String WX_POINT_PAY_KEY = "com.atgc.cotton.wx.point.pay";

    public static final class Action {
        //显示主页
        public static final String SHOW_HOME_ACTION = "com.atgc.cotton.SHOW_HOME_ACTION";
        public static final String UPDATE_ACCOUNT_INFORM = "com.atgc.cotton.update.account.inform";
        public static final String EXIT_CURRENT_ACTIVITY = "com.atgc.cotton.exit.current.activity";
        public static final String PHONE_BIND_STATE = "com.atgc.cotton.phone.bind.state";
        public static final String SELECTED_CURRENT_MUSIC = "com.atgc.cotton.selected.current.music";
        public static final String NET_CONENECT_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    }
}
