package com.atgc.cotton.entity;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017-08-26.
 */
public class BaseMember {

    private ArrayList<MemberEntity> Data = new ArrayList<>();

    public ArrayList<MemberEntity> getData() {
        return Data;
    }

    public void setData(ArrayList<MemberEntity> data) {
        Data = data;
    }
}
