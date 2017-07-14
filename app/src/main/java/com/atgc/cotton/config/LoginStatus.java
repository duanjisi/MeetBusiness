package com.atgc.cotton.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.atgc.cotton.App;
import com.atgc.cotton.entity.AccountEntity;

/**
 * Created by Johnny on 2016/7/5.
 */
public class LoginStatus {
    public static final String FILE_NAME = "login_status.xml";
    private static final String LOGIN_STATE = "login_state";
    private static final String AVATAR = "Avatar";
    private static final String PHONE = "MobilePhone";
    private static final String SEX = "Sex";
    private static final String TOKEN = "Token";
    private static final String USERID = "UserId";
    private static final String USERNAME = "UserName";
    private static final String INTRO = "Introduction";
    private static LoginStatus sLoginStatus;
    private SharedPreferences mPreferences;

    private LoginStatus() {
        mPreferences = App.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static LoginStatus getInstance() {
        if (sLoginStatus == null) {
            sLoginStatus = new LoginStatus();
        }
        return sLoginStatus;
    }

    public void login(AccountEntity account, boolean isThirdParty) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(AVATAR, account.getAvatar());
        editor.putString(PHONE, account.getMobilePhone());
        editor.putString(SEX, account.getSex());
        editor.putBoolean(LOGIN_STATE, true);
        editor.putString(TOKEN, account.getToken());
        editor.putString(USERID, account.getUserId());
        editor.putString(USERNAME, account.getUserName());
        editor.apply();
    }

    public void logout() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(LOGIN_STATE, false);
        editor.apply();
    }

    public boolean hadLogged() {
        return mPreferences.getBoolean(LOGIN_STATE, false);
    }

    public void clear() {
        mPreferences.edit().clear().apply();
    }

    public String getAvatar() {
        return mPreferences.getString(AVATAR, "");
    }

    public String getPhone() {
        return mPreferences.getString(PHONE, "");
    }

    public String getSex() {
        return mPreferences.getString(SEX, "");
    }

    public String getToken() {
        return mPreferences.getString(TOKEN, "");
    }

    public String getUserid() {
        return mPreferences.getString(USERID, "");
    }

    public String getUsername() {
        return mPreferences.getString(USERNAME, "");
    }

    public String getIntro() {
        return mPreferences.getString(INTRO, "");
    }

    public void setUser_name(String name) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(USERNAME, name);
        editor.apply();
    }


    public void setAvatar(String avatar) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(AVATAR, avatar);
        editor.apply();
    }

    public void setSex(String sex) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(SEX, sex);
        editor.apply();
    }

    public void setIntro(String intro) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(INTRO, intro);
        editor.apply();
    }

}
