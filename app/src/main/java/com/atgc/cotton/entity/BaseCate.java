package com.atgc.cotton.entity;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017-09-05.
 */
public class BaseCate {
    private ArrayList<CateEntity> Data = new ArrayList<>();

    public ArrayList<CateEntity> getData() {
        return Data;
    }

    public void setData(ArrayList<CateEntity> data) {
        Data = data;
    }
}
