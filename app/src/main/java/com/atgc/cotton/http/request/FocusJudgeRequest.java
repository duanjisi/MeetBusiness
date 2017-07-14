package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.FocusEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

/**
 * Created by Johnny on 2017/7/7.
 * 判断关注情况
 */
public class FocusJudgeRequest extends BaseDataRequest<FocusEntity> {

    public FocusJudgeRequest(String tag, Object... params) {
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
        return HttpUrl.BASE_FOCUS + mParams[0];
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
