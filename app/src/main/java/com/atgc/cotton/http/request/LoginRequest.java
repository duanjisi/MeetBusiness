package com.atgc.cotton.http.request;

import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017/6/23.
 */
public class LoginRequest extends BaseDataRequest<AccountEntity> {

    public LoginRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return true;
    }

    @Override
    protected Map<String, String> getParams() {
        String mobilephone = (String) mParams[0];
        String password = (String) mParams[1];
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobilephone", mobilephone);
        map.put("password", password);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.LOGIN_URL;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_POST;
    }
}
