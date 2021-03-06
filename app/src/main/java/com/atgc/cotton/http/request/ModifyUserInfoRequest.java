package com.atgc.cotton.http.request;

import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

/**
 * Created by Johnny on 2017/7/11.
 * 修改用户信息
 */
public class ModifyUserInfoRequest extends BaseDataRequest<String> {
    public ModifyUserInfoRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return true;
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> map = (Map<String, String>) mParams[0];
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.USER;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_PUT;
    }
}
