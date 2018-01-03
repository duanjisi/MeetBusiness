package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.UnitEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

/**
 * Created by Johnny on 2017-12-06.
 */
public class UnityRequest extends BaseDataRequest<UnitEntity> {
    public UnityRequest(String tag, Object... params) {
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
        return HttpUrl.UNIT_URL;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
