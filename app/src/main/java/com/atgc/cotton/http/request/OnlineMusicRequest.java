package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.BaseOnline;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017-09-05.
 */
public class OnlineMusicRequest extends BaseDataRequest<BaseOnline> {

    public OnlineMusicRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return false;
    }

    @Override
    protected Map<String, String> getParams() {
        String catid = (String) mParams[0];
        String page = (String) mParams[1];
        String size = (String) mParams[2];
        Map<String, String> map = new HashMap<String, String>();
        map.put("catid", catid);
        map.put("page", page);
        map.put("size", size);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.ONLINE_MUSICES;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
