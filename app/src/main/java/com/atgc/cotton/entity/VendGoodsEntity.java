package com.atgc.cotton.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GMARUnity on 2017/7/5.
 */
public class VendGoodsEntity implements Serializable {

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<VendGoodsEntity.Goods> getData() {
        return Data;
    }

    public void setData(List<VendGoodsEntity.Goods> data) {
        Data = data;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    private String Status;
    private int Code;
    private String Name;
    private String Message = "";
    private List<Goods> Data;

    public class Goods implements Serializable {

        public String getGoodsAttr() {
            return GoodsAttr;
        }

        public void setGoodsAttr(String goodsAttr) {
            GoodsAttr = goodsAttr;
        }

        public String getShopPrice() {
            return ShopPrice;
        }

        public void setShopPrice(String shopPrice) {
            ShopPrice = shopPrice;
        }

        public String getGoodsNumber() {
            return GoodsNumber;
        }

        public void setGoodsNumber(String goodsNumber) {
            GoodsNumber = goodsNumber;
        }

        public String getGoodsName() {
            return GoodsName;
        }

        public void setGoodsName(String goodsName) {
            GoodsName = goodsName;
        }

        public String getGoodsImg() {
            return GoodsImg;
        }

        public void setGoodsImg(String goodsImg) {
            GoodsImg = goodsImg;
        }

        public String getGoodsId() {
            return GoodsId;
        }

        public void setGoodsId(String goodsId) {
            GoodsId = goodsId;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        private String GoodsAttr;
        private String GoodsId;
        private String GoodsImg;
        private String GoodsName;
        private String GoodsNumber;
        private String ShopPrice;
        private boolean selected = false;
    }
}
