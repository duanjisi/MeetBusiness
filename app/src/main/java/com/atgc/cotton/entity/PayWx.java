package com.atgc.cotton.entity;

/**
 * Created by Johnny on 2017/8/5.
 */
public class PayWx {
    private String Appid = "";
    private String Noncestr = "";
    private String Package = "";
    private String Partnerid = "";
    private String Prepayid = "";
    private String Sign = "";
    private String Timestamp = "";

    public String getAppid() {
        return Appid;
    }

    public void setAppid(String appid) {
        Appid = appid;
    }

    public String getNoncestr() {
        return Noncestr;
    }

    public void setNoncestr(String noncestr) {
        Noncestr = noncestr;
    }

    public String getPackage() {
        return Package;
    }

    public void setPackage(String aPackage) {
        Package = aPackage;
    }

    public String getPartnerid() {
        return Partnerid;
    }

    public void setPartnerid(String partnerid) {
        Partnerid = partnerid;
    }

    public String getPrepayid() {
        return Prepayid;
    }

    public void setPrepayid(String prepayid) {
        Prepayid = prepayid;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
