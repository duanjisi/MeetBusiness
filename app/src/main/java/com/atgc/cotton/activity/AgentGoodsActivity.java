package com.atgc.cotton.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.H5LoginRequest;
import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.cookie.Cookie;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>描述：选择商品界面
 * <p>作者：duanjisi 2018年 01月 24日
 */
public class AgentGoodsActivity extends BaseActivity {
    private static final String TAG = AgentGoodsActivity.class.getSimpleName();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.wb)
    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_goods);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.wb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.wb:

                break;
        }
    }

    private void loadView() {
        myWebView.loadUrl("");
    }

    /**
     * init WebView Settings
     */
    private void initWebViewSettings() {
//        myWebView.getSettings().setSupportZoom(true);
//        myWebView.getSettings().setBuiltInZoomControls(true);
//        myWebView.getSettings().setDefaultFontSize(12);
//        myWebView.getSettings().setLoadWithOverviewMode(true);
        // 设置可以访问文件
        myWebView.getSettings().setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setUserAgentString("agent");
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setDatabaseEnabled(true);
    }

    /**
     * Sync Cookie
     */
//    private void syncCookie(Context context, String url) {
//        try {
//            Log.d("Nat: webView.syncCookie.url", url);
//
//            CookieSyncManager.createInstance(context);
//
//            CookieManager cookieManager = CookieManager.getInstance();
//            cookieManager.setAcceptCookie(true);
//            cookieManager.removeSessionCookie();// 移除
//            cookieManager.removeAllCookie();
//            String oldCookie = cookieManager.getCookie(url);
//            if (oldCookie != null) {
//                Log.d("Nat: webView.syncCookieOutter.oldCookie", oldCookie);
//            }
//
//            PersistentCookieStore persistentCookieStore = MyApplication
//                    .getInstance().getCookieStore();
//            for (Cookie cookie : persistentCookieStore.getCookies()) {
//
//                String cookieString = cookie.getName() + "=" + cookie.getValue()
//                        + "; domain=" + cookie.getDomain()
//                        + "; path=" + cookie.getPath();
//                cookieManager.setCookie(url, cookieString);
//                CookieSyncManager.getInstance().sync();
//            }
//            String newCookie = cookieManager.getCookie(url);
//            if (newCookie != null) {
//                Log.d("Nat: webView.syncCookie.newCookie", newCookie);
//            }
//        } catch (Exception e) {
//            Log.e("Nat: webView.syncCookie failed", e.toString());
//        }
//    }
    private void h5LoginRequest() {
        H5LoginRequest request = new H5LoginRequest(TAG);
        showLoadingDialog();
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String json) {

            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
            }
        });
    }
}
