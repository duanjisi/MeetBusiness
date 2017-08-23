package com.atgc.cotton.http.request;

import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017-08-21.
 */
public class BindSMSCodeRequest extends BaseDataRequest<String> {

    public BindSMSCodeRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return false;
    }

    @Override
    protected Map<String, String> getParams() {
        String mobilephone = (String) mParams[0];
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobilephone", mobilephone);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.BIND_SMS_QRCODE;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_POST;
    }
}
