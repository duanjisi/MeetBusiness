package com.atgc.cotton.entity;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/14.
 */
public class BaseComment {
    private ArrayList<Comment> Data = new ArrayList<>();

    public ArrayList<Comment> getData() {
        return Data;
    }

    public void setData(ArrayList<Comment> data) {
        Data = data;
    }
}
