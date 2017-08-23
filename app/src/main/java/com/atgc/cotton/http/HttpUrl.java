package com.atgc.cotton.http;

/**
 * Created by Johnny on 2017/5/16.
 */
public class HttpUrl {
    //    public static final String BASE_URL = "https://api.hmg66.com/v1/";
    public static final String BASE_URL = "https://yuetao.66boss.com/v1/";
    public static final String AUTH_URL = "https://api.66boss.com/ksauth/index.php";
    /**
     * android版本更新
     */
    public static final String VERSION_UPDATE = "https://app.66boss.com/update.php";

    /**
     * attention 关注相关接口
     */
    public static final String NUM_URL = BASE_URL + "attention/statistics";//获取我的粉丝总数，关注总数
    public static final String BASE_FOCUS = BASE_URL + "attention/";//获取我的粉丝总数，关注总数

    /**
     * public公共接口
     */
    public static final String HOME_PAGER_URL = BASE_URL + "public/feed";//获取首页发现数据列表
    public static final String LOGIN_URL = BASE_URL + "public/login";
    public static final String REGISTER_URL = BASE_URL + "public/register";
    public static final String MY_GOODS_URL = BASE_URL + "public/goodsmulti";
    public static final String COMMENTS_URL = BASE_URL + "public/commentary/";
    public static final String FOCUS_FANS_URL = BASE_URL + "public/followstat/";
    /**
     * 发布feed相关接口
     */
    public static final String BASE_FEED = BASE_URL + "feed/";
    public static final String PRAISE = BASE_URL + "feed/approval/";
    public static final String NEAR = BASE_URL + "feed/vicinity/";
    public static final String MY_PRODUCTION = BASE_URL + "feed/mine";
    public static final String MY_LIKE = BASE_URL + "feed/like";
    public static final String COM_REPLY = BASE_URL + "feed/commentary/";
    public static final String LIKE_STATUS = BASE_URL + "feed/isapproval/";
    /**
     * user公共接口
     */
    public static final String USER = BASE_URL + "user/";
    public static final String MODIFY_NICK_URL = BASE_URL + "user/name";
    public static final String USER_AVATAR_URL = USER + "avatar";
    public static final String CHANGE_PWS_URL = USER + "changepsw";
    public static final String BIND_PHONE = USER + "bind";
    public static final String BIND_SMS_QRCODE = USER + "bindsms";
    public static final String PHONE_UNBIND = USER + "unbindsms";
    public static final String PHONE_VERIFY = USER + "validunbindsms";
    /**
     * 售货架相关接口
     */
    public static final String VEND_UPLOAD_GOODS = BASE_URL + "goods/";//上架商品
    public static final String VEND_GET_MY_GOODS = BASE_URL + "goods/mine?";//获取我的售货架列表
    public static final String VEND_CHANGE_GOODS = BASE_URL + "goods/";//修改商品 or 删除商品
    public static final String VEND_GOODS = BASE_URL + "goods/mine";//修改商品 or 删除商品

    //退出登录
    public static final String LOGOUT = BASE_URL + "public/logout";

    //添加收货地址
    public static final String ADD_ADDRESS = BASE_URL + "user/address";

    //order 订单相关接口
    public static final String DELIVER_GOODS = BASE_URL + "order/shipping";
    //order 商品评价接口
    public static final String GOODS_COMMENT = BASE_URL + "order/comment";
}
