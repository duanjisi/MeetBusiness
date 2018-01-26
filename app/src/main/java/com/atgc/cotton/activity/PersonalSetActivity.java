package com.atgc.cotton.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.App;
import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.Session;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.entity.ActionEntity;
import com.atgc.cotton.util.MycsLog;
import com.atgc.cotton.util.Utils;
import com.atgc.cotton.widget.SharePopup;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by liw on 2017/7/8.
 */
public class PersonalSetActivity extends BaseActivity implements SharePopup.OnItemSelectedListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @BindView(R.id.tv_volum)
    TextView tvVolum;
    @BindView(R.id.rl_cache)
    RelativeLayout rlCache;
    //    @BindView(R.id.tv_auth_state)
//    TextView tvAuthState;
//    @BindView(R.id.rl_auth)
//    RelativeLayout rlAuth;
    @BindView(R.id.tv_bind)
    TextView tvBindView;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.rl_pws)
    RelativeLayout rlPws;
    //    @BindView(R.id.rl_wallet)
//    RelativeLayout rlWallet;
    @BindView(R.id.rl_tuijian)
    RelativeLayout rl_tuijian;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R.id.tv_exit_login)
    TextView tvExitLogin;

    private SharePopup sharePopup;
    private Handler handler;
    private AccountEntity account;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        App.getInstance().addTempActivity(this);
//        findViewById(R.id.iv_back).setOnClickListener(this);
//        findViewById(R.id.tv_exit_login).setOnClickListener(this);
//        findViewById(R.id.rl_about).setOnClickListener(this);
        handler = new Handler();
        resources = getResources();
        account = App.getInstance().getAccountEntity();
        sharePopup = new SharePopup(context, mController);
        sharePopup.setOnItemSelectedListener(this);
        initShareData();
        initView();
    }

    private void initShareData() {
        shareContent = getResources().getString(R.string.app_share_content);
        title = getResources().getString(R.string.app_share_title);
        targetUrl = getResources().getString(R.string.app_share_target);
    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_back:
//                finish();
//                break;
//            case R.id.tv_exit_login:    //退出登录
////                OkManager.getInstance().doGet(HttpUrl.LOGOUT, new OkManager.Funcl() {
////                    @Override
////                    public void onResponse(String result) {
////                        L.i(result);
////                        BaseResult bean = JSON.parseObject(result, BaseResult.class);
////                        if (bean.getCode() == 0) {
////                            App.getInstance().logout();
////                            handler.postDelayed(new Runnable() {
////                                @Override
////                                public void run() {
////                                    openActivity(MainActivity.class);
////                                    App.getInstance().exit();
////                                }
////                            }, 100);
////                        }else {
////                            showToast(bean.getMessage());
////                        }
////                    }
////
////                    @Override
////                    public void onFailure() {
////
////                    }
////                });
//                App.getInstance().logout();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        App.getInstance().logout();
//                        openActivity(MainActivity.class);
//                        App.getInstance().exit();
//                    }
//                }, 100);
//                break;
//            case R.id.rl_about:
//                break;
//        }
//    }

    private void initView() {
        String mbilePhone = account.getMobilePhone();
        if (TextUtils.isEmpty(mbilePhone)) {
            tvBindView.setText("未绑定");
            tvBindView.setTextColor(resources.getColor(R.color.text_color_gray_a));
            isBindView = false;
        } else {
            tvBindView.setText("已绑定");
            tvBindView.setTextColor(resources.getColor(R.color.red_fuwa));
            isBindView = true;
        }
    }

    private boolean isBindView = false;

    @OnClick({R.id.iv_back, R.id.rl_cache, R.id.rl_phone, R.id.rl_pws, R.id.rl_tuijian, R.id.rl_about, R.id.tv_exit_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_cache:
                break;
//            case R.id.rl_auth:
//                break;
            case R.id.rl_phone:
                if (isBindView) {
                    openActivity(VerifyPhoneActivity.class);
                } else {
                    openActivity(BindPhoneActivity.class);
                }
                break;
            case R.id.rl_pws:
                openActivity(ChangePwsActivity.class);
                break;
//            case R.id.rl_wallet:
//                openActivity(WalletActivity.class);
//                break;
            case R.id.rl_tuijian:
                if (!isFinishing()) {
                    if (sharePopup.isShowing()) {
                        sharePopup.dismiss();
                    } else {
                        sharePopup.show(getWindow().getDecorView());
                    }
                }
                break;
            case R.id.rl_about:
                openActivity(AboutUsActivity.class);
                break;
            case R.id.tv_exit_login:
                App.getInstance().logout();
                Utils.sendImMessage(Session.ACTION_STOP_CHAT_SERVICE, null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        App.getInstance().logout();
                        openActivity(MainActivity.class);
                        App.getInstance().exit();
                    }
                }, 100);
                break;
        }
    }

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
                }
            }
        });
    }

    @Subscribe
    public void onMessageEvent(ActionEntity event) {
        if (event != null) {
            String action = event.getAction();
            String tag = (String) event.getData();
            if (action.equals(Constants.Action.PHONE_BIND_STATE)) {
                account = App.getInstance().getAccountEntity();
                initView();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
