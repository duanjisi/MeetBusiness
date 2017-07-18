package com.atgc.cotton.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by liw on 2017/7/15.
 */
@Table(name="OrderGoods")
public class OrderGoodsEntity {
    @Column(column = "_id")
    @Id(column = "_id")
    private int _id;
    @Column(column = "buyNum")
    public int buyNum;      //购买数量
    @Column(column = "type")
    public String type;     //类别
    @Column(column = "imgUrl")
    public String imgUrl;   //商品图
    @Column(column = "goodsName")
    public String goodsName;    //商品名
    @Column(column = "title")
    public String title;     //店铺名
    @Column(column = "goodsPrice")
    public Double goodsPrice; //商品价格
    @Column(column = "goodsId")
    public Integer goodsId;  //商品id
    @Column(column = "head")
    public int head=0;  //是否显示标题    0是普通布局 1是店铺布局
    @Column(column = "head")
    public boolean check;//是否选中

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    @Override
    public String toString() {
        return "OrderGoodsEntity{" +
                "_id=" + _id +
                ", buyNum=" + buyNum +
                ", type='" + type + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", title='" + title + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsId=" + goodsId +
                ", head=" + head +
                '}';
    }
}
