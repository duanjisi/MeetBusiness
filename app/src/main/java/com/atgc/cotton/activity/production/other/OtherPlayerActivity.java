package com.atgc.cotton.activity.production.other;

import android.content.Intent;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.activity.goodsDetail.GoodsDetailActivity;
import com.atgc.cotton.adapter.CommentAdapter;
import com.atgc.cotton.entity.BaseComment;
import com.atgc.cotton.entity.BaseGood;
import com.atgc.cotton.entity.Comment;
import com.atgc.cotton.entity.FocusEntity;
import com.atgc.cotton.entity.GoodBean;
import com.atgc.cotton.entity.VideoEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.ComOrReplyRequest;
import com.atgc.cotton.http.request.CommentsRequest;
import com.atgc.cotton.http.request.FocusCancelRequest;
import com.atgc.cotton.http.request.FocusJudgeRequest;
import com.atgc.cotton.http.request.FocusSomeOneRequest;
import com.atgc.cotton.http.request.LikeStatusRequest;
import com.atgc.cotton.http.request.MyGoodRequest;
import com.atgc.cotton.http.request.PraiseNoRequest;
import com.atgc.cotton.http.request.PraiseRequest;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.MycsLog;
import com.atgc.cotton.util.TimeUtil;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.CircleImageView;
import com.atgc.cotton.widget.MorePopup;
import com.atgc.cotton.widget.MyListView;
import com.atgc.cotton.widget.MyScrollView;
import com.atgc.cotton.widget.SharePopup;
import com.ksyun.media.shortvideo.kit.KSYEditKit;
import com.ksyun.media.shortvideo.utils.ShortVideoConstants;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/13.
 * 他人作品播放页
 */
public class OtherPlayerActivity extends BaseActivity implements
        MyScrollView.ScrollViewListener,
        View.OnClickListener,
        SharePopup.OnItemSelectedListener,
        MorePopup.OnItemdListener {
    private static final String TAG = OtherPlayerActivity.class.getSimpleName();
    private GLSurfaceView mEditPreviewView;
    private KSYEditKit mEditKit;
    private SharePopup sharePopup;
    private MorePopup morePopup;
    private RelativeLayout rl_top_bar;
    private ImageLoader imageLoader;
    private CircleImageView iv_avatar, iv_header;
    private ImageView iv_back, iv_more, iv_like, iv_share;
    private TextView tv_title, tv_intro, tv_name, tv_nick, tv_time,
            tv_play_num, tv_like, tv_desc, tv_focus, tv_focus2, tv_com_num,
            tv_qure_more;
    private EditText editText;
    private Button btn_comment;
    private MyScrollView scrollView;
    private RelativeLayout relativeLayout, rl_share;
    private LinearLayout linearLayout, container;
    private MyListView listView;
    private CommentAdapter adapter;
    private View middle;
    private float mImageHeight;
    private float mImageWidth;
    private Resources resources;
    public final static String SRC_URL = "srcurl";
    private VideoEntity videoEntity;
    private boolean hasLike = false;
    private boolean isFocus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_player);
        initViews();
    }

    private void initViews() {
        resources = getResources();
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        linearLayout = (LinearLayout) findViewById(R.id.container);
        container = (LinearLayout) findViewById(R.id.container2);
        rl_top_bar = (RelativeLayout) findViewById(R.id.rl_top_bar);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_more = (ImageView) findViewById(R.id.iv_more);

        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_like = (ImageView) findViewById(R.id.iv_like);
        iv_avatar = (CircleImageView) findViewById(R.id.iv_avatar);
        iv_header = (CircleImageView) findViewById(R.id.icon_header);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_intro = (TextView) findViewById(R.id.tv_intro);
        tv_name = (TextView) findViewById(R.id.tv_name);

        tv_nick = (TextView) findViewById(R.id.tv_nick);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_play_num = (TextView) findViewById(R.id.tv_play_num);
        tv_like = (TextView) findViewById(R.id.tv_like);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_focus = (TextView) findViewById(R.id.tv_focus);
        tv_focus2 = (TextView) findViewById(R.id.tv_focus2);
        tv_com_num = (TextView) findViewById(R.id.tv_com_num);
        tv_qure_more = (TextView) findViewById(R.id.tv_qure_more);
        editText = (EditText) findViewById(R.id.editText);
        btn_comment = (Button) findViewById(R.id.btn_comment);
        listView = (MyListView) findViewById(R.id.listview);
        adapter = new CommentAdapter(context);
        listView.setAdapter(adapter);

        sharePopup = new SharePopup(context, mController);
        sharePopup.setOnItemSelectedListener(this);
        morePopup = new MorePopup(context, false);
        morePopup.setOnItemSelectedListener(this);

        mEditPreviewView = (GLSurfaceView) findViewById(R.id.edit_preview);
        iv_back.setOnClickListener(this);
        iv_more.setOnClickListener(this);
        iv_like.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        tv_focus.setOnClickListener(this);
        tv_focus2.setOnClickListener(this);
        tv_qure_more.setOnClickListener(this);
        btn_comment.setOnClickListener(this);

        scrollView = (MyScrollView) findViewById(R.id.scrollview);
        middle = findViewById(R.id.item_midle);
        scrollView.setScrollViewListener(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_top_view);
        rl_share = (RelativeLayout) findViewById(R.id.rl_share);
        rl_share.setOnClickListener(this);
        mImageHeight = UIUtils.getScreenHeight(context);
        mImageWidth = UIUtils.getScreenWidth(context);

        Log.i("info", "================mImageHeight:" + mImageHeight);
        relativeLayout.getLayoutParams().width = (int) mImageWidth;
        relativeLayout.getLayoutParams().height = (int) mImageHeight;
//        mEditPreviewView.getLayoutParams().width = (int) mImageWidth;
//        mEditPreviewView.getLayoutParams().height = (int) mImageHeight;
        initEditKit();
        bindData();
        startEditPreview();
    }

    private void initShareData() {
        shareContent = getResources().getString(R.string.live_share_content);
        title = videoEntity.getUserName();
        String videoPath = videoEntity.getMediaPath();
        if (videoPath.contains(".mp4")) {
            imageUrl = videoPath.replace(".mp4", ".jpg");
        } else if (videoPath.contains(".mov")) {
            imageUrl = videoPath.replace(".mov", ".jpg");
        }
        targetUrl = videoEntity.getMediaPath();
    }

    private void initEditKit() {
        mEditKit = new KSYEditKit(this);
        mEditKit.setDisplayPreview(mEditPreviewView);
        mEditKit.setOnErrorListener(mOnErrorListener);
        mEditKit.setOnInfoListener(mOnInfoListener);
        Bundle bundle = getIntent().getExtras();
        videoEntity = (VideoEntity) bundle.getSerializable("obj");
//        url = bundle.getString(SRC_URL);
        Log.i("info", "============feedid:" + videoEntity.getId() + "\n" + "userid:" + videoEntity.getUserId());
        String url = videoEntity.getMediaPath();
        if (!TextUtils.isEmpty(url)) {
            mEditKit.setEditPreviewUrl(url);
        }
    }

    private void bindData() {
        if (videoEntity != null) {
            tv_name.setText(videoEntity.getUserName());
            tv_intro.setText(videoEntity.getSignature());
            imageLoader.displayImage(videoEntity.getAvatar(), iv_avatar, ImageLoaderUtils.getDisplayImageOptions());

            tv_nick.setText(videoEntity.getUserName());
            tv_desc.setText(videoEntity.getSignature());
            tv_time.setText(TimeUtil.getDateTime(videoEntity.getAddTime()));
            tv_play_num.setText(videoEntity.getBrowseCount() + "播放");
            tv_like.setText(videoEntity.getLikeCount());
            tv_com_num.setText(videoEntity.getCommentCount() + "评论");
            imageLoader.displayImage(videoEntity.getAvatar(), iv_header, ImageLoaderUtils.getDisplayImageOptions());
            initShareData();
        }
        requestMyGoods();
        requestComment();
        requestLike();
    }

    private void startEditPreview() {
        //设置预览的音量
        mEditKit.setVolume(0.4f);
        //设置是否循环预览
        mEditKit.setLooping(true);
        //开启预览
        mEditKit.startEditPreview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEditKit.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEditKit.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEditKit.stopEditPreview();
        mEditKit.release();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
                if (!isFinishing()) {
                    if (morePopup.isShowing()) {
                        morePopup.dismiss();
                    } else {
                        morePopup.show(getWindow().getDecorView());
                    }
                }
                break;
            case R.id.iv_share:
                if (!isFinishing()) {
                    if (sharePopup.isShowing()) {
                        sharePopup.dismiss();
                    } else {
                        sharePopup.show(getWindow().getDecorView());
                    }
                }
                break;
            case R.id.rl_share:
                if (!isFinishing()) {
                    if (sharePopup.isShowing()) {
                        sharePopup.dismiss();
                    } else {
                        sharePopup.show(getWindow().getDecorView());
                    }
                }
                break;
            case R.id.tv_focus:
                if (isFocus) {
                    cancelFocusRequest();
                } else {
                    focusSomeBodyRequest();
                }
                break;
            case R.id.tv_focus2:
                if (isFocus) {
                    cancelFocusRequest();
                } else {
                    focusSomeBodyRequest();
                }
                break;
            case R.id.iv_like:
                if (!hasLike) {
                    praiseRequest();
                } else {
                    praiseUnRequest();
                }
                break;
            case R.id.tv_qure_more:
                if (videoEntity != null) {
                    Intent intent = new Intent(context, CommentDetailsActivity.class);
                    intent.putExtra("obj", videoEntity);
                    startActivity(intent);
                }
                break;
            case R.id.btn_comment:
                requestCommentStr();
                break;
        }
    }


    @Override
    public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
        Log.i("info", "================x:" + x + "\n" + "y:" + y + "  oldx:" + oldx + "\n" + "oldy:" + oldy);
        switchView(y);
    }

    private void showTopBar(int y) {
        if (y > mImageHeight) {
            iv_back.setImageResource(R.drawable.nav_return);
            iv_more.setImageResource(R.drawable.nav_more);
            rl_top_bar.setBackgroundColor(resources.getColor(R.color.white));
            tv_title.setText("视频详情");
        } else {
            iv_back.setImageResource(R.drawable.play_return);
            iv_more.setImageResource(R.drawable.play_more);
            rl_top_bar.setBackgroundColor(resources.getColor(R.color.transparent));
            tv_title.setText("");
        }
    }

    private void switchView(int y) {
        showTopBar(y);
        if (y != 0) {
            if (middle.getVisibility() == View.VISIBLE) {
                middle.setVisibility(View.GONE);
            }
        } else {
            if (middle.getVisibility() != View.VISIBLE) {
                middle.setVisibility(View.VISIBLE);
            }
        }
    }

    private KSYEditKit.OnErrorListener mOnErrorListener = new KSYEditKit.OnErrorListener() {
        @Override
        public void onError(int type, long msg) {
            switch (type) {
                case ShortVideoConstants.SHORTVIDEO_ERROR_COMPOSE_FAILED_UNKNOWN:
                case ShortVideoConstants.SHORTVIDEO_ERROR_COMPOSE_FILE_CLOSE_FAILED:
                case ShortVideoConstants.SHORTVIDEO_ERROR_COMPOSE_FILE_FORMAT_NOT_SUPPORTED:
                case ShortVideoConstants.SHORTVIDEO_ERROR_COMPOSE_FILE_OPEN_FAILED:
                case ShortVideoConstants.SHORTVIDEO_ERROR_COMPOSE_FILE_WRITE_FAILED:
                    break;
                case ShortVideoConstants.SHORTVIDEO_ERROR_SDK_AUTHFAILED:
                    break;
                case ShortVideoConstants.SHORTVIDEO_ERROR_UPLOAD_KS3_TOKEN_ERROR:
                    break;
            }
        }
    };

    private KSYEditKit.OnInfoListener mOnInfoListener = new KSYEditKit.OnInfoListener() {
        @Override
        public Object onInfo(int type, String... msgs) {
//            Log.d(TAG, "on info:" + type);
            switch (type) {
                case ShortVideoConstants.SHORTVIDEO_EDIT_PREPARED:
                    break;
                case ShortVideoConstants.SHORTVIDEO_COMPOSE_START: {
                    mEditKit.pauseEditPreview();
                    return null;
                }
                case ShortVideoConstants.SHORTVIDEO_COMPOSE_FINISHED: {
                }
                case ShortVideoConstants.SHORTVIDEO_COMPOSE_ABORTED:
                    break;
                case ShortVideoConstants.SHORTVIDEO_GET_KS3AUTH: {
                    if (msgs.length == 6) {
                    } else {
                        return null;
                    }
                }
                default:
                    return null;
            }
            return null;
        }
    };

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


    private void requestMyGoods() {
        if (videoEntity == null) {
            return;
        }
        MyGoodRequest request = new MyGoodRequest(TAG, "26,47,49,52");
        request.send(new BaseDataRequest.RequestCallback<BaseGood>() {
            @Override
            public void onSuccess(BaseGood pojo) {
                initDatas(pojo);
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    private void initDatas(BaseGood baseGood) {
        if (baseGood != null) {
            ArrayList<GoodBean> goods = baseGood.getData();
            if (goods != null && goods.size() != 0) {
//                int width = UIUtils.dip2px(context, 80);
                for (int i = 0; i < goods.size(); i++) {
                    GoodBean bean = goods.get(i);
                    ImageView imageView = new ImageView(this);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    params.leftMargin = 5;
                    imageView.setLayoutParams(params);
//                    imageView.getLayoutParams().width = (int) width;
//                    imageView.getLayoutParams().height = (int) width;
                    imageView.setId(i);
                    imageView.setTag(bean);
                    imageView.setOnClickListener(new clickListener());
                    imageLoader.displayImage(bean.getGoodsImg(), imageView, ImageLoaderUtils.getDisplayImageOptions());
//                    linearLayout.addView(imageView);
                    addContainerView(linearLayout, imageView, UIUtils.dip2px(context, 60));
                }

                for (int i = 0; i < goods.size(); i++) {
                    GoodBean bean = goods.get(i);
                    ImageView imageView = new ImageView(this);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    params.leftMargin = 5;
                    imageView.setLayoutParams(params);
                    imageView.setId(i);
                    imageView.setTag(bean);
                    imageView.setOnClickListener(new clickListener());
                    imageLoader.displayImage(bean.getGoodsImg(), imageView, ImageLoaderUtils.getDisplayImageOptions());
                    addContainerView(container, imageView, UIUtils.dip2px(context, 108));
                }
            }
        }
    }

    private void addContainerView(LinearLayout container, ImageView imageView, int dis) {
        imageView.getLayoutParams().width = (int) dis;
        imageView.getLayoutParams().height = (int) dis;
        container.addView(imageView);
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            GoodBean good = (GoodBean) view.getTag();
            if (good != null) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("goodId", good.getGoodsId());
                startActivity(intent);
            }
        }
    }

    private void requestComment() {
        if (videoEntity == null) {
            return;
        }
        CommentsRequest request = new CommentsRequest(TAG, videoEntity.getId(), "1", "10");
        request.send(new BaseDataRequest.RequestCallback<BaseComment>() {
            @Override
            public void onSuccess(BaseComment pojo) {
                initList(pojo);
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    private void initList(BaseComment baseComment) {
        if (baseComment != null) {
            ArrayList<Comment> comments = baseComment.getData();
            if (comments != null && comments.size() != 0) {
                adapter.initData(getBefore(comments));
            }
        }
    }

    private ArrayList<Comment> getBefore(ArrayList<Comment> list) {
        int size = list.size();
        if (size > 5) {
            ArrayList<Comment> comments = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                comments.add(list.get(i));
            }
            tv_qure_more.setVisibility(View.VISIBLE);
            return comments;
        } else {
            return list;
        }
    }

    private void praiseRequest() {
        if (!App.getInstance().isLogin()) {
            showTipsDialog();
            return;
        }
        if (videoEntity == null) {
            return;
        }
        PraiseRequest request = new PraiseRequest(TAG, videoEntity.getId());
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                hasLike = true;
                iv_like.setImageResource(R.drawable.full_play_like_press);
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    private void praiseUnRequest() {
        if (!App.getInstance().isLogin()) {
            showTipsDialog();
            return;
        }
        if (videoEntity == null) {
            return;
        }
        PraiseNoRequest request = new PraiseNoRequest(TAG, videoEntity.getId());
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                hasLike = false;
                iv_like.setImageResource(R.drawable.full_play_like_default);
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    private void requestLike() {
        if (videoEntity == null) {
            return;
        }
        LikeStatusRequest request = new LikeStatusRequest(TAG, videoEntity.getId());
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                initLike(pojo);
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    private void initLike(String json) {
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject object = new JSONObject(json);
                hasLike = object.getBoolean("IsLike");
                if (hasLike) {
                    iv_like.setImageResource(R.drawable.full_play_like_press);
                } else {
                    iv_like.setImageResource(R.drawable.full_play_like_default);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void requestCommentStr() {
        if (!App.getInstance().isLogin()) {
            showTipsDialog();
            return;
        }
        String content = getText(editText);
        if (TextUtils.isEmpty(content)) {
            showToast("评论内容不能为空!", true);
            return;
        }
        if (videoEntity == null) {
            return;
        }

        ComOrReplyRequest request = new ComOrReplyRequest(TAG, videoEntity.getId(), "0", content, videoEntity.getUserId());
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
//                showToast("评论成功!", true);
                editText.setText("");
                requestComment();
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    /**
     * 是否关注请求
     */
    private void isFocusRequest() {
        if (videoEntity == null) {
            return;
        }
        FocusJudgeRequest request = new FocusJudgeRequest(TAG, videoEntity.getUserId());
        request.send(new BaseDataRequest.RequestCallback<FocusEntity>() {
            @Override
            public void onSuccess(FocusEntity pojo) {
                cancelLoadingDialog();
                if (pojo != null) {
                    if (pojo.isFollow()) {
                        isFocus = true;
                        tv_focus.setText("已关注");
                        tv_focus.setBackgroundResource(R.drawable.bg_edit_btn);
                        tv_focus2.setText("已关注");
                        tv_focus2.setBackgroundResource(R.drawable.bg_edit_btn);
                    } else {
                        isFocus = false;
                        tv_focus.setText("关注");
                        tv_focus.setBackgroundResource(R.drawable.bg_edit_blue);
                        tv_focus2.setText("关注");
                        tv_focus2.setBackgroundResource(R.drawable.bg_edit_blue);
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

    private void focusSomeBodyRequest() {
        if (!App.getInstance().isLogin()) {
            showTipsDialog();
            return;
        }
        if (videoEntity == null) {
            return;
        }
        FocusSomeOneRequest request = new FocusSomeOneRequest(TAG, videoEntity.getUserId(), "他人作品");
        request.send(new BaseDataRequest.RequestCallback<FocusEntity>() {
            @Override
            public void onSuccess(FocusEntity pojo) {
                cancelLoadingDialog();
                isFocus = true;
                tv_focus.setText("已关注");
                tv_focus.setBackgroundResource(R.drawable.bg_edit_btn);
                tv_focus2.setText("已关注");
                tv_focus2.setBackgroundResource(R.drawable.bg_edit_btn);
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
        if (!App.getInstance().isLogin()) {
            showTipsDialog();
            return;
        }
        if (videoEntity == null) {
            return;
        }
        FocusCancelRequest request = new FocusCancelRequest(TAG, videoEntity.getUserId());
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                isFocus = false;
                tv_focus.setText("关注");
                tv_focus.setBackgroundResource(R.drawable.bg_edit_blue);
                tv_focus2.setText("关注");
                tv_focus2.setBackgroundResource(R.drawable.bg_edit_blue);
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
