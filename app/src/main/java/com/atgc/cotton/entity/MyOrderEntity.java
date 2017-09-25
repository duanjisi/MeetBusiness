package com.atgc.cotton.entity;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by GMARUnity on 2017/7/15.
 */
public class MyOrderEntity {

    /**
     * Status : 200
     * Code : 0
     * Name : OrderController
     * Message : success
     * Data : [{"AddTime":1500088547,"Address":"11","City":76,"Consignee":"测试","District":696,"ExpressName":"","ExpressNo":"","GoodsList":[{"BuyNumber":1,"GoodsAttr":"规格：666666|颜色：白色","GoodsId":49,"GoodsImg":"http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/a0e09cc26b9d5ec08f8e164d7b3c9165.jpg","GoodsName":"得得撸","ShopPrice":16},{"BuyNumber":1,"GoodsAttr":"规格：6666","GoodsId":52,"GoodsImg":"http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/8a43f6bc6965d6921273ce4e316b06e1.jpg","GoodsName":"ablution","ShopPrice":12345678}],"Mobile":"18202093752","OrderAmount":12345694,"OrderId":21,"OrderSn":"631835529822","OrderStatus":2,"PayStatus":1,"PayTime":0,"PayType":1,"Postscript":"","Province":6,"ShippingStatus":1,"ShippingTime":0,"SupplierId":100000096,"UserId":100000096}]
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
         * AddTime : 1500088547
         * Address : 11
         * City : 76
         * Consignee : 测试
         * District : 696
         * ExpressName :
         * ExpressNo :
         * GoodsList : [{"BuyNumber":1,"GoodsAttr":"规格：666666|颜色：白色","GoodsId":49,"GoodsImg":"http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/a0e09cc26b9d5ec08f8e164d7b3c9165.jpg","GoodsName":"得得撸","ShopPrice":16},{"BuyNumber":1,"GoodsAttr":"规格：6666","GoodsId":52,"GoodsImg":"http://ytcdn.66boss.com/data/yuetao/goods/2017/07/11/8a43f6bc6965d6921273ce4e316b06e1.jpg","GoodsName":"ablution","ShopPrice":12345678}]
         * Mobile : 18202093752
         * OrderAmount : 12345694
         * OrderId : 21
         * OrderSn : 631835529822
         * OrderStatus : 2
         * PayStatus : 1
         * PayTime : 0
         * PayType : 1
         * Postscript :
         * Province : 6
         * ShippingStatus : 1
         * ShippingTime : 0
         * SupplierId : 100000096
         * UserId : 100000096
         */
        private String OrdState;
        private int AddTime;
        private String Address;
        private int City;
        private String Consignee;
        private int District;
        private String ExpressName;
        private String ExpressNo;
        private String Mobile;
        private float OrderAmount;
        private int OrderId;
        private String OrderSn;
        private int OrderStatus;
        private int PayStatus;
        private int PayTime;
        private int PayType;
        private String Postscript;
        private int Province;
        private int ShippingStatus;
        private int ShippingTime;
        private int SupplierId;////店铺id
        private int UserId;
        private String SupplierName;//店铺名字
        private String SupplierAvatar;
        private String SupplierSex;//商家性别
        private String SupplierSignture;//商家签名

        public String getOrdState() {
            return OrdState;
        }

        public void setOrdState(String ordState) {
            OrdState = ordState;
        }

        public String getSupplierName() {
            return SupplierName;
        }

        public void setSupplierName(String supplierName) {
            SupplierName = supplierName;
        }

        private List<OrderGoodsEntity> GoodsList;

        public int getAddTime() {
            return AddTime;
        }

        public void setAddTime(int AddTime) {
            this.AddTime = AddTime;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public int getCity() {
            return City;
        }

        public void setCity(int City) {
            this.City = City;
        }

        public String getConsignee() {
            return Consignee;
        }

        public void setConsignee(String Consignee) {
            this.Consignee = Consignee;
        }

        public int getDistrict() {
            return District;
        }

        public void setDistrict(int District) {
            this.District = District;
        }

        public String getExpressName() {
            return ExpressName;
        }

        public void setExpressName(String ExpressName) {
            this.ExpressName = ExpressName;
        }

        public String getExpressNo() {
            return ExpressNo;
        }

        public void setExpressNo(String ExpressNo) {
            this.ExpressNo = ExpressNo;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public float getOrderAmount() {
            return OrderAmount;
        }

        public void setOrderAmount(float OrderAmount) {
            this.OrderAmount = OrderAmount;
        }

        public int getOrderId() {
            return OrderId;
        }

        public void setOrderId(int OrderId) {
            this.OrderId = OrderId;
        }

        public String getOrderSn() {
            return OrderSn;
        }

        public void setOrderSn(String OrderSn) {
            this.OrderSn = OrderSn;
        }

        public int getOrderStatus() {
            return OrderStatus;
        }

        public void setOrderStatus(int OrderStatus) {
            this.OrderStatus = OrderStatus;
        }

        public int getPayStatus() {
            return PayStatus;
        }

        public void setPayStatus(int PayStatus) {
            this.PayStatus = PayStatus;
        }

        public int getPayTime() {
            return PayTime;
        }

        public void setPayTime(int PayTime) {
            this.PayTime = PayTime;
        }

        public int getPayType() {
            return PayType;
        }

        public void setPayType(int PayType) {
            this.PayType = PayType;
        }

        public String getPostscript() {
            return Postscript;
        }

        public void setPostscript(String Postscript) {
            this.Postscript = Postscript;
        }

        public int getProvince() {
            return Province;
        }

        public void setProvince(int Province) {
            this.Province = Province;
        }

        public int getShippingStatus() {
            return ShippingStatus;
        }

        public void setShippingStatus(int ShippingStatus) {
            this.ShippingStatus = ShippingStatus;
        }

        public int getShippingTime() {
            return ShippingTime;
        }

        public void setShippingTime(int ShippingTime) {
            this.ShippingTime = ShippingTime;
        }

        public int getSupplierId() {
            return SupplierId;
        }

        public void setSupplierId(int SupplierId) {
            this.SupplierId = SupplierId;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
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

        public List<OrderGoodsEntity> getGoodsList() {
            if (GoodsList != null && GoodsList.size() > 0) {
                for (OrderGoodsEntity endEntity : GoodsList) {
                    endEntity.setContentType(1);
                    endEntity.setConsignee(Consignee);
                    endEntity.setMobile(Mobile);
                    endEntity.setOrderSn(OrderSn);

                    endEntity.setOrdState(OrdState);
                    endEntity.setStoreName(SupplierName);
                    endEntity.setPayStatus(PayStatus);
                    endEntity.setOrderStatus(OrderStatus);
                    endEntity.setShippingStatus(ShippingStatus);

                    endEntity.setProvince(Province);
                    endEntity.setCity(City);
                    endEntity.setDistrict(District);
                    endEntity.setAddress(Address);

                    endEntity.setSupplierId(SupplierId);
                    endEntity.setSupplierAvatar(SupplierAvatar);
                    endEntity.setSupplierSex(SupplierSex);
                    endEntity.setSupplierSignture(SupplierSignture);
                    endEntity.setOrderAmount(OrderAmount);
                }
            }

            if (GoodsList != null && GoodsList.size() > 0) {
                for (OrderGoodsEntity endEntity : GoodsList) {
                    endEntity.setGoodsJson(JSON.toJSONString(GoodsList));
                }
            }
            return GoodsList;
        }

        public void setGoodsList(List<OrderGoodsEntity> GoodsList) {
            this.GoodsList = GoodsList;
        }

    }
}
