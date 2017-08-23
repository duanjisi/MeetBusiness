package com.atgc.cotton.http.request;

import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017/8/16.
 */
public class GoodCommentRequest extends BaseDataRequest<String> {

    public GoodCommentRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return true;
    }

    @Override
    protected Map<String, String> getParams() {
        String orderid = (String) mParams[0];
        String goodsid = (String) mParams[1];
        String content = (String) mParams[2];
        String score = (String) mParams[2];
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderid", orderid);
        map.put("goodsid", goodsid);
        map.put("content", content);
        map.put("score", score );
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.GOODS_COMMENT;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_POST;
    }
}
