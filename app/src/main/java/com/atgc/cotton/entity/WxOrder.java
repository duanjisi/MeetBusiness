package com.atgc.cotton.entity;

/**
 * Created by Johnny on 2017/8/5.
 */
public class WxOrder {

    private int Status;
    private int Code;
    private String Name;
    private String Message;
    private PayWx Data;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public PayWx getData() {
        return Data;
    }

    public void setData(PayWx data) {
        Data = data;
    }
}
