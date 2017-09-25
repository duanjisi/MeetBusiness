package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.BaseCate;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

/**
 * Created by Johnny on 2017-09-05.
 */
public class OnlineMuCateRequest extends BaseDataRequest<BaseCate> {

    public OnlineMuCateRequest(String tag, Object... params) {
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
        return HttpUrl.ONLINE_MUSIC_CATES;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
