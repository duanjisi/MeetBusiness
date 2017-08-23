package com.atgc.cotton.activity.production.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.atgc.cotton.App;
import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.config.LoginStatus;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.entity.ActionEntity;
import com.atgc.cotton.entity.ChangeAvatarEntity;
import com.atgc.cotton.entity.FansEntity;
import com.atgc.cotton.fragment.MyLikeFragment;
import com.atgc.cotton.fragment.MyProFragment;
import com.atgc.cotton.fragment.ProductFragment;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;
import com.atgc.cotton.http.request.FansRequest;
import com.atgc.cotton.listener.PermissionListener;
import com.atgc.cotton.listenter.ListenerConstans;
import com.atgc.cotton.listenter.ViewPagerListener;
import com.atgc.cotton.util.FileUtils;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.MycsLog;
import com.atgc.cotton.util.PermissonUtil.PermissionUtil;
import com.atgc.cotton.util.PhotoAlbumUtil.MultiImageSelector;
import com.atgc.cotton.util.PhotoAlbumUtil.MultiImageSelectorActivity;
import com.atgc.cotton.util.ToastUtil;
import com.atgc.cotton.util.Utils;
import com.atgc.cotton.widget.ActionSheet;
import com.atgc.cotton.widget.SharePopup;
import com.atgc.cotton.widget.SimpleViewPagerIndicator;
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
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Johnny on 2017/7/5.
 * 我的作品
 */
public class MyProductionActivity extends BaseActivity implements
        View.OnClickListener,
        ViewPagerListener,
        SharePopup.OnItemSelectedListener,
        ActionSheet.OnSheetItemClickListener {
    private static final String TAG = MyProductionActivity.class.getSimpleName();
    private AccountEntity account;
    private ActionSheet actionSheet;
    private final int OPEN_CAMERA = 1;//相机
    private final int OPEN_ALBUM = 2;//相册
    private final int REQUEST_CLIP_IMAGE = 3;//裁剪
    private Bitmap bitmap;
    private String mOutputPath;
    private String savePath;
    private PermissionListener permissionListener;
    private int cameraType;//1:相机 2：相册
    private Uri imageUri;
    private String imageName;
    private String access_token;

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
        EventBus.getDefault().register(this);
        initViews();
        initDatas();
        initEvents();
    }

    @Subscribe
    public void onMessageEvent(ActionEntity event) {
        if (event != null) {
            String action = event.getAction();
            String tag = (String) event.getData();
            if (action.equals(Constants.Action.UPDATE_ACCOUNT_INFORM)) {
                LoginStatus sLoginStatus = LoginStatus.getInstance();
                if (tag.equals("avatar")) {
                    String avatar = sLoginStatus.getAvatar();
                    imageLoader.displayImage(avatar, iv_bg, ImageLoaderUtils.getDisplayImageOptions());
                } else if (tag.equals("name")) {
                    String name = sLoginStatus.getUsername();
                    tv_title.setText(name);
                } else if (tag.equals("sex")) {
                    String sex = sLoginStatus.getSex();
                    Drawable nav_up = null;
                    if (sex.equals("0")) {
                        nav_up = getResources().getDrawable(R.drawable.works_man);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    } else {
                        nav_up = getResources().getDrawable(R.drawable.works_lady);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    }
                    tv_title.setCompoundDrawables(null, null, nav_up, null);
                } else if (tag.equals("signature")) {
                    String signature = sLoginStatus.getIntro();
                    tv_intro.setText(signature);
                }
            }
        }
    }

    private void initViews() {
        account = App.getInstance().getAccountEntity();
        access_token = account.getToken();
        if (Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20) {
            savePath = getFilesDir().getPath() + "/";
        } else {
            savePath = Environment.getExternalStorageDirectory() + "/IMProject/";
        }
        mOutputPath = new File(getExternalCacheDir(), "chosen.jpg").getPath();

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
        iv_bg.setOnClickListener(this);
        tv_focus.setOnClickListener(this);
        tv_fans.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_intro.setOnClickListener(this);
        iv_bg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showActionSheet();
                return true;
            }
        });
        request();
    }

    private void initDatas() {
        mIndicator.setTitles(mTitles);
        myProFragment = new MyProFragment();
        likeFragment = new MyLikeFragment();
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

    private void showActionSheet() {
        actionSheet = new ActionSheet(MyProductionActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true);
//        if (isLongClick) {
//            actionSheet.addSheetItem("保存图片", ActionSheet.SheetItemColor.Black,
//                    MyProductionActivity.this);
//        } else {
        actionSheet.addSheetItem("拍照", ActionSheet.SheetItemColor.Black,
                MyProductionActivity.this)
                .addSheetItem("从手机相册选择", ActionSheet.SheetItemColor.Black,
                        MyProductionActivity.this)
                .addSheetItem("保存图片", ActionSheet.SheetItemColor.Black,
                        MyProductionActivity.this);
//        }
        actionSheet.show();
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
            case R.id.iv_bg:

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
//        showLoadingDialog();
//        MyNumRequest request = new MyNumRequest(TAG);
//        request.send(new BaseDataRequest.RequestCallback<MyNumEntity>() {
//            @Override
//            public void onSuccess(MyNumEntity pojo) {
//                cancelLoadingDialog();
//                bindDatas(pojo);
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                cancelLoadingDialog();
//                showToast(msg, true);
//            }
//        });
        showLoadingDialog();
        FansRequest request = new FansRequest(TAG, account.getUserId());
        request.send(new BaseDataRequest.RequestCallback<FansEntity>() {
            @Override
            public void onSuccess(FansEntity pojo) {
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

    private void bindDatas(FansEntity entity) {
        if (entity != null) {
            String sex = account.getSex();
            tv_title.setText(account.getUserName());
            Drawable nav_up = null;
            if (sex.equals("1")) {
                nav_up = getResources().getDrawable(R.drawable.works_man);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            } else {
                nav_up = getResources().getDrawable(R.drawable.works_lady);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            }
            tv_title.setCompoundDrawables(null, null, nav_up, null);
            tv_focus.setText("关注：" + entity.getFollowCount());
            tv_fans.setText("粉丝：" + entity.getFansCount());
            tv_intro.setText(account.getSignature());
            imageLoader.displayImage(account.getAvatar(), iv_bg, ImageLoaderUtils.getDisplayImageOptions());
        }
    }

    @Override
    public void onClick(int which) {
        switch (which) {
            case 1://拍照 or 长按 保存图片
//                if (isLongClick && iv_bg != null) {//长按 保存图片
//                    iv_bg.setDrawingCacheEnabled(true);
//                    bitmap = iv_bg.getDrawingCache();
//                    if (bitmap != null) {
//                        FileUtils.saveImageToGallery(this, bitmap);
//                        iv_bg.setDrawingCacheEnabled(false);
//                        ToastUtil.showShort(this, getString(R.string.success_save));
//                    } else {
//                        ToastUtil.showShort(this, getString(R.string.msg_could_not_save_photo));
//                    }
//                } else {//拍照
                cameraType = OPEN_CAMERA;
                getPermission();
//                }
                break;
            case 2://从手机相册选择
                cameraType = OPEN_ALBUM;
                getPermission();
                break;
            case 3://保存图片
                iv_bg.setDrawingCacheEnabled(true);
                bitmap = iv_bg.getDrawingCache();
                if (bitmap != null) {
                    FileUtils.saveImageToGallery(this, bitmap);
                    iv_bg.setDrawingCacheEnabled(false);
                    ToastUtil.showShort(this, getString(R.string.success_save));
                } else {
                    ToastUtil.showShort(this, getString(R.string.msg_could_not_save_photo));
                }
                break;
        }
    }

    private void getPermission() {
        permissionListener = new PermissionListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, permissionListener);
            }

            @Override
            public void onRequestPermissionSuccess() {
                if (cameraType == OPEN_CAMERA) {
                    if (Build.VERSION.SDK_INT < 24) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        imageName = getNowTime() + ".jpg";
                        File dir;
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            dir = Environment.getExternalStorageDirectory();
                        } else {
                            dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        }
                        File file = new File(dir, imageName);
                        imageUri = Uri.fromFile(file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, OPEN_CAMERA);
                        }
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        String imageName = getNowTime() + ".jpg";
                        File file = new File(savePath, imageName);
                        imageUri = FileProvider.getUriForFile(MyProductionActivity.this, "com.atgc.cotton.fileProvider", file);//这里进行替换uri的获得方式
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
                        startActivityForResult(intent, OPEN_CAMERA);
                    }

                } else if (cameraType == OPEN_ALBUM) {
                    MultiImageSelector.create(context).
                            showCamera(false).
                            count(1)
                            .multi() // 多选模式, 默认模式;
                            .start(MyProductionActivity.this, OPEN_ALBUM);
                }
            }

            @Override
            public void onRequestPermissionError() {
                ToastUtil.showShort(MyProductionActivity.this, getString(R.string.giving_camera_permissions));
            }
        };
        PermissionUtil
                .with(this)
                .permissions(
                        PermissionUtil.PERMISSIONS_GROUP_CAMERA //相机权限
                ).request(permissionListener);
    }

    @SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_CAMERA && resultCode == RESULT_OK) {    //打开相机
            if (imageUri != null) {
                String path = null;
                if (Build.VERSION.SDK_INT < 24) {
                    path = Utils.getPath(this, imageUri);
                } else {
                    path = imageUri.toString();
                }
                if (Build.VERSION.SDK_INT >= 24 && path.contains("com.atgc.cotton.fileProvider") &&
                        path.contains("/IMProject/")) {
                    String[] arr = path.split("/IMProject/");
                    if (arr != null && arr.length > 1) {
                        path = savePath + arr[1];
                    }
                }
                if (!TextUtils.isEmpty(path)) {
                    ClipImageActivity.prepare()
                            .aspectX(2).aspectY(2)//裁剪框横向及纵向上的比例
                            .inputPath(path).outputPath(mOutputPath)//要裁剪的图片地址及裁剪后保存的地址
                            .startForResult(this, REQUEST_CLIP_IMAGE);
                }
            }
        } else if (requestCode == OPEN_ALBUM && resultCode == RESULT_OK) { //打开相册
            if (data == null) {
                return;
            } else {
                ArrayList<String> selectPicList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (selectPicList != null && selectPicList.size() > 0) {
                    String path = selectPicList.get(0);
                    if (!TextUtils.isEmpty(path)) {
                        ClipImageActivity.prepare()
                                .aspectX(2).aspectY(2)//裁剪框横向及纵向上的比例
                                .inputPath(path).outputPath(mOutputPath)//要裁剪的图片地址及裁剪后保存的地址
                                .startForResult(this, REQUEST_CLIP_IMAGE);
                    }
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CLIP_IMAGE) {
            String path = ClipImageActivity.ClipOptions.createFromBundle(data).getOutputPath();
            if (path != null) {
                uploadImageFile(path);
            }
            return;
        }
    }

    private void uploadImageFile(String path) {
        showLoadingDialog();
        String main = HttpUrl.USER_AVATAR_URL;
        HttpUtils httpUtils = new HttpUtils(60 * 1000);//实例化RequestParams对象
        com.lidroid.xutils.http.RequestParams params = new com.lidroid.xutils.http.RequestParams();
        File file = new File(path);
        params.addHeader("Authorization", access_token);
        params.addBodyParameter("avatar", file);
        httpUtils.send(HttpRequest.HttpMethod.PUT, main, params, new RequestCallBack<String>() {
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
                        } else {
                            ChangeAvatarEntity.Data result = entity.getData();
                            if (result != null) {
                                ToastUtil.showShort(context, "更改成功");
                                String avatar = result.getAvatar();
                                imageLoader.displayImage(avatar, iv_bg,
                                        ImageLoaderUtils.getDisplayImageOptions());
                                LoginStatus loginStatus = LoginStatus.getInstance();
                                if (!TextUtils.isEmpty(avatar)) {
                                    loginStatus.setAvatar(avatar);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    ToastUtil.showShort(context, "上传失败");
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("info", "==============HttpException:" + e.getMessage());
                int code = e.getExceptionCode();
                if (code == 401) {
                    goLogin();
                } else {
                    cancelLoadingDialog();
                    showToast(s, false);
                }
            }
        });
    }

    private void goLogin() {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_LOGOUT_RESETING);
        App.getInstance().sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
