package com.atgc.cotton.entity;

import java.util.List;

/**
 * Created by GMARUnity on 2017/7/15.
 */
public class OrderEvaluateEntity {

    /**
     * Status : 200
     * Code : 0
     * Name : GoodsController
     * Message : success
     * Data : [{"GoodsAttr":"规格：6666","GoodsId":52,"GoodsImg":"http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/8a43f6bc6965d6921273ce4e316b06e1.jpg","GoodsName":"ablution","GoodsNumber":26,"ShopPrice":12345678},{"GoodsAttr":"规格：666666 颜色：白色","GoodsId":49,"GoodsImg":"http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/a0e09cc26b9d5ec08f8e164d7b3c9165.jpg","GoodsName":"得得撸","GoodsNumber":16,"ShopPrice":16},{"GoodsAttr":"颜色：蓝色","GoodsId":47,"GoodsImg":"http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/1553d6b58008e07d3c05133f00f1e0aa.jpg","GoodsName":"%E5%97%A8%E8%90%8C","GoodsNumber":21,"ShopPrice":888},{"GoodsAttr":"型号：大号","GoodsId":26,"GoodsImg":"http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/8c7ef2fd658f4a56c575001bb54d36ea.jpg","GoodsName":"啦咯啦咯啦咯","GoodsNumber":666,"ShopPrice":666}]
     */

    private int Status;
    private int Code;
    private String Name;
    private String Message;
    private List<OrderGoodsEntity> Data;

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

    public List<OrderGoodsEntity> getData() {
        return Data;
    }

    public void setData(List<OrderGoodsEntity> Data) {
        this.Data = Data;
    }
}
