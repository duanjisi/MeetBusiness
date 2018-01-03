package com.atgc.cotton.entity;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017-10-20.
 */
public class BaseSGood {
    private ArrayList<SGoodEntity> Data = new ArrayList<>();

    public ArrayList<SGoodEntity> getData() {
        return Data;
    }

    public void setData(ArrayList<SGoodEntity> data) {
        Data = data;
    }
}
