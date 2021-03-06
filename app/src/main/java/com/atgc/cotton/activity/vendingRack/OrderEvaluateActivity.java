package com.atgc.cotton.activity.vendingRack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.atgc.cotton.App;
import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.entity.ChangeAvatarEntity;
import com.atgc.cotton.entity.OrderGoodsEntity;
import com.atgc.cotton.http.HttpUrl;
import com.atgc.cotton.listener.PermissionListener;
import com.atgc.cotton.util.FileUtils;
import com.atgc.cotton.util.PermissonUtil.PermissionUtil;
import com.atgc.cotton.util.PhotoAlbumUtil.MultiImageSelector;
import com.atgc.cotton.util.PhotoAlbumUtil.MultiImageSelectorActivity;
import com.atgc.cotton.util.ToastUtil;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.ActionSheet;
import com.atgc.cotton.widget.GlideRoundTransform;
import com.atgc.cotton.widget.MyRatingBar;
import com.atgc.cotton.widget.RoundImageView;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by GMARUnity on 2017/7/3.
 */
public class OrderEvaluateActivity extends BaseActivity implements View.OnClickListener, ActionSheet.OnSheetItemClickListener {

    private TextView tv_back;
    private Button bt_upload;
    private RoundImageView iv_icon;
    private MyRatingBar starBar;
    private EditText et_content;
    private LinearLayout ll_img;
    private View v_add_img;

    private PermissionListener permissionListener;

    private final int OPEN_CAMERA = 1;//相机
    private final int OPEN_ALBUM = 2;//相册
    private int cameraType;//1:相机 2：相册 3：视频
    private Uri imageUri;
    private String photoPath;
    private int addSize = 0, addTag;
    private HashMap<Integer, View> imgMap;
    private HashMap<Integer, String> imgStrMap;
    private int sceenW;
    private OrderGoodsEntity orderEntity;
    private AccountEntity accountEntity;
    private String access_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_evaluate);
        initView();
    }

    private void initView() {
        imgMap = new HashMap<>();
        imgStrMap = new HashMap<>();
        sceenW = UIUtils.getScreenWidth(this);
        accountEntity = App.getInstance().getAccountEntity();
        access_token = accountEntity.getToken();
        ll_img = (LinearLayout) findViewById(R.id.ll_img);
        et_content = (EditText) findViewById(R.id.et_content);
        starBar = (MyRatingBar) findViewById(R.id.ratingBar);
        iv_icon = (RoundImageView) findViewById(R.id.iv_icon);
        bt_upload = (Button) findViewById(R.id.bt_upload);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        bt_upload.setOnClickListener(this);
        v_add_img = View.inflate(this, R.layout.item_vend_add_img, null);
        TextView tv_add_img = (TextView) v_add_img.findViewById(R.id.tv_add_img);
        et_content.getLayoutParams().height = (int) (sceenW / 2 * 0.9);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_add_img.getLayoutParams();
        params.width = sceenW / 5;
        params.height = sceenW / 5;

        LinearLayout.LayoutParams Headparams = (LinearLayout.LayoutParams) iv_icon.getLayoutParams();
        Headparams.width = sceenW / 5;
        Headparams.height = sceenW / 5;
        iv_icon.setLayoutParams(Headparams);

        int pad = sceenW / 22;
        params.setMargins(0, pad, pad, pad);
        tv_add_img.setLayoutParams(params);
        ll_img.addView(v_add_img);
        tv_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionSheet();
            }
        });
//        Glide.with(OrderEvaluateActivity.this).load(path).
//                error(R.drawable.zf_default_message_image).transform(new GlideRoundTransform(this, 10)).into(iv_icon);
        starBar.setOnRatingBarChangeListener(new MyRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(MyRatingBar MyRatingBar, float rating, boolean fromUser) {
                Log.i("rating:", "" + rating);
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            orderEntity = (OrderGoodsEntity) intent.getSerializableExtra("data");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_upload:
                uploadImageFile();
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.iv_close:
                int addTag = (int) v.getTag();
                addViewToContainer(false, addTag, null);
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
                        File photoFile = createImgFile();
                        imageUri = Uri.fromFile(photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, OPEN_CAMERA);
                        }
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        String imageName = System.currentTimeMillis() + ".jpg";
                        String savePath = Environment.getExternalStorageDirectory() + "/IMProject/";
                        File file = new File(savePath, imageName);
                        imageUri = FileProvider.getUriForFile(OrderEvaluateActivity.this, "com.boss66.meetbusiness.fileProvider", file);//这里进行替换uri的获得方式
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
                        startActivityForResult(intent, OPEN_CAMERA);
                    }
                } else if (cameraType == OPEN_ALBUM) {
                    int size = 4 - addSize;
                    MultiImageSelector.create(OrderEvaluateActivity.this).
                            showCamera(false).
                            count(size)
                            .multi() // 多选模式, 默认模式;
                            .start(OrderEvaluateActivity.this, OPEN_ALBUM);
                }
            }

            @Override
            public void onRequestPermissionError() {
                ToastUtil.showShort(OrderEvaluateActivity.this, getString(R.string.giving_camera_permissions));
            }
        };
        PermissionUtil
                .with(this)
                .permissions(
                        PermissionUtil.PERMISSIONS_GROUP_CAMERA_1 //相机权限
                ).request(permissionListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, permissionListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_CAMERA && resultCode == RESULT_OK) {//打开相机
            if (imageUri != null) {
                String path = null;
                if (Build.VERSION.SDK_INT < 24) {
                    path = FileUtils.getPath(this, imageUri);
                } else {
                    path = imageUri.toString();
                }
                imgStrMap.put(addTag, path);
                addViewToContainer(true, 0, path);
            }
        } else if (requestCode == OPEN_ALBUM && resultCode == RESULT_OK && data != null) { //打开相册
            ArrayList<String> selectPicList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            if (selectPicList != null) {
                int size = selectPicList.size();
                for (int i = 0; i < size; i++) {
                    String path = selectPicList.get(i);
                    imgStrMap.put(addTag, path);
                    addViewToContainer(true, 0, path);
                }
            }
        }
    }

    private void addViewToContainer(boolean isAdd, int tag, String path) {
        if (isAdd) {
            View v_add_img = View.inflate(this, R.layout.item_vend_add_img_content, null);
            RelativeLayout rl_item = (RelativeLayout) v_add_img.findViewById(R.id.rl_item);
            ImageView iv_close = (ImageView) v_add_img.findViewById(R.id.iv_close);
            ImageView iv_icon = (ImageView) v_add_img.findViewById(R.id.iv_icon);

            Glide.with(OrderEvaluateActivity.this).load(path).
                    error(R.drawable.zf_default_message_image).transform(new GlideRoundTransform(this, 10)).into(iv_icon);

            iv_close.setTag(addTag);
            iv_close.setOnClickListener(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl_item.getLayoutParams();
            params.width = sceenW / 5;
            params.height = sceenW / 5;
            int pad = sceenW / 22;
            params.setMargins(0, pad, pad, pad);
            rl_item.setLayoutParams(params);
            ll_img.addView(v_add_img, addSize);
            imgMap.put(addTag, v_add_img);
            addTag++;
            addSize++;
        } else {
            ll_img.removeView(imgMap.get(tag));
            imgMap.remove(tag);
            imgStrMap.remove(tag);
            addSize--;
        }
//        if (imgMap.size() > 0) {
//            View img = ll_img.getChildAt(0);
//            TextView tv_img = (TextView) img.findViewById(R.id.tv_img);
//            tv_img.setVisibility(View.VISIBLE);
//        }
    }

    private void showActionSheet() {
        ActionSheet actionSheet = new ActionSheet(OrderEvaluateActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true);
        actionSheet
                .addSheetItem(getString(R.string.take_photos), ActionSheet.SheetItemColor.Black,
                        OrderEvaluateActivity.this)
                .addSheetItem(getString(R.string.from_the_mobile_phone_photo_album_choice), ActionSheet.SheetItemColor.Black,
                        OrderEvaluateActivity.this);
        actionSheet.show();
    }

    @Override
    public void onClick(int which) {
        switch (which) {
            case 1:
                cameraType = OPEN_CAMERA;
                break;
            case 2:
                cameraType = OPEN_ALBUM;
                break;
        }
        getPermission();
    }

    /**
     * 自定义图片名，获取照片的file
     */
    private File createImgFile() {
        //创建文件
        String fileName = "img_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";//确定文件名
        File dir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStorageDirectory();
        } else {
            dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        File tempFile = new File(dir, fileName);
        try {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取文件路径
        photoPath = tempFile.getAbsolutePath();
        return tempFile;
    }

    private void uploadImageFile() {
        String main = HttpUrl.GOODS_COMMENT;
        HttpUtils httpUtils = new HttpUtils(60 * 1000);//实例化RequestParams对象
        com.lidroid.xutils.http.RequestParams params = new com.lidroid.xutils.http.RequestParams();
        params.addHeader("Authorization", access_token);
        params.addBodyParameter("orderid", "" + orderEntity.getOrderId());
        params.addBodyParameter("goodsid", "" + orderEntity.getGoodsId());
        String content = getText(et_content);
        if (!TextUtils.isEmpty(content)) {
            params.addBodyParameter("content", content);
        }
        String score = "" + starBar.getRating();
        if (!score.equals("") && !score.equals("0.0")) {
            params.addBodyParameter("score", score);
        } else {
            showToast("评分不能为空！", true);
            return;
        }
        for (int i = 0; i < 9; i++) {
            String path = imgStrMap.get(i);
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                if (file.exists() && file.length() > 0) {
                    params.addBodyParameter("pic" + i, file);
                }
            }
        }
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
                        } else {
                            ToastUtil.showShort(context, "发布成功!");
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
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

    @SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }
}
