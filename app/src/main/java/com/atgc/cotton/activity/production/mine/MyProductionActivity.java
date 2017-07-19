package com.atgc.cotton.activity.production.mine;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.entity.MyNumEntity;
import com.atgc.cotton.fragment.MyLikeFragment;
import com.atgc.cotton.fragment.MyProFragment;
import com.atgc.cotton.fragment.ProductFragment;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.MyNumRequest;
import com.atgc.cotton.listenter.ListenerConstans;
import com.atgc.cotton.listenter.ViewPagerListener;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.MycsLog;
import com.atgc.cotton.widget.SharePopup;
import com.atgc.cotton.widget.SimpleViewPagerIndicator;
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

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/5.
 * 我的作品
 */
public class MyProductionActivity extends BaseActivity implements
        View.OnClickListener,
        ViewPagerListener,
        SharePopup.OnItemSelectedListener {
    private static final String TAG = MyProductionActivity.class.getSimpleName();
    private AccountEntity account;
    private ImageLoader imageLoader;
    private ImageView iv_back, iv_bg;
    private TextView tv_title, tv_focus, tv_fans, tv_edit, tv_intro;
    private SharePopup sharePopup;
    private String[] mTitles = new String[]{"作品", "喜欢"};
    private SimpleViewPagerIndicator mIndicator;
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ProductFragment myProFragment, likeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_production);
        ListenerConstans.mQunZuPager = this;
        initViews();
        initDatas();
        initEvents();
    }

    private void initViews() {
        account = App.getInstance().getAccountEntity();
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        iv_back = (ImageView) findViewById(R.id.iv_back);
//        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        tv_title = (TextView) findViewById(R.id.tv_name);
        tv_focus = (TextView) findViewById(R.id.tv_focus);
        tv_fans = (TextView) findViewById(R.id.tv_fans);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_intro = (TextView) findViewById(R.id.tv_intro);

        sharePopup = new SharePopup(context, mController);
        sharePopup.setOnItemSelectedListener(this);

        iv_back.setOnClickListener(this);
//        iv_share.setOnClickListener(this);
        iv_bg.setOnClickListener(this);
        tv_focus.setOnClickListener(this);
        tv_fans.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_intro.setOnClickListener(this);
        request();
    }

    private void initDatas() {
        mIndicator.setTitles(mTitles);
        myProFragment=new MyProFragment();
        likeFragment=new MyLikeFragment();
        mFragments.add(myProFragment);
        mFragments.add(likeFragment);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
    }

    private void initEvents() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setCurrentItem(int page) {
        mViewPager.setCurrentItem(page);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
//            case R.id.iv_share:
//                if (!isFinishing()) {
//                    if (sharePopup.isShowing()) {
//                        sharePopup.dismiss();
//                    } else {
//                        sharePopup.show(getWindow().getDecorView());
//                    }
//                }
//                break;
            case R.id.tv_focus:

                break;
            case R.id.tv_fans:

                break;
            case R.id.tv_edit:
                openActivity(EditDataActivity.class);
                break;
            case R.id.tv_intro:

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
                    circleMedia.setShareImage(new UMImage(context, R.drawable.ic_launcher));
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


    private void request() {
        showLoadingDialog();
        MyNumRequest request = new MyNumRequest(TAG);
        request.send(new BaseDataRequest.RequestCallback<MyNumEntity>() {
            @Override
            public void onSuccess(MyNumEntity pojo) {
                cancelLoadingDialog();
                bindDatas(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void bindDatas(MyNumEntity entity) {
        if (entity != null) {
            String sex = account.getSex();
            tv_title.setText(account.getUserName());
            Drawable nav_up = null;
            if (sex.equals("0")) {
                nav_up = getResources().getDrawable(R.drawable.works_man);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            } else {
                nav_up = getResources().getDrawable(R.drawable.works_lady);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            }
            tv_title.setCompoundDrawables(null, null, nav_up, null);
            tv_focus.setText("关注：" + entity.getFollowCount());
            tv_fans.setText("粉丝：" + entity.getFansCount());
            imageLoader.displayImage(account.getAvatar(), iv_bg, ImageLoaderUtils.getDisplayImageOptions());
        }
    }
}
