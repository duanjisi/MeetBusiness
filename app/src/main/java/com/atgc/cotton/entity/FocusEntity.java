package com.atgc.cotton.entity;

/**
 * Created by Johnny on 2017/7/12.
 */
public class FocusEntity {
    private boolean IsFollow = false;
    private String Id = "";

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public boolean isFollow() {
        return IsFollow;
    }

    public void setFollow(boolean follow) {
        IsFollow = follow;
    }
}
