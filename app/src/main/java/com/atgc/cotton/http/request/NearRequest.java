package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.HomeBaseData;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017/7/7.
 * 首页附近视频列表
 */
public class NearRequest extends BaseDataRequest<HomeBaseData> {

    public NearRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return false;
    }

    @Override
    protected Map<String, String> getParams() {
        String lgt = (String) mParams[0];
        String lat = (String) mParams[1];
        Map<String, String> map = new HashMap<String, String>();
        map.put("lgt", lgt);
        map.put("lat", lat);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.NEAR;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
