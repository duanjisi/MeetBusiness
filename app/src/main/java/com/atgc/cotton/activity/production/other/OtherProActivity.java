package com.atgc.cotton.activity.production.other;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.adapter.ProductAdapter;
import com.atgc.cotton.entity.FansEntity;
import com.atgc.cotton.entity.FocusEntity;
import com.atgc.cotton.entity.HomeBaseData;
import com.atgc.cotton.entity.MemberEntity;
import com.atgc.cotton.entity.OrderGoodsEntity;
import com.atgc.cotton.entity.UserInfo;
import com.atgc.cotton.entity.VideoEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.FansRequest;
import com.atgc.cotton.http.request.FocusCancelRequest;
import com.atgc.cotton.http.request.FocusJudgeRequest;
import com.atgc.cotton.http.request.FocusSomeOneRequest;
import com.atgc.cotton.http.request.OtherProRequest;
import com.atgc.cotton.http.request.UserInfoRequest;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.MycsLog;
import com.atgc.cotton.widget.MorePopup;
import com.atgc.cotton.widget.MyGridView;
import com.atgc.cotton.widget.SharePopup;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.paging.gridview.PagingGridView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/11.
 * 他人作品
 */
public class OtherProActivity extends BaseActivity implements View.OnClickListener, SharePopup.OnItemSelectedListener, MorePopup.OnItemdListener {
    private static final String TAG = OtherProActivity.class.getSimpleName();
    private ImageView iv_back, iv_more, iv_bg;
    private TextView tv_name, tv_focus, tv_fans, tv_fos, tv_intro;
    //    private VideoEntity videoEntity;
    private String userId;
    private ImageLoader imageLoader;
    private SharePopup sharePopup;
    private MorePopup morePopup;
    private static final int PAGER_NUM = 10;
    private int pager = 1;
    private MyGridView gridView;
    private ProductAdapter adapter;
    private boolean loadMore = true;
    private boolean isFocus = false;
    private String signature, sex, avatar, userName;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_production);
        init();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String userid = bundle.getString("userid", "");
            if (!TextUtils.isEmpty(userid)) {
                requestUserInfo(userid);
            } else {
                VideoEntity videoEntity = (VideoEntity) bundle.getSerializable("obj");
                MemberEntity memberEntity = (MemberEntity) bundle.getSerializable("member");
                OrderGoodsEntity orderEntity = (OrderGoodsEntity) bundle.getSerializable("orderEntity");
                if (videoEntity != null) {
                    userId = videoEntity.getUserId();
                    signature = videoEntity.getSignature();
                    sex = videoEntity.getSex();
                    avatar = videoEntity.getAvatar();
                    userName = videoEntity.getUserName();
                }

                if (memberEntity != null) {
                    userId = memberEntity.getUserId();
                    signature = memberEntity.getSignature();
                    sex = memberEntity.getSex();
                    avatar = memberEntity.getAvatar();
                    userName = memberEntity.getUserName();
                }

                if (orderEntity != null) {
                    userId = "" + orderEntity.getSupplierId();
                    signature = orderEntity.getSupplierSignture();
                    sex = orderEntity.getSupplierSex();
                    avatar = orderEntity.getSupplierAvatar();
                    userName = orderEntity.getStoreName();
                }
                initViews();
            }
        }
    }

    private void initViews() {
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        iv_back = (ImageView) findViewById(R.id.iv_back);
//        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_more = (ImageView) findViewById(R.id.iv_more);
        iv_bg = (ImageView) findViewById(R.id.iv_bg);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_focus = (TextView) findViewById(R.id.tv_focus);
        tv_fans = (TextView) findViewById(R.id.tv_fans);
//        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_fos = (TextView) findViewById(R.id.tv_focu);
        tv_intro = (TextView) findViewById(R.id.tv_intro);

        iv_back.setOnClickListener(this);
//        iv_share.setOnClickListener(this);
        iv_more.setOnClickListener(this);
//        tv_msg.setOnClickListener(this);
        tv_fos.setOnClickListener(this);
        sharePopup = new SharePopup(context, mController);
        sharePopup.setOnItemSelectedListener(this);
        morePopup = new MorePopup(context);
        morePopup.setOnItemSelectedListener(this);
        gridView = (MyGridView) findViewById(R.id.grid);
        adapter = new ProductAdapter(context);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new itemClickListener());
        gridView.setHasMoreItems(true);
        gridView.setPagingableListener(new PagingGridView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (loadMore) {
                    requestMoreDatas();
                } else {
                    gridView.onFinishLoading(false, null);
                }
            }
        });
        requestDatas();
        requestFans();
        isFocusRequest();
    }

    private class itemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            VideoEntity videoEntity = (VideoEntity) adapterView.getItemAtPosition(i);
            if (videoEntity != null) {
                Intent intent = new Intent(context, OtherPlayerActivity.class);
                intent.putExtra("obj", videoEntity);
                startActivity(intent);
            }
        }
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
            case R.id.iv_more:
                if (!isFinishing()) {
                    if (morePopup.isShowing()) {
                        morePopup.dismiss();
                    } else {
                        morePopup.show(getWindow().getDecorView());
                    }
                }
                break;
            case R.id.tv_msg:

                break;
            case R.id.tv_focu:
                if (isFocus) {
                    cancelFocusRequest();
                } else {
                    focusSomeBodyRequest();
                }
                break;
        }
    }

    private void requestUserInfo(String id) {
        showLoadingDialog();
        UserInfoRequest request = new UserInfoRequest(TAG, id);
        request.send(new BaseDataRequest.RequestCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo pojo) {
                if (pojo != null) {
                    userId = pojo.getUserId();
                    signature = pojo.getSignature();
                    sex = pojo.getSex();
                    avatar = pojo.getAvatar();
                    userName = pojo.getUserName();
                    initViews();
                }
            }

            @Override
            public void onFailure(String msg) {
                cancelFocusRequest();
                showToast(msg, true);
            }
        });
    }

    private void requestDatas() {
        showLoadingDialog();
        OtherProRequest request = new OtherProRequest(TAG, userId, "" + pager, "" + PAGER_NUM);
        request.send(new BaseDataRequest.RequestCallback<HomeBaseData>() {
            @Override
            public void onSuccess(HomeBaseData pojo) {
                cancelLoadingDialog();
                initDatas(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                if (msg.equals("no record")) {
                    gridView.onFinishLoading(false, null);
                    loadMore = false;
                } else {
//                    showToast("数据加载完成", true);
                }
            }
        });
    }

    private void initDatas(HomeBaseData baseData) {
        if (baseData != null) {
            ArrayList<VideoEntity> videos = baseData.getData();
            if (videos != null && videos.size() != 0) {
                adapter.removeAllItems();
                adapter.addMoreItems(videos);
            } else {
                loadMore = false;
                gridView.onFinishLoading(false, null);
            }

//            if (videoEntity != null) {
            tv_intro.setText(signature);
//                String sex = videoEntity.getSex();
            tv_name.setText(userName);
            Drawable nav_up = null;
            if (sex.equals("1")) {
                nav_up = getResources().getDrawable(R.drawable.works_man);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            } else {
                nav_up = getResources().getDrawable(R.drawable.works_lady);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            }
            tv_name.setCompoundDrawables(null, null, nav_up, null);
//                tv_focus.setText("关注：" + videoEntity.getFollowCount());
//                tv_fans.setText("粉丝：" + videoEntity.getFansCount());

//                String videoPath = videoEntity.getMediaPath();
//                String imageUrl = "";
//                if (videoPath.contains(".mp4")) {
//                    imageUrl = videoPath.replace(".mp4", ".jpg");
//                } else if (videoPath.contains(".mov")) {
//                    imageUrl = videoPath.replace(".mov", ".jpg");
//                }
            imageLoader.displayImage(avatar, iv_bg, ImageLoaderUtils.getDisplayImageOptions());
        }
    }
//    }

    private void requestMoreDatas() {
        pager++;
        OtherProRequest request = new OtherProRequest(TAG, userId, "" + pager, "" + PAGER_NUM);
        request.send(new BaseDataRequest.RequestCallback<HomeBaseData>() {
            @Override
            public void onSuccess(HomeBaseData pojo) {
                cancelLoadingDialog();
                bindData(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                if (msg.equals("no record")) {
                    gridView.onFinishLoading(false, null);
                    loadMore = false;
                } else {
//                    showToast("数据加载完成", true);
                }
            }
        });
    }

    private void bindData(HomeBaseData homeBaseData) {
        if (homeBaseData != null) {
            ArrayList<VideoEntity> videos = homeBaseData.getData();
            int size = videos.size();
            if (videos != null && size != 0) {
                gridView.onFinishLoading(true, videos);
            } else {
//                showToast("加载完成!", true);
                gridView.onFinishLoading(false, null);
                loadMore = false;
            }
        }
    }

    private void requestFans() {
        FansRequest request = new FansRequest(TAG, userId);
        request.send(new BaseDataRequest.RequestCallback<FansEntity>() {
            @Override
            public void onSuccess(FansEntity pojo) {
                tv_focus.setText("关注：" + pojo.getFollowCount());
                tv_fans.setText("粉丝：" + pojo.getFansCount());
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
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

    @Override
    public void onItemClick(int type) {
        switch (type) {
            case 0:

                break;
            case 1://举报
                showToast("举报", true);
                break;
            case 2://加入黑名单
                showToast("加入黑名单", true);
                break;
        }
    }

    /**
     * 是否关注请求
     */
    private void isFocusRequest() {
//        if (videoEntity == null) {
//            return;
//        }
        FocusJudgeRequest request = new FocusJudgeRequest(TAG, userId);
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                if (!TextUtils.isEmpty(pojo)) {
                    if (isFocus(pojo)) {
                        isFocus = true;
                        tv_fos.setBackgroundResource(R.drawable.bg_edit_blue);
                    } else {
                        isFocus = false;
                        tv_fos.setBackgroundResource(R.drawable.bg_edit_btn);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private boolean isFocus(String json) {
        boolean flag = false;
        try {
            JSONObject object = new JSONObject(json);
            flag = object.getBoolean("IsFollow");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return flag;
    }

    private void focusSomeBodyRequest() {
//        if (videoEntity == null) {
//            return;
//        }
        FocusSomeOneRequest request = new FocusSomeOneRequest(TAG, userId, "他人作品");
        request.send(new BaseDataRequest.RequestCallback<FocusEntity>() {
            @Override
            public void onSuccess(FocusEntity pojo) {
                cancelLoadingDialog();
                isFocus = true;
                tv_fos.setBackgroundResource(R.drawable.bg_edit_blue);
                showToast("关注成功!", true);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void cancelFocusRequest() {
//        if (videoEntity == null) {
//            return;
//        }
        FocusCancelRequest request = new FocusCancelRequest(TAG, userId);
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                isFocus = false;
                tv_fos.setBackgroundResource(R.drawable.bg_edit_btn);
                showToast("取消关注成功!", true);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }
}
