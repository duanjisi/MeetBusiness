package com.atgc.cotton.activity.videoedit;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atgc.cotton.App;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.util.FileUtils;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.R;
import com.atgc.cotton.http.HttpUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by Johnny on 2017/6/27.
 * 分享视频
 */
public class ShareVideoActivity extends BaseActivity {
    private ImageView ivClose, iv_video_bg;
    private TextView tvPublish;
    private EditText editText;
    private RadioGroup mRadioGroup;
    private String videoPath, BgPath;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_video);
        initViews();
    }

    private void initViews() {
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            videoPath = bundle.getString("videoPath", "");
            BgPath = bundle.getString("BgPath", "");
        }
        ivClose = (ImageView) findViewById(R.id.iv_close);
        iv_video_bg = (ImageView) findViewById(R.id.iv_video_bg);
        tvPublish = (TextView) findViewById(R.id.tv_publish);
        editText = (EditText) findViewById(R.id.et_content);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadVideoFile();
            }
        });
        mRadioGroup.setOnCheckedChangeListener(new CheckListener());
        if (!TextUtils.isEmpty(BgPath)) {
            imageLoader.displayImage("file://" + BgPath, iv_video_bg, ImageLoaderUtils.getDisplayImageOptions());
        }
    }

    private class CheckListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup arg0, int arg1) {

            switch (arg1) {
                case R.id.rb_qq:

                    break;
                case R.id.rb_zone:

                    break;
                case R.id.rb_wx:

                    break;
                case R.id.rb_wx_circle:

                    break;
                default:
                    break;
            }
        }
    }


    private void uploadVideoFile() {
        if (!isImageFile(BgPath)) {
            showToast("图片文件格式不对!", true);
            return;
        }
        if (TextUtils.isEmpty(videoPath)) {
            showToast("视频文件路径为空!", true);
        }

        String main = HttpUrl.BASE_FEED;
        final HttpUtils httpUtils = new HttpUtils(60 * 1000);//实例化RequestParams对象
        final com.lidroid.xutils.http.RequestParams params = new com.lidroid.xutils.http.RequestParams();
        try {
            params.addHeader("Authorization", App.getInstance().getToken());
            File file = new File(BgPath);
            if (file.exists() && file.length() > 0) {
                params.addBodyParameter("coverfile ", file);//封面
            }

            File videoFile = new File(videoPath);
            if (videoFile.exists() && videoFile.length() > 0) {
                params.addBodyParameter("videofile ", videoFile);//封面
            }

            params.addBodyParameter("content ", getText(editText));//发布的内容
            params.addBodyParameter("location ", "");//位置坐标 eg:113.1111515-23.35352
            params.addBodyParameter("locationname ", "");//位置坐标对应的名称
            params.addBodyParameter("goodsid ", "");//关联的商品ID
            params.addBodyParameter("purview ", "");//是否公开，1:公开 2:私密 默认1
            params.addBodyParameter("reminduser ", "");//提醒谁看，传UserId,多个用 ‘|’ 隔开 eg:100000000|100000001
            showLoadingDialog();
            httpUtils.send(HttpRequest.HttpMethod.POST, main, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.i("info", "responseInfo:" + responseInfo.result);
                    Toast.makeText(context, "上传成功!", Toast.LENGTH_LONG).show();
                    cancelLoadingDialog();
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(context, "上传失败!", Toast.LENGTH_LONG).show();
                    cancelLoadingDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isImageFile(String url) {
        Log.i("info", "===============url:" + url);
        String end = FileUtils.getFileNameFromPath(url);
        if (end.contains(".jpg") ||
                end.contains(".png") ||
                end.contains(".jpeg")) {
            return true;
        } else {
            return false;
        }
    }
}
