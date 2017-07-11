package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.MyNumEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

/**
 * Created by Johnny on 2017/7/10.
 */
public class MyNumRequest extends BaseDataRequest<MyNumEntity> {

    public MyNumRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return true;
    }

    @Override
    protected Map<String, String> getParams() {
        return null;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.NUM_URL;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
