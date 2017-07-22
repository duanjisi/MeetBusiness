package com.atgc.cotton.entity;

import java.util.List;

/**
 * Created by liw on 2017/7/12.
 */

public class AddressListEntity {

    /**
     * Status : 200
     * Code : 0
     * Name : UserController
     * Message : success
     * Data : [{"AddTime":1499840271,"Address":"啦啦啦啦啦啦","AddressId":4,"City":76,"Consignee":"哈哈哈","Contact":"15323339887","District":693,"IsDefault":1,"Province":6,"UserId":100000107}]
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
         * AddTime : 1499840271
         * Address : 啦啦啦啦啦啦
         * AddressId : 4
         * City : 76
         * Consignee : 哈哈哈
         * Contact : 15323339887
         * District : 693
         * IsDefault : 1
         * Province : 6
         * UserId : 100000107
         */

        private int AddTime;
        private String Address;
        private int AddressId;
        private int City;
        private String Consignee;
        private String Contact;
        private int District;
        private int IsDefault;
        private int Province;
        private int UserId;

        private String location;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

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

        public int getAddressId() {
            return AddressId;
        }

        public void setAddressId(int AddressId) {
            this.AddressId = AddressId;
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

        public String getContact() {
            return Contact;
        }

        public void setContact(String Contact) {
            this.Contact = Contact;
        }

        public int getDistrict() {
            return District;
        }

        public void setDistrict(int District) {
            this.District = District;
        }

        public int getIsDefault() {
            return IsDefault;
        }

        public void setIsDefault(int IsDefault) {
            this.IsDefault = IsDefault;
        }

        public int getProvince() {
            return Province;
        }

        public void setProvince(int Province) {
            this.Province = Province;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }
    }
}
