package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.VideoEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

/**
 * Created by Johnny on 2017-08-30.
 */
public class VideoDetailsRequest extends BaseDataRequest<VideoEntity> {

    public VideoDetailsRequest(String tag, Object... params) {
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
        return HttpUrl.VIDEO_DETAILS_URL + mParams[0];
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
