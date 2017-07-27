package com.atgc.cotton.entity;

import java.util.List;

/**
 * Created by liw on 2017/7/24.
 */

public class MsgEntity {


    /**
     * Status : 200
     * Code : 0
     * Name : MessageController
     * Message : success
     * Data : [{"Avatar":"http://ytcdn.66boss.com/data/yuetao/avatar/43/43fb69d9f358f775f06c00a848fa1f57.jpg ","FeedId":37,"FeedMediafile":"data/yuetao/video/2017/07/05/bf2d7f4f8e89a24c43287c37b862737e-640x640.mp4","Id":303,"MsgContent":"赞了你的作品","UidFrom":100000111,"UserName":"Vison-L萧潇"}]
     */

    private int Status;
    private int Code;
    private String Name;
    private String Message;
    private List<DataBean> Data;

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

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * Avatar : http://ytcdn.66boss.com/data/yuetao/avatar/43/43fb69d9f358f775f06c00a848fa1f57.jpg
         * FeedId : 37
         * FeedMediafile : data/yuetao/video/2017/07/05/bf2d7f4f8e89a24c43287c37b862737e-640x640.mp4
         * Id : 303
         * MsgContent : 赞了你的作品
         * UidFrom : 100000111
         * UserName : Vison-L萧潇
         */

        private String Avatar;
        private int FeedId;
        private String FeedMediafile;
        private int Id;
        private String MsgContent;
        private int UidFrom;
        private String UserName;

        public String getAvatar() {
            return Avatar;
        }

        public void setAvatar(String Avatar) {
            this.Avatar = Avatar;
        }

        public int getFeedId() {
            return FeedId;
        }

        public void setFeedId(int FeedId) {
            this.FeedId = FeedId;
        }

        public String getFeedMediafile() {
            return FeedMediafile;
        }

        public void setFeedMediafile(String FeedMediafile) {
            this.FeedMediafile = FeedMediafile;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getMsgContent() {
            return MsgContent;
        }

        public void setMsgContent(String MsgContent) {
            this.MsgContent = MsgContent;
        }

        public int getUidFrom() {
            return UidFrom;
        }

        public void setUidFrom(int UidFrom) {
            this.UidFrom = UidFrom;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }
    }
}
