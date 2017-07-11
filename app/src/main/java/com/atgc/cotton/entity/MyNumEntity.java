package com.atgc.cotton.entity;

/**
 * Created by Johnny on 2017/7/10.
 */
public class MyNumEntity {
    private String UserId = "";
    private String FollowCount = "";
    private String FansCount = "";

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getFollowCount() {
        return FollowCount;
    }

    public void setFollowCount(String followCount) {
        FollowCount = followCount;
    }

    public String getFansCount() {
        return FansCount;
    }

    public void setFansCount(String fansCount) {
        FansCount = fansCount;
    }
}
