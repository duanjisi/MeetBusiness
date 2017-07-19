package com.atgc.cotton.entity;

import java.util.List;

/**
 * Created by GMARUnity on 2017/7/11.
 */
public class GoodsDetailEntity {
    /**
     * Status : 200
     * Code : 0
     * Name : PublicController
     * Message : success
     * Data : {"Avatar":"http://ytcdn.66boss.com/","GoodsAttr":[{"AttrId":107,"AttrName":"颜色","AttrValue":"蓝色"}],"GoodsDesc":"","GoodsGallery":[{"ImgId":89,"ImgUrl":"http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/1553d6b58008e07d3c05133f00f1e0aa.jpg"},{"ImgId":90,"ImgUrl":"http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/cfaf98585b35129d4a7d9c64bab6d445.jpg"}],"GoodsId":47,"GoodsName":"%E5%97%A8%E8%90%8C","GoodsNumber":21,"GoodsPrice":888,"UserId":"100000096","UserName":"用户_6030"}
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
         * Avatar : http://ytcdn.66boss.com/
         * GoodsAttr : [{"AttrId":107,"AttrName":"颜色","AttrValue":"蓝色"}]
         * GoodsDesc :
         * GoodsGallery : [{"ImgId":89,"ImgUrl":"http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/1553d6b58008e07d3c05133f00f1e0aa.jpg"},{"ImgId":90,"ImgUrl":"http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/cfaf98585b35129d4a7d9c64bab6d445.jpg"}]
         * GoodsId : 47
         * GoodsName : %E5%97%A8%E8%90%8C
         * GoodsNumber : 21
         * GoodsPrice : 888
         * UserId : 100000096
         * UserName : 用户_6030
         */

        private String Avatar;
        private String GoodsDesc;
        private int GoodsId;
        private String GoodsName;
        private int GoodsNumber;
        private Double GoodsPrice;
        private String UserId;
        private String UserName;
        private List<VendGoodsAttrEntity> GoodsAttr;
        private List<GoodsGalleryBean> GoodsGallery;

        public String getAvatar() {
            return Avatar;
        }

        public void setAvatar(String Avatar) {
            this.Avatar = Avatar;
        }

        public String getGoodsDesc() {
            return GoodsDesc;
        }

        public void setGoodsDesc(String GoodsDesc) {
            this.GoodsDesc = GoodsDesc;
        }

        public int getGoodsId() {
            return GoodsId;
        }

        public void setGoodsId(int GoodsId) {
            this.GoodsId = GoodsId;
        }

        public String getGoodsName() {
            return GoodsName;
        }

        public void setGoodsName(String GoodsName) {
            this.GoodsName = GoodsName;
        }

        public int getGoodsNumber() {
            return GoodsNumber;
        }

        public void setGoodsNumber(int GoodsNumber) {
            this.GoodsNumber = GoodsNumber;
        }

        public Double getGoodsPrice() {
            return GoodsPrice;
        }

        public void setGoodsPrice(Double GoodsPrice) {
            this.GoodsPrice = GoodsPrice;
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

        public List<VendGoodsAttrEntity> getGoodsAttr() {
            return GoodsAttr;
        }

        public void setGoodsAttr(List<VendGoodsAttrEntity> GoodsAttr) {
            this.GoodsAttr = GoodsAttr;
        }

        public List<GoodsGalleryBean> getGoodsGallery() {
            return GoodsGallery;
        }

        public void setGoodsGallery(List<GoodsGalleryBean> GoodsGallery) {
            this.GoodsGallery = GoodsGallery;
        }

        public static class GoodsGalleryBean {
            /**
             * ImgId : 89
             * ImgUrl : http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/1553d6b58008e07d3c05133f00f1e0aa.jpg
             */

            private int ImgId;
            private String ImgUrl;

            public int getImgId() {
                return ImgId;
            }

            public void setImgId(int ImgId) {
                this.ImgId = ImgId;
            }

            public String getImgUrl() {
                return ImgUrl;
            }

            public void setImgUrl(String ImgUrl) {
                this.ImgUrl = ImgUrl;
            }
        }
    }
}
