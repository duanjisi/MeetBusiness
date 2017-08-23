package com.atgc.cotton.http.request;

import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017-08-21.
 */
public class ChangePwsRequest extends BaseDataRequest<String> {

    public ChangePwsRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return false;
    }

    @Override
    protected Map<String, String> getParams() {
        String oldpsw = (String) mParams[0];
        String newpsw = (String) mParams[1];
        Map<String, String> map = new HashMap<String, String>();
        map.put("oldpsw", oldpsw);
        map.put("newpsw", newpsw);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.CHANGE_PWS_URL;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_PUT;
    }
}
