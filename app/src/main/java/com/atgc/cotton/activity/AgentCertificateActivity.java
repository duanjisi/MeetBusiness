package com.atgc.cotton.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.activity.production.mine.ClipImageActivity;
import com.atgc.cotton.entity.AgentInfo;
import com.atgc.cotton.entity.AgentParam;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.AgentSaveRequest;
import com.atgc.cotton.listener.PermissionListener;
import com.atgc.cotton.util.PermissonUtil.PermissionUtil;
import com.atgc.cotton.util.PhotoAlbumUtil.MultiImageSelector;
import com.atgc.cotton.util.PhotoAlbumUtil.MultiImageSelectorActivity;
import com.atgc.cotton.util.Utils;
import com.atgc.cotton.widget.ActionSheet;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2018-01-21.
 * 上传证件
 */
public class AgentCertificateActivity extends BaseActivity implements ActionSheet.OnSheetItemClickListener {
    private static final String TAG = AgentCertificateActivity.class.getSimpleName();
    private ActionSheet actionSheet;
    private PermissionListener permissionListener;
    private int cameraType;//1:相机 2：相册
    private Uri imageUri;
    private String imageName;
    private String mOutputPath;
    private final int OPEN_CAMERA = 1;//相机
    private final int OPEN_ALBUM = 2;//相册
    private final int REQUEST_CLIP_IMAGE = 3;//裁剪
    private int selectType = 0;//0:正面,1：反面
    private String positivePath, reversePath, handlePath;
    private String savePath;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_positive)
    ImageView ivPositive;
    @BindView(R.id.iv_reverse)
    ImageView ivReverse;
    @BindView(R.id.iv_handheld)
    ImageView ivHandheld;
    @BindView(R.id.btn_next)
    Button btnNext;
    private AgentParam param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_upload);
        ButterKnife.bind(this);
        param = (AgentParam) getIntent().getExtras().getSerializable("obj");
        if (Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20) {
            savePath = getFilesDir().getPath() + "/";
        } else {
            savePath = Environment.getExternalStorageDirectory() + "/HDProject/";
        }
        mOutputPath = new File(getExternalCacheDir(), "chosen.jpg").getPath();
    }

    @OnClick({R.id.iv_back, R.id.iv_positive, R.id.iv_reverse, R.id.iv_handheld, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_positive:
                selectType = 0;
                showActionSheet();
                break;
            case R.id.iv_reverse:
                selectType = 1;
                showActionSheet();
                break;
            case R.id.iv_handheld:
                selectType = 2;
                showActionSheet();
                break;
            case R.id.btn_next:
                submit();
                break;
        }
    }

    private void submit() {
        if (param != null) {
            if (TextUtils.isEmpty(positivePath)) {
                showToast("正面照尚未传");
                return;
            }
            if (TextUtils.isEmpty(reversePath)) {
                showToast("反面照尚未传");
                return;
            }
            if (TextUtils.isEmpty(handlePath)) {
                showToast("手持身份证照尚未传");
                return;
            }
            param.setIdcardp(positivePath);
            param.setIdcardo(reversePath);
            param.setIdcardm(handlePath);

            HashMap<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(param.getId()));
            map.put("truename", param.getTruename());
            map.put("idcartno", param.getIdcartno());
            map.put("mobilephone", param.getMobilephone());
            map.put("smscode", param.getSmscode());
            map.put("idcardp", param.getIdcardp());
            map.put("idcardo", param.getIdcardo());
            map.put("idcardm", param.getIdcardm());
            map.put("account_user", param.getAccount_user());
            map.put("account", param.getAccount());
            map.put("bank", param.getBank());
            showLoadingDialog();
            AgentSaveRequest request = new AgentSaveRequest(TAG, map);
            request.send(new BaseDataRequest.RequestCallback<AgentInfo>() {
                @Override
                public void onSuccess(AgentInfo agentInfo) {
                    cancelLoadingDialog();
                    showToast("提交资料成功！");
                }

                @Override
                public void onFailure(String msg) {
                    cancelLoadingDialog();
                    showToast(msg);
                }
            });
        }
    }

    private void showActionSheet() {
        actionSheet = new ActionSheet(AgentCertificateActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true);
        actionSheet.addSheetItem("从手机相册选择", ActionSheet.SheetItemColor.Black,
                AgentCertificateActivity.this)
                .addSheetItem("拍照片", ActionSheet.SheetItemColor.Black,
                        AgentCertificateActivity.this);
        actionSheet.show();
    }

    @Override
    public void onClick(int which) {
        switch (which) {
            case 1:
                cameraType = OPEN_ALBUM;
                getPermission();
                break;
            case 2:
                cameraType = OPEN_CAMERA;
                getPermission();
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
                        // 指定调用相机拍照后照片的储存路径
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
                        imageUri = FileProvider.getUriForFile(AgentCertificateActivity.this, "com.atgc.cotton.fileProvider", file);//这里进行替换uri的获得方式
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
                        startActivityForResult(intent, OPEN_CAMERA);
                    }
                } else if (cameraType == OPEN_ALBUM) {
                    MultiImageSelector.create(context).
                            showCamera(false).
                            count(1)
                            .multi() // 多选模式, 默认模式;
                            .start(AgentCertificateActivity.this, OPEN_ALBUM);
                }
            }

            @Override
            public void onRequestPermissionError() {
                showToast(getString(R.string.giving_camera_permissions));
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

    @SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String fileName = System.currentTimeMillis() + ".jpg";
        mOutputPath = new File(getExternalCacheDir(), fileName).getPath();
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
                showImageFile(path);
            }
            return;
        }
    }

    private void showImageFile(String path) {
        switch (selectType) {
            case 0:
                positivePath = path;
                Glide.with(context).load(path).
                        placeholder(R.drawable.zf_default_message_image).
                        crossFade().into(ivPositive);
                break;
            case 1:
                reversePath = path;
                Glide.with(context).load(path).
                        placeholder(R.drawable.zf_default_message_image).
                        crossFade().into(ivReverse);
                break;
            case 2:
                handlePath = path;
                Glide.with(context).load(path).
                        placeholder(R.drawable.zf_default_message_image).
                        crossFade().into(ivHandheld);
                break;
        }
    }
}
