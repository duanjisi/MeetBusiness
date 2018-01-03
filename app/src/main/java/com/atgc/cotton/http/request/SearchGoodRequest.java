package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.BaseSGood;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017-10-20.
 */
public class SearchGoodRequest extends BaseDataRequest<BaseSGood> {

    public SearchGoodRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return false;
    }

    @Override
    protected Map<String, String> getParams() {
        String key = (String) mParams[0];
        String page = (String) mParams[1];
        String size = (String) mParams[2];
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", key);
        map.put("page", page);
        map.put("size", size);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.SEARCH_GOOD;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
