package com.atgc.cotton.entity;

/**
 * 修改头像
 */
public class ChangeAvatarEntity {
    public String Name;
    public String Message;
    public int Code;
    public int Status;


    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        public String getAvatar() {
            return Avatar;
        }

        public void setAvatar(String avatar) {
            this.Avatar = avatar;
        }

        String Avatar;
    }
}
