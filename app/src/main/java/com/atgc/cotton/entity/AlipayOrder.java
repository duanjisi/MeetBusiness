package com.atgc.cotton.entity;

/**
 * Created by Johnny on 2017/3/25.
 */
public class AlipayOrder {

    /**
     * Status : 200
     * Code : 0
     * Name : PayController
     * Message : success
     * Data : {"OrderStr":"app_id=2016092201948966&biz_content={\"out_trade_no\":\"657657875350\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"seller_id\":\"gangmian_66boss@163.com\",\"subject\":\"老板六六订单657657875350\",\"timeout_express\":\"24h\",\"total_amount\":\"450.00\"}&charset=utf-8&format=JSON&method=alipay.trade.app.pay&notify_url=https://yuetao.66boss.com/notice/ali&sign=pGYAWBgznxm29zTdlK3oqk3K06ZTy%2BGICu7nyyxLX4EMbojWL3FzNYu690wB5Yt2t2wVnHuwudD0vvJUYajDkCSk19xxRlUxM%2FmThLuQT1VjXv4c4jH2GKgnOZi50Gck4PuMN1fTfMGGxczWeiK%2FRO8ZIbMAwI%2FYgRFBoZ7CEOFFVrlFHOg1HpBDS5eU20pleVfNUWY8PpZvLoZiN3i72xXQTPVDUvu5dEHAXiqRXCi32f5Ry4bJAMCHh6bagH2CUp8auW3ZeyCm92OWrqGd5Vvxzb3zRh%2Fq4jIxDACkJYrnwQyf%2FDqkYXtZQpNodFp%2Bg0raVa1t8NmVtGthhE%2Fs0g%3D%3D&sign_type=RSA2&timestamp=2017-07-24 18:11:19&version=1.0"}
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
         * OrderStr : app_id=2016092201948966&biz_content={"out_trade_no":"657657875350","product_code":"QUICK_MSECURITY_PAY","seller_id":"gangmian_66boss@163.com","subject":"老板六六订单657657875350","timeout_express":"24h","total_amount":"450.00"}&charset=utf-8&format=JSON&method=alipay.trade.app.pay&notify_url=https://yuetao.66boss.com/notice/ali&sign=pGYAWBgznxm29zTdlK3oqk3K06ZTy%2BGICu7nyyxLX4EMbojWL3FzNYu690wB5Yt2t2wVnHuwudD0vvJUYajDkCSk19xxRlUxM%2FmThLuQT1VjXv4c4jH2GKgnOZi50Gck4PuMN1fTfMGGxczWeiK%2FRO8ZIbMAwI%2FYgRFBoZ7CEOFFVrlFHOg1HpBDS5eU20pleVfNUWY8PpZvLoZiN3i72xXQTPVDUvu5dEHAXiqRXCi32f5Ry4bJAMCHh6bagH2CUp8auW3ZeyCm92OWrqGd5Vvxzb3zRh%2Fq4jIxDACkJYrnwQyf%2FDqkYXtZQpNodFp%2Bg0raVa1t8NmVtGthhE%2Fs0g%3D%3D&sign_type=RSA2&timestamp=2017-07-24 18:11:19&version=1.0
         */

        private String OrderStr;

        public String getOrderStr() {
            return OrderStr;
        }

        public void setOrderStr(String OrderStr) {
            this.OrderStr = OrderStr;
        }
    }
}
