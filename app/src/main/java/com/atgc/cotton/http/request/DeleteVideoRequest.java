package com.atgc.cotton.http.request;

import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

/**
 * Created by Johnny on 2017-08-30.
 */
public class DeleteVideoRequest extends BaseDataRequest<String> {

    public DeleteVideoRequest(String tag, Object... params) {
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
        return HttpUrl.DELETE_VIDEO + mParams[0];
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_DELETE;
    }
}
