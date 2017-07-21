package com.atgc.cotton.entity;

/**
 * Created by GMARUnity on 2017/7/15.
 */
public class OrderActionEntity {
    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private int page;
    private int child;
    private boolean isRefresh;
    private boolean isBuy;
    private String doAction;
    private int orderid;

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getDoAction() {
        return doAction;
    }

    public void setDoAction(String doAction) {
        this.doAction = doAction;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }
}
