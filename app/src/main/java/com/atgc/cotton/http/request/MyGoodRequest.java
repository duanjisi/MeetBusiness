package com.atgc.cotton.http.request;

import com.atgc.cotton.entity.BaseGood;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017/7/14.
 */
public class MyGoodRequest extends BaseDataRequest<BaseGood> {

    public MyGoodRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return false;
    }

    @Override
    protected Map<String, String> getParams() {
        String goodsid = (String) mParams[0];
        Map<String, String> map = new HashMap<String, String>();
        map.put("goodsid", goodsid);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.MY_GOODS_URL;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_GET;
    }
}
