package com.atgc.cotton.http;

/**
 * Created by Johnny on 2017/5/16.
 */
public class HttpUrl {
    public static final String BASE_URL = "https://api.hmg66.com/v1/";

    public static final String AUTH_URL = "https://api.66boss.com/ksauth/index.php";

    /**
     * public公共接口
     */
    public static final String LOGIN_URL = BASE_URL + "public/login";
    public static final String REGISTER_URL = BASE_URL + "public/register";

    /**
     * 发布feed相关接口
     */
    public static final String BASE_FEED = BASE_URL + "feed/";
    public static final String PRAISE = BASE_URL + "feed/approval/";

    /**
     * user公共接口
     */
    public static final String USER = BASE_URL + "user/";
    public static final String MODIFY_NICK_URL = BASE_URL + "user/name";

}
