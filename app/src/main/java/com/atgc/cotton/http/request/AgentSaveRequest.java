package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.AgentInfo;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

/**
 * Created by Johnny on 2018-01-20.
 */
public class AgentSaveRequest extends BaseDataRequest<AgentInfo> {

    public AgentSaveRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return true;
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> map = (Map<String, String>) mParams[0];
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.AGENT_SENDSMS;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_POST;
    }
}
