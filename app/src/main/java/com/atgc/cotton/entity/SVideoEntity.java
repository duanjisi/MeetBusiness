package com.atgc.cotton.entity;

/**
 * Created by Johnny on 2017-10-20.
 */
public class SVideoEntity {
    private String Id = "";
    private String Content = "";
    private String UserId = "";
    private String MediaPath = "";
    private String AddTime = "";

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMediaPath() {
        return MediaPath;
    }

    public void setMediaPath(String mediaPath) {
        MediaPath = mediaPath;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }
}
