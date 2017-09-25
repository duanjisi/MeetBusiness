package com.atgc.cotton.activity.videoedit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.atgc.cotton.App;
import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.adapter.VendGoodAdapter;
import com.atgc.cotton.entity.ActionEntity;
import com.atgc.cotton.entity.ChangeAvatarEntity;
import com.atgc.cotton.entity.VendGoodsEntity;
import com.atgc.cotton.http.HttpUrl;
import com.atgc.cotton.util.FileUtils;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.MycsLog;
import com.atgc.cotton.util.ToastUtil;
import com.atgc.cotton.widget.MyListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.bean.HandlerRequestCode;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.io.File;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Johnny on 2017/6/27.
 * 分享视频
 */
public class ShareVideoActivity extends BaseActivity {
    private String geohash, address;
    private ImageView ivClose, iv_video_bg;
    private TextView tvPublish, tv_link_goods, tv_add_location;
    private MyListView listView;
    private VendGoodAdapter adapter;
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
        if (!TextUtils.isEmpty(BgPath)) {
            Bitmap bitmap = BitmapFactory.decodeFile(BgPath);
            Log.i("info", "width:" + bitmap.getWidth() + "\n" + "height:" + bitmap.getHeight());
        }
        listView = (MyListView) findViewById(R.id.listview);
        adapter = new VendGoodAdapter(context, new itemRemoveCallBack());
        listView.setAdapter(adapter);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        iv_video_bg = (ImageView) findViewById(R.id.iv_video_bg);
        tvPublish = (TextView) findViewById(R.id.tv_publish);
        tv_link_goods = (TextView) findViewById(R.id.tv_link_goods);
        tv_add_location = (TextView) findViewById(R.id.tv_add_location);
        editText = (EditText) findViewById(R.id.et_content);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        tv_link_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VendGoodsActivity.class);
                String goodids = getGoodids();
                if (!TextUtils.isEmpty(goodids)) {
                    intent.putExtra("goodids", goodids);
                }
                startActivityForResult(intent, 100);
            }
        });
        tv_add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActvityForResult(SearchAddressActivity.class, 102);
            }
        });
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
        initSharePlatform(mController);
    }

    private class itemRemoveCallBack implements VendGoodAdapter.itemRemoveCallBack {
        @Override
        public void onRemoveItem(VendGoodsEntity.Goods good) {
            if (good != null) {
                ArrayList<VendGoodsEntity.Goods> goodses = (ArrayList<VendGoodsEntity.Goods>) adapter.getData();
                if (goodses != null && goodses.size() != 0) {
                    for (VendGoodsEntity.Goods entity : goodses) {
                        if (good.getGoodsId().equals(entity.getGoodsId())) {
                            goodses.remove(entity);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            ArrayList<VendGoodsEntity.Goods> list = (ArrayList<VendGoodsEntity.Goods>) data.getSerializableExtra("list");
            if (list != null && list.size() != 0) {
//                Drawable nav_up = getResources().getDrawable(R.drawable.selected);
//                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//                tv_link_goods.setCompoundDrawables(nav_up, null, null, null);
                adapter.initData(list);
            }
        } else if (requestCode == 102 && resultCode == RESULT_OK && data != null) {
            address = data.getStringExtra("address");
            geohash = data.getStringExtra("geohash");
            tv_add_location.setText("" + address);
        }
    }

    private class CheckListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup arg0, int arg1) {
            switch (arg1) {
                case R.id.rb_qq:
                    isThird = true;
                    shareMedia = SHARE_MEDIA.QQ;
                    break;
                case R.id.rb_zone:
                    isThird = true;
                    shareMedia = SHARE_MEDIA.QZONE;
                    break;
                case R.id.rb_wx:
                    isThird = true;
                    shareMedia = SHARE_MEDIA.WEIXIN;
                    break;
                case R.id.rb_wx_circle:
                    isThird = true;
                    shareMedia = SHARE_MEDIA.WEIXIN_CIRCLE;
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
            Log.i("info", "=================BgPath:" + BgPath + "\n" + "length:" + file.length());
            if (file.exists() && file.length() > 0) {
                params.addBodyParameter("coverfile", file);//封面
                Log.i("info", "=================coverfile");
            }
            File videoFile = new File(videoPath);
            if (videoFile.exists() && videoFile.length() > 0) {
                params.addBodyParameter("videofile", videoFile);//视频
                Log.i("info", "=================videoFile");
            }
            params.addBodyParameter("content", getText(editText));//发布的内容
            if (!TextUtils.isEmpty(geohash)) {
                params.addBodyParameter("location", geohash);//位置坐标 eg:113.1111515-23.35352
            }

            if (!TextUtils.isEmpty(address)) {
                params.addBodyParameter("locationname", address);//位置坐标对应的名称
            }
            String goodids = getGoodids();
            if (!TextUtils.isEmpty(goodids)) {
                Log.i("info", "==============goodsid:" + goodids);
                params.addBodyParameter("goodsid", goodids);//关联的商品ID
            }
            params.addBodyParameter("purview", "1");//是否公开，1:公开 2:私密 默认1
            params.addBodyParameter("reminduser", "");//提醒谁看，传UserId,多个用 ‘|’ 隔开 eg:100000000|100000001
            showLoadingDialog();
            httpUtils.send(HttpRequest.HttpMethod.POST, main, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    try {
                        cancelLoadingDialog();
                        Log.i("info", "==============responseInfo:" + responseInfo.result);
                        ChangeAvatarEntity entity = JSON.parseObject(responseInfo.result, ChangeAvatarEntity.class);
                        if (entity != null) {
                            if (entity.Status == 401) {
                                Intent intent = new Intent();
                                intent.setAction(Constants.ACTION_LOGOUT_RESETING);
                                App.getInstance().sendBroadcast(intent);
                            } else if (entity.Status == 200 && entity.Code == 0) {
                                if (isThird) {
                                    ChangeAvatarEntity.Data result = entity.getData();
                                    if (result != null) {
                                        initThirdData(result.getMediaPath());
                                        if (shareMedia != null) {
                                            onItemSelected(shareMedia);
                                        }
                                    }
                                } else {
                                    ToastUtil.showShort(context, "上传成功");
                                    EventBus.getDefault().post(new ActionEntity(Constants.Action.EXIT_CURRENT_ACTIVITY));
                                    finish();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        ToastUtil.showShort(context, "上传失败");
                    }
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

    private String getGoodids() {
        ArrayList<VendGoodsEntity.Goods> goodses = (ArrayList<VendGoodsEntity.Goods>) adapter.getData();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < goodses.size(); i++) {
            VendGoodsEntity.Goods good = goodses.get(i);
            sb.append(",").append(good.getGoodsId());
        }
        return sb.toString();
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

    private void initThirdData(String videoPath) {
        shareContent = getResources().getString(R.string.live_share_content);
        title = App.getInstance().getAccountEntity().getUserName();
        if (videoPath.contains(".mp4")) {
            imageUrl = videoPath.replace(".mp4", ".jpg");
        } else if (videoPath.contains(".mov")) {
            imageUrl = videoPath.replace(".mov", ".jpg");
        }
        targetUrl = videoPath;
    }

    private void initSharePlatform(UMSocialService umSocialService) {
        String weixinAppId = getString(R.string.weixin_app_id);
        String weixinAppSecret = getString(R.string.weixin_app_secret);
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, weixinAppId, weixinAppSecret);
        wxHandler.addToSocialSDK();

        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, weixinAppId, weixinAppSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        String qqAppId = getString(R.string.qq_app_id);
        String qqAppSecret = getString(R.string.qq_app_key);
        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((android.app.Activity) context, qqAppId, qqAppSecret);
        qqSsoHandler.addToSocialSDK();

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((android.app.Activity) context, qqAppId, qqAppSecret);
        qZoneSsoHandler.addToSocialSDK();
    }

    private boolean isThird = false;
    private SHARE_MEDIA shareMedia = null;
    private String shareContent;
    private String targetUrl;
    private String title;
    private String imageUrl;

    public void onItemSelected(SHARE_MEDIA shareMedia) {
        UMediaObject uMediaObject = null;
        MycsLog.i("info", "====title:" + title);
        MycsLog.i("info", "====targetUrl:" + targetUrl);
        switch (shareMedia) {
            case WEIXIN:
                if (!mController.getConfig().getSsoHandler(HandlerRequestCode.WX_REQUEST_CODE).isClientInstalled()) {
                    showToast(R.string.notice_weixin_not_install, false);
                    return;
                }
                //设置微信好友分享内容
                WeiXinShareContent weixinContent = new WeiXinShareContent();
                //设置分享文字
                weixinContent.setShareContent(shareContent);
                //设置title
//                weixinContent.setTitle(TextUtils.isEmpty(title) ? mWebView.getTitle() : title);
                weixinContent.setTitle(title);
                //设置分享内容跳转URL
                weixinContent.setTargetUrl(targetUrl);
                if (imageUrl != null && !imageUrl.equals("")) {
                    //设置分享图片
                    weixinContent.setShareImage(new UMImage(context, imageUrl));
                } else {
                    weixinContent.setShareImage(new UMImage(context, R.drawable.logo_circle));
                }
                uMediaObject = weixinContent;
                break;
            case WEIXIN_CIRCLE:
                if (!mController.getConfig().getSsoHandler(HandlerRequestCode.WX_REQUEST_CODE).isClientInstalled()) {
                    showToast(R.string.notice_weixin_not_install, false);
                    return;
                }
                //设置微信朋友圈分享内容
                CircleShareContent circleMedia = new CircleShareContent();
                circleMedia.setShareContent(shareContent);
                //设置朋友圈title
                circleMedia.setTitle(title);
                circleMedia.setTargetUrl(targetUrl);
                if (imageUrl != null) {
                    //设置分享图片
                    circleMedia.setShareImage(new UMImage(context, imageUrl));
                } else {
                    circleMedia.setShareImage(new UMImage(context, R.drawable.logo_circle));
                }
                uMediaObject = circleMedia;
                break;
            case QQ:
                if (!mController.getConfig().getSsoHandler(HandlerRequestCode.QQ_REQUEST_CODE).isClientInstalled()) {
                    showToast(R.string.notice_qq_not_install, false);
                    return;
                }
                QQShareContent qqShareContent = new QQShareContent();
                qqShareContent.setShareContent(shareContent);
                qqShareContent.setTitle(title);
                if (imageUrl != null && !imageUrl.equals("")) {
                    //设置分享图片
                    qqShareContent.setShareImage(new UMImage(context, imageUrl));
                } else {
                    qqShareContent.setShareImage(new UMImage(context, R.drawable.logo_circle));
                }
                qqShareContent.setTargetUrl(targetUrl);
                uMediaObject = qqShareContent;
                break;
            case QZONE:
                QZoneShareContent qzone = new QZoneShareContent();
//                // 设置分享文字
//                qzone.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QZone");
//                // 设置点击消息的跳转URL
//                qzone.setTargetUrl("http://www.baidu.com");
//                // 设置分享内容的标题
//                qzone.setTitle("QZone title");
                // 设置分享图片
                qzone.setShareContent(shareContent);
                qzone.setTitle(title);
                qzone.setTargetUrl(targetUrl);
                if (imageUrl != null && !imageUrl.equals("")) {
                    //设置分享图片
                    qzone.setShareImage(new UMImage(context, imageUrl));
                } else {
                    qzone.setShareImage(new UMImage(context, R.drawable.logo_circle));
                }
                uMediaObject = qzone;
                break;
        }
        mController.setShareMedia(uMediaObject);
        mController.postShare(context, shareMedia, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                if (eCode == StatusCode.ST_CODE_SUCCESSED) {
                    showToast("分享成功!", true);
                    EventBus.getDefault().post(new ActionEntity(Constants.Action.EXIT_CURRENT_ACTIVITY));
                    finish();
                }
            }
        });
    }
}
