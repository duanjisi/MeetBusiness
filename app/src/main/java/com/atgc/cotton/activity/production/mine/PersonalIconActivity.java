package com.atgc.cotton.activity.production.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.atgc.cotton.App;
import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.http.HttpUrl;
import com.atgc.cotton.listener.PermissionListener;
import com.atgc.cotton.util.FileUtils;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.PermissonUtil.PermissionUtil;
import com.atgc.cotton.util.PhotoAlbumUtil.MultiImageSelector;
import com.atgc.cotton.util.PhotoAlbumUtil.MultiImageSelectorActivity;
import com.atgc.cotton.util.ToastUtil;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.util.Utils;
import com.atgc.cotton.widget.ActionSheet;
import com.atgc.cotton.widget.photoview.PhotoView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * 个人头像
 */
public class PersonalIconActivity extends BaseActivity implements View.OnClickListener, ActionSheet.OnSheetItemClickListener {

    private TextView tv_back, tv_right;
    private PhotoView iv_icon;
    private ActionSheet actionSheet;
    private final int OPEN_CAMERA = 1;//相机
    private final int OPEN_ALBUM = 2;//相册
    private final int REQUEST_CLIP_IMAGE = 3;//裁剪
    private Bitmap bitmap;
    private boolean isLongClick = false;
    private String mOutputPath;
    private String savePath;
    private ImageLoader imageLoader;
    private Uri imageUri;
    private String imageName;
    private String access_token;
    private boolean isHeadChange = false;
    private String headurl;
    private PermissionListener permissionListener;
    private int cameraType;//1:相机 2：相册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_icon);
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20) {
            savePath = getFilesDir().getPath() + "/";
        } else {
            savePath = Environment.getExternalStorageDirectory() + "/IMProject/";
        }
        access_token = App.getInstance().getAccountEntity().getToken();
        mOutputPath = new File(getExternalCacheDir(), "chosen.jpg").getPath();
        int screenW = UIUtils.getScreenWidth(this);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_right = (TextView) findViewById(R.id.tv_right);
        iv_icon = (PhotoView) findViewById(R.id.iv_icon);
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) iv_icon.getLayoutParams();
        linearParams.height = screenW;
        iv_icon.setLayoutParams(linearParams);
        tv_back.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        iv_icon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isLongClick = true;
                showActionSheet();
                return true;
            }
        });
        imageLoader = ImageLoaderUtils.createImageLoader(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String head = bundle.getString("head");
                if (!TextUtils.isEmpty(head)) {
                    imageLoader.displayImage(head, iv_icon,
                            ImageLoaderUtils.getDisplayImageOptions());
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                goBack();
                break;
            case R.id.tv_right:
                isLongClick = false;
                showActionSheet();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            goBack();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void goBack() {
        if (isHeadChange) {
            Intent intent = new Intent();
            intent.putExtra("headicon", headurl);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    private void showActionSheet() {
        actionSheet = new ActionSheet(PersonalIconActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true);
        if (isLongClick) {
            actionSheet.addSheetItem("保存图片", ActionSheet.SheetItemColor.Black,
                    PersonalIconActivity.this);
        } else {
            actionSheet.addSheetItem("拍照", ActionSheet.SheetItemColor.Black,
                    PersonalIconActivity.this)
                    .addSheetItem("从手机相册选择", ActionSheet.SheetItemColor.Black,
                            PersonalIconActivity.this)
                    .addSheetItem("保存图片", ActionSheet.SheetItemColor.Black,
                            PersonalIconActivity.this);
        }
        actionSheet.show();
    }

    @Override
    public void onClick(int which) {
        switch (which) {
            case 1://拍照 or 长按 保存图片
                if (isLongClick && iv_icon != null) {//长按 保存图片
                    iv_icon.setDrawingCacheEnabled(true);
                    bitmap = iv_icon.getDrawingCache();
                    if (bitmap != null) {
                        FileUtils.saveImageToGallery(this, bitmap);
                        iv_icon.setDrawingCacheEnabled(false);
                        ToastUtil.showShort(this, getString(R.string.success_save));
                    } else {
                        ToastUtil.showShort(this, getString(R.string.msg_could_not_save_photo));
                    }
                } else {//拍照
                    cameraType = OPEN_CAMERA;
                    getPermission();
                }
                break;
            case 2://从手机相册选择
                cameraType = OPEN_ALBUM;
                getPermission();
                break;
            case 3://保存图片
                iv_icon.setDrawingCacheEnabled(true);
                bitmap = iv_icon.getDrawingCache();
                if (bitmap != null) {
                    FileUtils.saveImageToGallery(this, bitmap);
                    iv_icon.setDrawingCacheEnabled(false);
                    ToastUtil.showShort(this, getString(R.string.success_save));
                } else {
                    ToastUtil.showShort(this, getString(R.string.msg_could_not_save_photo));
                }
                break;
        }
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
                if (Build.VERSION.SDK_INT >= 24 && path.contains("im.boss66.com.fileProvider") &&
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
        params.addBodyParameter("access_token", access_token);
        params.addBodyParameter("avatar", file);
        httpUtils.send(HttpRequest.HttpMethod.POST, main, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    cancelLoadingDialog();
                    Log.i("info", "==============responseInfo:" + responseInfo.toString());
//                    ChangeAvatarEntity entity = JSON.parseObject(responseInfo.result, ChangeAvatarEntity.class);
//                    if (entity != null) {
//                        if (entity.status == 401) {
//                            Intent intent = new Intent();
//                            intent.setAction(Constants.ACTION_LOGOUT_RESETING);
//                            App.getInstance().sendBroadcast(intent);
//                        } else {
//                            ChangeAvatarEntity.Result result = entity.getResult();
//                            if (result != null) {
//                                isHeadChange = true;
//                                ToastUtil.showShort(context, "更改成功");
//                                headurl = result.getAvatar();
//                                imageLoader.displayImage(headurl, iv_icon,
//                                        ImageLoaderUtils.getDisplayImageOptions());
//                                LoginStatus loginStatus = LoginStatus.getInstance();
//                                String avatar = result.getAvatar();
//                                if (!TextUtils.isEmpty(avatar)) {
//                                    loginStatus.setAvatar(avatar);
//                                }
//                            }
//                        }
//                    }
                } catch (JSONException e) {
                    ToastUtil.showShort(context, "上传失败");
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
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

    @SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
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
//                        // 指定调用相机拍照后照片的储存路径
//                        File dir = new File(savePath);
//                        if (!dir.exists()) {
//                            dir.mkdirs();
//                        }
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
                        imageUri = FileProvider.getUriForFile(PersonalIconActivity.this, "im.boss66.com.fileProvider", file);//这里进行替换uri的获得方式
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
                        startActivityForResult(intent, OPEN_CAMERA);
                    }

                } else if (cameraType == OPEN_ALBUM) {
                    MultiImageSelector.create(context).
                            showCamera(false).
                            count(1)
                            .multi() // 多选模式, 默认模式;
                            .start(PersonalIconActivity.this, OPEN_ALBUM);
                }
            }

            @Override
            public void onRequestPermissionError() {
                ToastUtil.showShort(PersonalIconActivity.this, getString(R.string.giving_camera_permissions));
            }
        };
        PermissionUtil
                .with(this)
                .permissions(
                        PermissionUtil.PERMISSIONS_GROUP_CAMERA //相机权限
                ).request(permissionListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, permissionListener);
    }

}
