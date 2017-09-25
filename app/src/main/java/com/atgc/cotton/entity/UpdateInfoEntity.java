package com.atgc.cotton.entity;

/**
 * Created by Johnny on 2016/9/21.
 */
public class UpdateInfoEntity {
//    private String version = "";
//    private String up_time = "";
//    private String msg = "";
//    private int force = 0;
//    private String url = "";

    private String APKUrl = "";
    private String AppVersion = "";
    private boolean UpForce = false;
    private String UpMsg = "";
    private String UpTime = "";

    public String getAPKUrl() {
        return APKUrl;
    }

    public void setAPKUrl(String APKUrl) {
        this.APKUrl = APKUrl;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }

    public boolean isUpForce() {
        return UpForce;
    }

    public void setUpForce(boolean upForce) {
        UpForce = upForce;
    }

    public String getUpMsg() {
        return UpMsg;
    }

    public void setUpMsg(String upMsg) {
        UpMsg = upMsg;
    }

    public String getUpTime() {
        return UpTime;
    }

    public void setUpTime(String upTime) {
        UpTime = upTime;
    }
}
