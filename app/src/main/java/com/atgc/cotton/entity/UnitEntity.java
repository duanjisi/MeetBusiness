package com.atgc.cotton.entity;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017-12-06.
 */
public class UnitEntity {

    private String Message = "";
    private ArrayList<String> Data = new ArrayList<>();

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<String> getData() {
        return Data;
    }

    public void setData(ArrayList<String> data) {
        Data = data;
    }
}
