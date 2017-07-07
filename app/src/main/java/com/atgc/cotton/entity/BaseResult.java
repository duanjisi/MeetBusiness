package com.atgc.cotton.entity;

import android.util.Log;

import com.alibaba.fastjson.JSON;

/**
 * Created by GMARUnity on 2017/7/6.
 */
public class BaseResult {

    private String Name;
    private String Message;
    private int Code;

    public void setStatus(int status) {
        Status = status;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private int Status;

    public String getData() {
        return data;
    }

    private String data;
    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        this.Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public int getStatus() {
        return Status;
    }

    public static BaseResult parse(String json) {
        BaseResult res = new BaseResult();
        try {
            res = JSON.parseObject(json, BaseResult.class);
        } catch (Exception e) {
            Log.e("Json Error", e.toString());
        }
        return res;
    }

}
