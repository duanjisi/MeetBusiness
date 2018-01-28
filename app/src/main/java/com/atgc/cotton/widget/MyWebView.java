package com.atgc.cotton.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by Johnny on 2016/11/18.
 */
public class MyWebView extends WebView {
    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context) {
        super(context);
    }

    public void enablecrossdomain() {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field.setAccessible(true);
            Object webviewcore = field.get(this);
            Method method = webviewcore.getClass().getDeclaredMethod(
                    "nativeRegisterURLSchemeAsLocal", String.class);
            method.setAccessible(true);
            method.invoke(webviewcore, "http");
            method.invoke(webviewcore, "https");
        } catch (Exception e) {
            Log.d("custom webview", "enablecrossdomain error");
            e.printStackTrace();
        }
    }

    public void enablecrossdomain41() {
        try {
            Field webviewclassic_field = WebView.class
                    .getDeclaredField("mProvider");
            webviewclassic_field.setAccessible(true);
            Object webviewclassic = webviewclassic_field.get(this);
            Field webviewcore_field = webviewclassic.getClass()
                    .getDeclaredField("mWebViewCore");
            webviewcore_field.setAccessible(true);
            Object mWebViewCore = webviewcore_field.get(webviewclassic);
            Field nativeclass_field = webviewclassic.getClass()
                    .getDeclaredField("mNativeClass");
            nativeclass_field.setAccessible(true);
            Object mNativeClass = nativeclass_field.get(webviewclassic);

            Method method = mWebViewCore.getClass().getDeclaredMethod(
                    "nativeRegisterURLSchemeAsLocal",
                    new Class[]{int.class, String.class});
            method.setAccessible(true);
            method.invoke(mWebViewCore, mNativeClass, "http");
            method.invoke(mWebViewCore, mNativeClass, "https");
        } catch (Exception e) {
            Log.d("custom webview", "enablecrossdomain error");
            e.printStackTrace();
        }
    }
}
