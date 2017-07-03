package com.boss66.meetbusiness.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Johnny on 2017/7/3.
 */
public class AuthEntity {
    private String RetMsg = "";
    @JSONField(name = "x-amz-date")
    private String amz = "";
    private String Authorization = "";
    private String RetCode = "";

    public String getRetMsg() {
        return RetMsg;
    }

    public void setRetMsg(String retMsg) {
        RetMsg = retMsg;
    }

    public String getAmz() {
        return amz;
    }

    public void setAmz(String amz) {
        this.amz = amz;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }

    public String getRetCode() {
        return RetCode;
    }

    public void setRetCode(String retCode) {
        RetCode = retCode;
    }
}
