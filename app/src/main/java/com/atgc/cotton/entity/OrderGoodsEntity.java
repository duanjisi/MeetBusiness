package com.atgc.cotton.entity;

import java.io.Serializable;

/**
 * Created by GMARUnity on 2017/7/15.
 */
public class OrderGoodsEntity implements Serializable {
    private int BuyNumber;
    private String GoodsAttr;
    private int GoodsId;
    private String GoodsImg;
    private String GoodsName;
    private double ShopPrice;

    private float OrderAmount;
    private int OrderId;
    private String OrderSn;
    private String Consignee;
    private String Mobile;
    private int contentType;//0:头 1：内容 2：尾
    private int allNum;
    private int ShippingStatus;
    private int PayStatus;
    private int OrderStatus;

    private String OrdState;

    private String SupplierAvatar;
    private String SupplierSex;//商家性别
    private String SupplierSignture;//商家签名


    private String SupplierName;//店铺名字
    private int SupplierId;//店铺id
    int orderType;//"0:待付款", "1:待发货", "2:待收货", "3:待评价"

    private int Province;
    private int City;
    private int District;
    private String Address;
    private String goodsJson;

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getConsignee() {
        return Consignee;
    }

    public void setConsignee(String consignee) {
        Consignee = consignee;
    }

    public String getOrdState() {
        return OrdState;
    }

    public void setOrdState(String ordState) {
        OrdState = ordState;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getShippingStatus() {
        return ShippingStatus;
    }

    public void setShippingStatus(int shippingStatus) {
        ShippingStatus = shippingStatus;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public int getPayStatus() {
        return PayStatus;
    }

    public void setPayStatus(int payStatus) {
        PayStatus = payStatus;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public float getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(float orderAmount) {
        OrderAmount = orderAmount;
    }

    public String getOrderSn() {
        return OrderSn;
    }

    public void setOrderSn(String orderSn) {
        OrderSn = orderSn;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getStoreName() {
        return SupplierName;
    }

    public void setStoreName(String storeName) {
        this.SupplierName = storeName;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getBuyNumber() {
        return BuyNumber;
    }

    public void setBuyNumber(int BuyNumber) {
        this.BuyNumber = BuyNumber;
    }

    public String getGoodsAttr() {
        return GoodsAttr;
    }

    public void setGoodsAttr(String GoodsAttr) {
        this.GoodsAttr = GoodsAttr;
    }

    public int getGoodsId() {
        return GoodsId;
    }

    public void setGoodsId(int GoodsId) {
        this.GoodsId = GoodsId;
    }

    public String getGoodsImg() {
        return GoodsImg;
    }

    public void setGoodsImg(String GoodsImg) {
        this.GoodsImg = GoodsImg;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String GoodsName) {
        this.GoodsName = GoodsName;
    }

    public double getShopPrice() {
        return ShopPrice;
    }

    public void setShopPrice(double ShopPrice) {
        this.ShopPrice = ShopPrice;
    }

    public int getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(int supplierId) {
        SupplierId = supplierId;
    }

    public int getProvince() {
        return Province;
    }

    public void setProvince(int province) {
        Province = province;
    }

    public int getCity() {
        return City;
    }

    public void setCity(int city) {
        City = city;
    }

    public int getDistrict() {
        return District;
    }

    public void setDistrict(int district) {
        District = district;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSupplierAvatar() {
        return SupplierAvatar;
    }

    public void setSupplierAvatar(String supplierAvatar) {
        SupplierAvatar = supplierAvatar;
    }

    public String getSupplierSex() {
        return SupplierSex;
    }

    public void setSupplierSex(String supplierSex) {
        SupplierSex = supplierSex;
    }

    public String getSupplierSignture() {
        return SupplierSignture;
    }

    public void setSupplierSignture(String supplierSignture) {
        SupplierSignture = supplierSignture;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getGoodsJson() {
        return goodsJson;
    }

    public void setGoodsJson(String goodsJson) {
        this.goodsJson = goodsJson;
    }
}
