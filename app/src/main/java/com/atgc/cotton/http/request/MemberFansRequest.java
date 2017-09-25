package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.BaseMember;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017-08-26.
 */
public class MemberFansRequest extends BaseDataRequest<BaseMember> {

    public MemberFansRequest(String tag, Object... params) {
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
        return HttpUrl.ATTENTION_LIST_FANS;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
