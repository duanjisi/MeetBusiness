package com.atgc.cotton.entity;

/**
 * Created by Johnny on 2017/4/20.
 */
public class ActionEntity {
    //    private int index = 0;
    private String action = "";
    private Object data = null;

    public ActionEntity(String action) {
        this.action = action;
    }

    public ActionEntity(String action, Object data) {
        this.action = action;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getAction() {
        return action;
    }

}
