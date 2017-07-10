package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.retrofit.ApiStores;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liw on 2017/7/7.
 */

public class RegisterRequst extends BaseDataRequest<String> {
    public RegisterRequst(String tag, Object... params) {
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
        return ApiStores.API_SERVER_URL+"public/register";
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_POST;
    }
}
