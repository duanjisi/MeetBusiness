package com.boss66.meetbusiness.entity;

/**
 * Created by liw on 2017/6/29.
 */

public class FilterEntity {
    private int filter;
    private String name;

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FilterEntity(int filter, String name) {
        this.filter = filter;
        this.name = name;
    }
}
