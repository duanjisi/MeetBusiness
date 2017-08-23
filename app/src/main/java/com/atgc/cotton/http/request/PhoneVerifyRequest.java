package com.atgc.cotton.http.request;

import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017-08-21.
 */
public class PhoneVerifyRequest extends BaseDataRequest<String> {

    public PhoneVerifyRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return true;
    }

    @Override
    protected Map<String, String> getParams() {
        String verifycode = (String) mParams[0];
        Map<String, String> map = new HashMap<String, String>();
        map.put("verifycode", verifycode);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.PHONE_VERIFY;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_POST;
    }
}
