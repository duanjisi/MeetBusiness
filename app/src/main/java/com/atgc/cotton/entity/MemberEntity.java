package com.atgc.cotton.entity;

import java.io.Serializable;

/**
 * Created by Johnny on 2017-08-26.
 */
public class MemberEntity implements Serializable {
    private String Avatar = "";
    private String SourceFrom = "";
    private String UidFrom = "";
    private String UidTo = "";
    private String Sex = "";
    private String UserName = "";
    private String UserId = "";
    private String Signature = "";

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getSourceFrom() {
        return SourceFrom;
    }

    public void setSourceFrom(String sourceFrom) {
        SourceFrom = sourceFrom;
    }

    public String getUidFrom() {
        return UidFrom;
    }

    public void setUidFrom(String uidFrom) {
        UidFrom = uidFrom;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUidTo() {
        return UidTo;
    }

    public void setUidTo(String uidTo) {
        UidTo = uidTo;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }
}
