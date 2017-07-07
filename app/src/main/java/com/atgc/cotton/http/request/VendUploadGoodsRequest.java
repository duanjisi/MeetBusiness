package com.atgc.cotton.http.request;

import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;

import java.util.Map;

/**
 * Created by GMARUnity on 2017/7/5.
 */
public class VendUploadGoodsRequest extends BaseDataRequest<String> {
    public VendUploadGoodsRequest(String tag, Object... params) {
        super(tag, params);
    }

    @Override
    protected boolean isParse() {
        return false;
    }

    @Override
    protected Map<String, String> getParams() {
        return null;
    }

    @Override
    protected String getApiPath() {
        return HttpUrl.VEND_UPLOAD_GOODS;
    }

    @Override
    protected int getRequestMethod() {
        return REQUEST_METHOD_POST;
    }
}
