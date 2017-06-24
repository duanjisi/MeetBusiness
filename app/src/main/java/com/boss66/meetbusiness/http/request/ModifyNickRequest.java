package com.boss66.meetbusiness.http.request;

import com.boss66.meetbusiness.http.BaseDataRequest;
import com.boss66.meetbusiness.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017/6/23.
 */
public class ModifyNickRequest extends BaseDataRequest<String> {

    public ModifyNickRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return true;
    }

    @Override
    protected Map<String, String> getParams() {
        String nick = (String) mParams[0];
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", nick);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.MODIFY_NICK_URL;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_PUT;
    }
}
