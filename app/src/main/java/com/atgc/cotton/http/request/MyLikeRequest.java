package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.HomeBaseData;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017/7/7.
 * 我喜欢作品
 */
public class MyLikeRequest extends BaseDataRequest<HomeBaseData> {

    public MyLikeRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return false;
    }

    @Override
    protected Map<String, String> getParams() {
        String page = (String) mParams[0];
        String size = (String) mParams[1];
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", page);
        map.put("size", size);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.MY_LIKE;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
