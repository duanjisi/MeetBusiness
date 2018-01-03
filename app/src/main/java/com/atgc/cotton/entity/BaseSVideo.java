package com.atgc.cotton.entity;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017-10-20.
 */
public class BaseSVideo {
    private ArrayList<SVideoEntity> Data = new ArrayList<>();

    public ArrayList<SVideoEntity> getData() {
        return Data;
    }

    public void setData(ArrayList<SVideoEntity> data) {
        Data = data;
    }
}
