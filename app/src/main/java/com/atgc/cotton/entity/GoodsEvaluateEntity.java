package com.atgc.cotton.entity;

import java.util.List;

/**
 * Created by liw on 2017/7/26.
 */

public class GoodsEvaluateEntity {

    /**
     * Status : 200
     * Code : 0
     * Name : PublicController
     * Message : success
     * Data : [{"AddTime":1500363218,"Avatar":"http://api.hmg66.ccc/data/yuetao/avatar/49/9e730b6110583bbd93f01b7e942b7194.jpg ","Content":"测试评论商品","GoodsId":21,"Id":10,"OrderId":17,"Pics":["data/yuetao/goodscomm/2017/07/18/d9253285bf898ba2478c2e817376fb91.jpg","data/yuetao/goodscomm/2017/07/18/37d473e6ddd9a12d2adf7c748e2ee2ef.jpeg"],"Score":5,"UserId":100000000,"UserName":"无怨无悔"}]
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
         * AddTime : 1500363218
         * Avatar : http://api.hmg66.ccc/data/yuetao/avatar/49/9e730b6110583bbd93f01b7e942b7194.jpg
         * Content : 测试评论商品
         * GoodsId : 21
         * Id : 10
         * OrderId : 17
         * Pics : ["data/yuetao/goodscomm/2017/07/18/d9253285bf898ba2478c2e817376fb91.jpg","data/yuetao/goodscomm/2017/07/18/37d473e6ddd9a12d2adf7c748e2ee2ef.jpeg"]
         * Score : 5
         * UserId : 100000000
         * UserName : 无怨无悔
         */

        private int AddTime;
        private String Avatar;
        private String Content;
        private int GoodsId;
        private int Id;
        private int OrderId;
        private int Score;
        private int UserId;
        private String UserName;
        private List<String> Pics;

        public int getAddTime() {
            return AddTime;
        }

        public void setAddTime(int AddTime) {
            this.AddTime = AddTime;
        }

        public String getAvatar() {
            return Avatar;
        }

        public void setAvatar(String Avatar) {
            this.Avatar = Avatar;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public int getGoodsId() {
            return GoodsId;
        }

        public void setGoodsId(int GoodsId) {
            this.GoodsId = GoodsId;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getOrderId() {
            return OrderId;
        }

        public void setOrderId(int OrderId) {
            this.OrderId = OrderId;
        }

        public int getScore() {
            return Score;
        }

        public void setScore(int Score) {
            this.Score = Score;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public List<String> getPics() {
            return Pics;
        }

        public void setPics(List<String> Pics) {
            this.Pics = Pics;
        }
    }
}
