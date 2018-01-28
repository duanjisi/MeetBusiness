package com.atgc.cotton.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.http.HttpUrl;
import com.atgc.cotton.widget.MyWebView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>描述：选择商品界面
 * <p>作者：duanjisi 2018年 01月 24日
 */
public class AgentGoodsActivity extends BaseActivity {
    private static final String TAG = AgentGoodsActivity.class.getSimpleName();
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.wb)
    MyWebView webView;
    @Bind(R.id.web_view_progress)
    ProgressBar mWebViewProgress;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CookieSyncManager.createInstance(context);
            CookieManager cookieMgr = CookieManager.getInstance();
            cookieMgr.setAcceptCookie(true);
            cookieMgr.removeSessionCookie();// 移除
            cookieMgr.removeAllCookie();
//            cookieStr = msg.obj.toString();
            cookieMgr.setCookie(".66boss.com", msg.obj.toString());// this place should add the login host address(not the login index address)
//            cookieMgr.setCookie("com.atgc.cotton", msg.obj.toString());
            CookieSyncManager.getInstance().sync();
            loadWebPager();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_goods);
        ButterKnife.bind(this);
        h5LoginReuqest();
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (webView.canGoBack()) {
                    webView.goBack(); // goBack()表示返回WebView的上一页面
                } else {
                    finish();
                }
                break;
        }
    }


//    private void h5LoginRequest() {
//        H5LoginRequest request = new H5LoginRequest(TAG);
//        showLoadingDialog();
//        request.send(new BaseDataRequest.RequestCallback<String>() {
//            @Override
//            public void onSuccess(String json) {
//
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                cancelLoadingDialog();
//            }
//        });
//    }

    private void h5LoginReuqest() {
        showLoadingDialog();
        new HttpCookie(handler).start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void loadWebPager() {
        cancelLoadingDialog();
        webView.enablecrossdomain41();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setUserAgentString("Android/66boss");
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAllowFileAccess(true);
//        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setAllowFileAccess(true);
//        settings.setAllowFileAccessFromFileURLs(true);

        settings.setAllowUniversalAccessFromFileURLs(true);
//        webView.addJavascriptInterface(new javaScriptObj(), "local_obj");
        webView.setWebViewClient(new webViewClient());
        webView.setWebChromeClient(new strategyWebChromeClient());
        webView.loadUrl(HttpUrl.AGENT_APPLY_GOOD);
    }


    public class HttpCookie extends Thread {
        private Handler mHandler;

        public HttpCookie(Handler mHandler) {
            this.mHandler = mHandler;
        }

        @Override
        public void run() {
            HttpClient client = new DefaultHttpClient();
            AccountEntity account = App.getInstance().getAccountEntity();
            String accsToken = account.getToken();
            String strUrl = null;
            try {
                strUrl = HttpUrl.AGENT_LOGIN + "?token=" + URLEncoder.encode(accsToken, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpGet post = new HttpGet(strUrl);//this place should add the login address
//            List<NameValuePair> list = new ArrayList<NameValuePair>();
//            String login_user_id = account.getUser_id();

//            list.add(new BasicNameValuePair("login_user_id", login_user_id));
//            list.add(new BasicNameValuePair("token", accsToken));
            try {
//                BasicHttpParams params = new BasicHttpParams();
//                params.setParameter("token", accsToken);
////                post.setEntity(new UrlEncodedFormEntity(list));
//                post.setParams(params);
                HttpResponse reponse = client.execute(post);
                int code = reponse.getStatusLine().getStatusCode();
                if (code == HttpStatus.SC_OK) {
                    AbstractHttpClient absClient = (AbstractHttpClient) client;
                    List<Cookie> cookies = absClient.getCookieStore().getCookies();
                    String value = getCookieText(cookies);
                    Log.i("info", "===cookie:" + value);
                    if (value != null && !value.equals("")) {
                        Message msg = new Message();
                        msg.obj = value;
                        mHandler.sendMessage(msg);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取标准 Cookie
     */
    private String getCookieText(List<Cookie> cookies) {
        if (cookies != null && cookies.size() != 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < cookies.size(); i++) {
                Cookie cookie = cookies.get(i);
                String cookieName = cookie.getName();
                String cookieValue = cookie.getValue();
                if (!TextUtils.isEmpty(cookieName)
                        && !TextUtils.isEmpty(cookieValue)) {
                    sb.append(cookieName + "=");
                    sb.append(cookieValue + ";");
                }
            }
            com.umeng.socialize.utils.Log.e("cookie", sb.toString());
            return sb.toString();
        } else {
            return null;
        }
    }

    private class strategyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (mWebViewProgress == null) return;
            if (newProgress >= 0 && newProgress < 100) {
                if (mWebViewProgress.getVisibility() != View.VISIBLE) {
                    mWebViewProgress.setVisibility(View.VISIBLE);
                }
                mWebViewProgress.setProgress(newProgress);
            } else {
                mWebViewProgress.setVisibility(View.GONE);
            }
        }
    }

    private class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("info", "loadUrl:" + url);
//            if (url.contains("seckill.66boss.com") && cookieStr != null && !cookieStr.equals("")) {
//                CookieSyncManager.createInstance(context);
//                CookieManager cookieMgr = CookieManager.getInstance();
//                cookieMgr.setAcceptCookie(true);
//                cookieMgr.setCookie(".66boss.com", cookieStr);// this place should add the login host address(not the login index address)
//                CookieSyncManager.getInstance().sync();
//            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i("info", "=====onPageFinished()");
            view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
                    "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            super.onPageFinished(view, url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
