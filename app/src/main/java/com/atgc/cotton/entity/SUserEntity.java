package com.atgc.cotton.entity;

/**
 * Created by Johnny on 2017-10-20.
 */
public class SUserEntity {

    private String UserId = "";
    private String UserName = "";
    private String Avatar = "";
    private String Signature = "";

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }
}
