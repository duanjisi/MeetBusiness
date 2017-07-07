package com.atgc.cotton.entity;

/**
 * Created by liw on 2017/7/7.
 */

public class UserEntity {

    /**
     * Status : 200
     * Code : 0
     * Name : PublicController
     * Message : 登录成功
     * Data : {"Avatar":"http://apicdn.hmg66.com/","MobilePhone":"15323339887","Sex":"0","Token":"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOjEwMDAwMDEwNywiZXhwIjoxNTE0OTc0MjM3LCJpc3MiOiJ6aGVuZ2NvZ0BnbWFpbC5jb20iLCJuYmYiOjE0OTk0MjIyMzd9.7Rh3H10wg89s_u6kREH55ypjns-EMqwAmvD-v6rZTIw","UserId":"100000107","UserName":"用户_9887"}
     */

    private int Status;
    private int Code;
    private String Name;
    private String Message;
    private DataBean Data;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * Avatar : http://apicdn.hmg66.com/
         * MobilePhone : 15323339887
         * Sex : 0
         * Token : Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOjEwMDAwMDEwNywiZXhwIjoxNTE0OTc0MjM3LCJpc3MiOiJ6aGVuZ2NvZ0BnbWFpbC5jb20iLCJuYmYiOjE0OTk0MjIyMzd9.7Rh3H10wg89s_u6kREH55ypjns-EMqwAmvD-v6rZTIw
         * UserId : 100000107
         * UserName : 用户_9887
         */

        private String Avatar;
        private String MobilePhone;
        private String Sex;
        private String Token;
        private String UserId;
        private String UserName;

        public String getAvatar() {
            return Avatar;
        }

        public void setAvatar(String Avatar) {
            this.Avatar = Avatar;
        }

        public String getMobilePhone() {
            return MobilePhone;
        }

        public void setMobilePhone(String MobilePhone) {
            this.MobilePhone = MobilePhone;
        }

        public String getSex() {
            return Sex;
        }

        public void setSex(String Sex) {
            this.Sex = Sex;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String Token) {
            this.Token = Token;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }
    }
}
