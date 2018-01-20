package com.atgc.cotton.entity;

/**
 * Created by Johnny on 2018-01-20.
 */
public class AgentInfo {
    private String Id = "";
    private String UserId = "";
    private String IdCardNo = "";
    private String IdCardP = "";
    private String IdCardM = "";
    private String Account = "";
    private String AccountUser = "";
    private String AddTime = "";
    private String LastTime = "";
    private String CheckTime = "";
    private String Reason = "";
    private String Status = "";

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getIdCardNo() {
        return IdCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        IdCardNo = idCardNo;
    }

    public String getIdCardP() {
        return IdCardP;
    }

    public void setIdCardP(String idCardP) {
        IdCardP = idCardP;
    }

    public String getIdCardM() {
        return IdCardM;
    }

    public void setIdCardM(String idCardM) {
        IdCardM = idCardM;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getAccountUser() {
        return AccountUser;
    }

    public void setAccountUser(String accountUser) {
        AccountUser = accountUser;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getLastTime() {
        return LastTime;
    }

    public void setLastTime(String lastTime) {
        LastTime = lastTime;
    }

    public String getCheckTime() {
        return CheckTime;
    }

    public void setCheckTime(String checkTime) {
        CheckTime = checkTime;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
