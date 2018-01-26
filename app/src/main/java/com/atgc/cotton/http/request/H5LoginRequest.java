package com.atgc.cotton.http.request;

import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

/**
 * <p>描述：
 * <p>作者：duanjisi 2018年 01月 25日
 */

public class H5LoginRequest extends BaseDataRequest<String> {

    public H5LoginRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return false;
    }

    @Override
    protected Map<String, String> getParams() {
        return null;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.AGENT_LOGIN;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
