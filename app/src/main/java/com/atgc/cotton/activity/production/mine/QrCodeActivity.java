package com.atgc.cotton.activity.production.mine;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.listener.CreateImageCallback;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.MakeQRCodeUtil;
import com.atgc.cotton.util.MycsLog;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.CircleImageView;
import com.atgc.cotton.widget.SharePopup;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.bean.HandlerRequestCode;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by Johnny on 2017/7/5.
 * 我的二维码
 */
public class QrCodeActivity extends BaseActivity implements SharePopup.OnItemSelectedListener {
    private ImageView iv_back, iv_share, iv_qr_code;
    private CircleImageView avatar;
    private TextView tv_name, tv_id;
    private ImageLoader imageLoader;
    private int screenW;
    private SharePopup sharePopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        initViews();
    }

    private void initViews() {
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        avatar = (CircleImageView) findViewById(R.id.iv_head);
        iv_qr_code = (ImageView) findViewById(R.id.iv_qcode);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_id = (TextView) findViewById(R.id.tv_id);
        sharePopup = new SharePopup(context, mController);
        sharePopup.setOnItemSelectedListener(this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFinishing()) {
                    if (sharePopup.isShowing()) {
                        sharePopup.dismiss();
                    } else {
                        sharePopup.show(getWindow().getDecorView());
                    }
                }
            }
        });
        screenW = UIUtils.getScreenWidth(QrCodeActivity.this) * 3 / 5;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) iv_qr_code.getLayoutParams();
        linearParams.height = screenW;
        linearParams.width = screenW;
        iv_qr_code.setLayoutParams(linearParams);

        AccountEntity sAccount = App.getInstance().getAccountEntity();
        String user_id = sAccount.getUserId();
        String url = "https://api.66boss.com/web/download?uid=" + user_id;
        MakeQRCodeUtil.createQRImage(url, screenW, screenW, iv_qr_code, new CreateImageCallback() {
            @Override
            public void produce(Bitmap bit) {
                if (bit != null) {
                    bitmap = bit;
                }
            }
        });
        String headicon = sAccount.getAvatar();
        imageLoader.displayImage(headicon, avatar,
                ImageLoaderUtils.getDisplayImageOptions());
        String user_name = sAccount.getUserName();
        tv_name.setText("" + user_name);
        String sex = sAccount.getSex();
        Drawable nav_up = null;
        if (sex.equals("1")) {
            nav_up = getResources().getDrawable(R.drawable.works_man);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        } else {
            nav_up = getResources().getDrawable(R.drawable.works_lady);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        }
        tv_name.setCompoundDrawables(null, null, nav_up, null);
        tv_id.setText(sAccount.getUserId());
        initShareData();
    }

    private void initShareData() {
        shareContent = getResources().getString(R.string.live_share_content);
        title = App.getInstance().getAccountEntity().getUserName();
        targetUrl = "http://www.baidu.com/";
    }

    private Bitmap bitmap;
    private String shareContent;
    private String targetUrl;
    private String title;
    private String imageUrl;

    @Override
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
                    if (bitmap != null) {
                        weixinContent.setShareImage(new UMImage(context, bitmap));
                    } else {
                        weixinContent.setShareImage(new UMImage(context, R.drawable.logo_circle));
                    }
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
                    if (bitmap != null) {
                        circleMedia.setShareImage(new UMImage(context, bitmap));
                    } else {
                        circleMedia.setShareImage(new UMImage(context, R.drawable.logo_circle));
                    }
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
                    if (bitmap != null) {
                        qqShareContent.setShareImage(new UMImage(context, bitmap));
                    } else {
                        qqShareContent.setShareImage(new UMImage(context, R.drawable.logo_circle));
                    }
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
                    if (bitmap != null) {
                        qzone.setShareImage(new UMImage(context, bitmap));
                    } else {
                        qzone.setShareImage(new UMImage(context, R.drawable.logo_circle));
                    }
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
                }
            }
        });
    }
}
