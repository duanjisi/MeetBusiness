package com.atgc.cotton.entity;

import java.io.Serializable;

/**
 * <p>描述：
 * <p>作者：duanjisi 2018年 01月 24日
 */

public class AgentParam implements Serializable {
    private int id = 0;
    private String truename = "";
    private String idcartno = "";
    private String mobilephone = "";
    private String smscode = "";
    private String idcardp = "";
    private String idcardo = "";
    private String idcardm = "";
    private String account_user = "";
    private String account = "";
    private String bank = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getIdcartno() {
        return idcartno;
    }

    public void setIdcartno(String idcartno) {
        this.idcartno = idcartno;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getSmscode() {
        return smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

    public String getIdcardp() {
        return idcardp;
    }

    public void setIdcardp(String idcardp) {
        this.idcardp = idcardp;
    }

    public String getIdcardo() {
        return idcardo;
    }

    public void setIdcardo(String idcardo) {
        this.idcardo = idcardo;
    }

    public String getIdcardm() {
        return idcardm;
    }

    public void setIdcardm(String idcardm) {
        this.idcardm = idcardm;
    }

    public String getAccount_user() {
        return account_user;
    }

    public void setAccount_user(String account_user) {
        this.account_user = account_user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
