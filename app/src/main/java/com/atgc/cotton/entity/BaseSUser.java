package com.atgc.cotton.entity;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017-10-20.
 */
public class BaseSUser {
    private ArrayList<SUserEntity> Data = new ArrayList<>();

    public ArrayList<SUserEntity> getData() {
        return Data;
    }

    public void setData(ArrayList<SUserEntity> data) {
        Data = data;
    }
}
