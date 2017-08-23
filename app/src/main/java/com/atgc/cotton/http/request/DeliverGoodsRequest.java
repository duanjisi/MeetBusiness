package com.atgc.cotton.http.request;

import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnny on 2017/8/11.
 */
public class DeliverGoodsRequest extends BaseDataRequest<String> {

    public DeliverGoodsRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return true;
    }

    @Override
    protected Map<String, String> getParams() {
        String orderid  = (String) mParams[0];
        String expressname = (String) mParams[1];
        String expressnum = (String) mParams[2];
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderid", orderid);
        map.put("expressname", expressname);
        map.put("expressnum", expressnum);
        return map;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.DELIVER_GOODS;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_POST;
    }
}
