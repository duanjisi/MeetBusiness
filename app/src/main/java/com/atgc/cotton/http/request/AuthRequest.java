package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.AuthEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017/6/23.
 */
public class AuthRequest extends BaseDataRequest<AuthEntity> {

    public AuthRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return true;
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> map = new HashMap<String, String>();
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.AUTH_URL;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
