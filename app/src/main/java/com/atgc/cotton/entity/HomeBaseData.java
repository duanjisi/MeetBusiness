package com.atgc.cotton.entity;


import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/7.
 */
public class HomeBaseData {
    private ArrayList<VideoEntity> Data = new ArrayList<>();

    public ArrayList<VideoEntity> getData() {
        return Data;
    }

    public void setData(ArrayList<VideoEntity> data) {
        Data = data;
    }
}
