package com.atgc.cotton.entity;

import java.util.List;

/**
 * Created by liw on 2017/7/20.
 */

public class GoodsJsonEntity {

    /**
     * supplierid : 100000000
     * goodslist : [{"goodsid":21,"attrs":"颜色：红色|尺寸：40X65cm","buynumber":32},{"goodsid":21,"attrs":"颜色：白色|尺寸：40X65cm","buynumber":10}]
     */

    private int supplierid;
    private List<GoodslistBean> goodslist;

    public int getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(int supplierid) {
        this.supplierid = supplierid;
    }

    public List<GoodslistBean> getGoodslist() {
        return goodslist;
    }

    public void setGoodslist(List<GoodslistBean> goodslist) {
        this.goodslist = goodslist;
    }

    public static class GoodslistBean {
        /**
         * goodsid : 21
         * attrs : 颜色：红色|尺寸：40X65cm
         * buynumber : 32
         */

        private int goodsid;
        private String attrs;
        private int buynumber;

        public int getGoodsid() {
            return goodsid;
        }

        public void setGoodsid(int goodsid) {
            this.goodsid = goodsid;
        }

        public String getAttrs() {
            return attrs;
        }

        public void setAttrs(String attrs) {
            this.attrs = attrs;
        }

        public int getBuynumber() {
            return buynumber;
        }

        public void setBuynumber(int buynumber) {
            this.buynumber = buynumber;
        }
    }
}
