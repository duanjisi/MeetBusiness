package com.atgc.cotton.entity;

/**
 * Created by Johnny on 2016/7/11.
 */
public class BaseConversation {
    private String conversation_id = "";//如果单聊则是用户id,群聊则是群id
    private String user_name = "";//如果单聊则是用户名,群聊则是群名
    private String avatar = "";
    private String unread_msg_count = "";
    private String newest_msg_time = "";
    private String newest_msg_type = "";//unicast, group 单聊，群聊
    private String newest_msg = "";

    public BaseConversation() {
    }

    public BaseConversation(String user_name, String avatar, String newest_msg) {
        this.user_name = user_name;
        this.avatar = avatar;
        this.newest_msg = newest_msg;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getUnread_msg_count() {
        return unread_msg_count;
    }

    public void setUnread_msg_count(String unread_msg_count) {
        this.unread_msg_count = unread_msg_count;
    }

    public String getNewest_msg_time() {
        return newest_msg_time;
    }

    public void setNewest_msg_time(String newest_msg_time) {
        this.newest_msg_time = newest_msg_time;
    }

    public String getNewest_msg_type() {
        return newest_msg_type;
    }

    public void setNewest_msg_type(String newest_msg_type) {
        this.newest_msg_type = newest_msg_type;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getNewest_msg() {
        return newest_msg;
    }

    public void setNewest_msg(String newest_msg) {
        this.newest_msg = newest_msg;
    }
}
