/**
 * Summary: 响应协议内容反序列化对象（不包含data字段，方便提前状态码检测）
 */

package com.boss66.meetbusiness.http;

public class PreRspPojo {
    public Integer Code;
    public String msg;

    public Integer state;
    public Integer Status;
//    public String result;

    public String message;
    public String Data;
}