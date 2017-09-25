package com.atgc.cotton.entity;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017-09-05.
 */
public class BaseOnline {
    private ArrayList<OnlineVoiceEntity> Data = new ArrayList<>();

    public ArrayList<OnlineVoiceEntity> getData() {
        return Data;
    }

    public void setData(ArrayList<OnlineVoiceEntity> data) {
        Data = data;
    }
}
