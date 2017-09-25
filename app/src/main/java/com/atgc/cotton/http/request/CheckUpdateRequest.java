package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.UpdateInfoEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

/**
 * Created by Johnny on 2016/9/21.
 */
public class CheckUpdateRequest extends BaseDataRequest<UpdateInfoEntity> {

    public CheckUpdateRequest(String tag, Object... params) {
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
        return HttpUrl.VERSION_UPDATE;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
