package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.AgentCheck;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

/**
 * Created by Johnny on 2018-01-20.
 */
public class AgentCheckRequest extends BaseDataRequest<AgentCheck> {

    public AgentCheckRequest(String tag, Object... params) {
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
        return HttpUrl.AGENT_CHECK_USER;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_POST;
    }
}
