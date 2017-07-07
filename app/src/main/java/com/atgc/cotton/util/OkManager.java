package com.atgc.cotton.util;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.atgc.cotton.App;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created by liw on 2017/6/26.
 */

public class OkManager {

    private final String TAG = OkManager.class.getSimpleName();
    private static OkManager manager;
    private OkHttpClient client;
    private Handler handler;

    private OkManager() {
        client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    //采用单利模式来运行对象
    public static OkManager getInstance() {
        if (manager == null) {
            synchronized (OkManager.class) { //进行了同步
                if (manager == null) {
                    manager = new OkManager();
                }
            }
        }
        return manager;
    }

    /**
     * 异步请求json，返回String
     *
     * @param url
     * @param callBack
     */
    public void doGet(String url, final Funcl callBack) {

        checkNet();


        final Request request = new Request.Builder().url(url).build(); //请求体
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                onApplyFailure(callBack);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onApplySuccess(response.body().toString(), callBack);
                }

            }
        });

    }

    private void checkNet() {
        App app = App.getInstance();
        if (!NetworkUtil.networkAvailable(app)) {
            ToastUtil.show(app.getApplicationContext(), "网络连接出现错误！", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 异步请求json，返回String
     *
     * @param url
     * @param callBack
     */
    public void doPost(String url, Map<String, String> params, final Funcl callBack) {

        checkNet();

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formEncodingBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = formEncodingBuilder.build();
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                onApplyFailure(callBack);

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onApplySuccess(response.body().string(), callBack);
                }
            }
        });
    }

    /**
     * 上传文件             代测试 okhttp2.0不确定这样写可行
     *
     * @param url
     * @param params
     * @param files
     * @param callBack
     */
    public void doUpload(String url, Map<String, String> params, Map<String, File> files, final Funcl callBack) {

        checkNet();


        MultipartBuilder multipartBuilder = new MultipartBuilder();

        //添加参数
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }

        //添加上传文件
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                multipartBuilder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }
        RequestBody requestBody = multipartBuilder.build();
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                onApplyFailure(callBack);

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onApplySuccess(response.body().string(), callBack);
                }
            }
        });

    }

    //获取mime type
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 请求失败
     *
     * @param callBack
     */
    private void onApplyFailure(final Funcl callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onFailure();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    /**
     * 请求成功
     *
     * @param jsonValue
     * @param callBack
     */
    private void onApplySuccess(final String jsonValue, final Funcl callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(jsonValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    interface Funcl {
        void onResponse(String result);

        void onFailure();
    }
}
