package com.atgc.cotton.http.request;

import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017/7/11.
 * 修改用户昵称
 */
public class ModifyUserNickRequest extends BaseDataRequest<String> {
    public ModifyUserNickRequest(String tag, Object... params) {
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
