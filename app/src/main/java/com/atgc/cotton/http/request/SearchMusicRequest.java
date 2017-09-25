package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.BaseOnline;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017-09-05.
 */
public class SearchMusicRequest extends BaseDataRequest<BaseOnline> {

    public SearchMusicRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return false;
    }

    @Override
    protected Map<String, String> getParams() {
        String keyword = (String) mParams[0];
        Map<String, String> map = new HashMap<String, String>();
        map.put("keyword", keyword);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.ONLINE_MUSIC_SEARCH;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
