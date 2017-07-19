package com.atgc.cotton.entity;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/14.
 */
public class BaseGood {
    private ArrayList<GoodBean> Data = new ArrayList<>();

    public ArrayList<GoodBean> getData() {
        return Data;
    }

    public void setData(ArrayList<GoodBean> data) {
        Data = data;
    }
}
