package com.atgc.cotton.http;

/**
 * Created by Johnny on 2017/5/16.
 */
public class HttpUrl {
    //    public static final String BASE_URL = "https://api.hmg66.com/v1/";
    public static final String BASE_URL = "https://yuetao.66boss.com/v1/";
    public static final String AUTH_URL = "https://api.66boss.com/ksauth/index.php";


    /**
     * attention 关注相关接口
     */
    public static final String NUM_URL = BASE_URL + "attention/statistics";//获取我的粉丝总数，关注总数

    /**
     * public公共接口
     */
    public static final String HOME_PAGER_URL = BASE_URL + "public/feed";//获取首页发现数据列表
    public static final String LOGIN_URL = BASE_URL + "public/login";
    public static final String REGISTER_URL = BASE_URL + "public/register";

    /**
     * 发布feed相关接口
     */
    public static final String BASE_FEED = BASE_URL + "feed/";
    public static final String PRAISE = BASE_URL + "feed/approval/";
    public static final String NEAR = BASE_URL + "feed/vicinity/";
    public static final String MY_PRODUCTION = BASE_URL + "feed/mine";

    /**
     * user公共接口
     */
    public static final String USER = BASE_URL + "user/";
    public static final String MODIFY_NICK_URL = BASE_URL + "user/name";
    public static final String USER_AVATAR_URL = USER + "avatar";
    /**
     * 售货架相关接口
     */
    public static final String VEND_UPLOAD_GOODS = BASE_URL + "goods/";//上架商品
    public static final String VEND_GET_MY_GOODS = BASE_URL + "goods/mine?";//获取我的售货架列表
    public static final String VEND_CHANGE_GOODS = BASE_URL + "goods/";//修改商品 or 删除商品

    //退出登录
    public static final String LOGOUT  = BASE_URL+"public/logout";
}
