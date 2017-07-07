package com.atgc.cotton.entity;

/**
 * Created by GMARUnity on 2017/6/28.
 */
public class OrderEntity {

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public String getGoodsPrice() {
        return GoodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        GoodsPrice = goodsPrice;
    }

    public String getGoodsNum() {
        return GoodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        GoodsNum = goodsNum;
    }

    public String getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(String allPrice) {
        this.allPrice = allPrice;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getGoodsContent() {
        return GoodsContent;
    }

    public void setGoodsContent(String goodsContent) {
        GoodsContent = goodsContent;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getHeadTag() {
        return headTag;
    }

    public void setHeadTag(int headTag) {
        this.headTag = headTag;
    }

    public int getFootTag() {
        return footTag;
    }

    public void setFootTag(int footTag) {
        this.footTag = footTag;
    }

    int contentType;//0:头 1：内容 2：尾
    String shopName;
    String GoodsName;
    String GoodsPrice;
    String GoodsNum;
    String GoodsContent;
    String allPrice;
    int allNum;
    int orderType;//"0:待付款", "1:待发货", "2:待收货", "3:待评价"
    int headTag;
    int footTag;

}
