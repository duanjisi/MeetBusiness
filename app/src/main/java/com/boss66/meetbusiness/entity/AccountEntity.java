package com.boss66.meetbusiness.entity;

/**
 * Created by Johnny on 2017/6/23.
 */
public class AccountEntity {
    private String Avatar = "";
    private String MobilePhone = "";
    private String Sex = "";
    private String Token = "";
    private String UserId = "";
    private String UserName = "";

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getMobilePhone() {
        return MobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        MobilePhone = mobilePhone;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

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

    @Override
    public String toString() {
        return "AccountEntity{" +
                "Avatar='" + Avatar + '\'' +
                ", MobilePhone='" + MobilePhone + '\'' +
                ", Sex='" + Sex + '\'' +
                ", Token='" + Token + '\'' +
                ", UserId='" + UserId + '\'' +
                ", UserName='" + UserName + '\'' +
                '}';
    }
}
