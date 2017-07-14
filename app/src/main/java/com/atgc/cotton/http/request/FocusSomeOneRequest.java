package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.FocusEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017/7/7.
 * 关注某人
 */
public class FocusSomeOneRequest extends BaseDataRequest<FocusEntity> {

    public FocusSomeOneRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return true;
    }

    @Override
    protected Map<String, String> getParams() {
        String sourcefrom  = (String) mParams[1];
        Map<String, String> map = new HashMap<String, String>();
        map.put("sourcefrom ", sourcefrom );
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.BASE_FOCUS + mParams[0];
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_POST;
    }
}
