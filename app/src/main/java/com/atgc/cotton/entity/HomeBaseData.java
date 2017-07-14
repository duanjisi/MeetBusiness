package com.atgc.cotton.entity;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/7.
 */
public class HomeBaseData {
    @JSONField(name = "Data")
    private ArrayList<VideoEntity> Data = new ArrayList<>();

    public ArrayList<VideoEntity> getData() {
        return Data;
    }

    public void setData(ArrayList<VideoEntity> data) {
        Data = data;
    }
}
