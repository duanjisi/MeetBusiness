package com.atgc.cotton.http.request;

import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017/7/14.
 */
public class ComOrReplyRequest extends BaseDataRequest<String> {

    public ComOrReplyRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return true;
    }

    @Override
    protected Map<String, String> getParams() {
        String pid = (String) mParams[1];
        String content = (String) mParams[2];
        String uidto = (String) mParams[3];
        Map<String, String> map = new HashMap<String, String>();
        map.put("pid", pid);
        map.put("content", content);
        map.put("uidto", uidto);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.COM_REPLY + mParams[0];
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_POST;
    }
}
