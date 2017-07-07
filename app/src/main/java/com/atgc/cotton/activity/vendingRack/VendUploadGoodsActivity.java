package com.atgc.cotton.activity.vendingRack;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.util.KeyboardUtils;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.GlideRoundTransform;
import com.atgc.cotton.R;
import com.atgc.cotton.adapter.VendUploadGoodsAdapter;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.entity.VendUploadEntity;
import com.atgc.cotton.http.request.VendUploadGoodsRequest;
import com.atgc.cotton.listener.PermissionListener;
import com.atgc.cotton.util.FileUtils;
import com.atgc.cotton.util.PermissonUtil.PermissionUtil;
import com.atgc.cotton.util.PhotoAlbumUtil.MultiImageSelector;
import com.atgc.cotton.util.PhotoAlbumUtil.MultiImageSelectorActivity;
import com.atgc.cotton.util.ToastUtil;
import com.atgc.cotton.widget.ActionSheet;
import com.bumptech.glide.Glide;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by GMARUnity on 2017/6/19.
 */
public class VendUploadGoodsActivity extends AppCompatActivity implements View.OnClickListener, ActionSheet.OnSheetItemClickListener {

    private static final String TAG = VendUploadGoodsActivity.class.getSimpleName();

    private LRecyclerView rv_content;
    private TextView tv_back;
    private Toolbar toolbar;
    private Button bt_upload;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private VendUploadGoodsAdapter adapter;
    private List<VendUploadEntity> list;
    private int attrSize = 1;

    private LinearLayout ll_img;
    private View v_add_img;
    private EditText et_repertory;
    private int sceenW, sceenH, repertoryNum = 0;
    private int addSize = 0, addTag;
    private HashMap<Integer, View> imgMap;
    private HashMap<Integer, String> imgStrMap;
    private BottomSheetBehavior mBottomSheetBehavior;
    private PermissionListener permissionListener;

    private final int OPEN_CAMERA = 1;//相机
    private final int OPEN_ALBUM = 2;//相册
    private int cameraType;//1:相机 2：相册 3：视频
    private Uri imageUri;
    private String photoPath;

    private EditText et_title, et_price;
    private String savePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vend_upload_goods);
        initView();
    }

    private void initView() {
        savePath = Environment.getExternalStorageDirectory() + "/IMProject/";
        imgMap = new HashMap<>();
        imgStrMap = new HashMap<>();
        sceenW = UIUtils.getScreenWidth(this);
        sceenH = UIUtils.getScreenHeight(this);
        bt_upload = (Button) findViewById(R.id.bt_upload);
        tv_back = (TextView) findViewById(R.id.tv_back);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv_content = (LRecyclerView) findViewById(R.id.rv_content);
        tv_back.setOnClickListener(this);
        bt_upload.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mBottomSheetBehavior = BottomSheetBehavior.from(bt_upload);
        adapter = new VendUploadGoodsAdapter(this);
        list = new LinkedList<>();
        for (int i = 0; i < attrSize; i++) {
            VendUploadEntity entity = new VendUploadEntity();
            list.add(entity);
        }
        adapter.setDataList(list);
        adapter.setOnDelListener(new VendUploadGoodsAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                Toast.makeText(VendUploadGoodsActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                attrSize--;
                adapter.remove(pos);
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        rv_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyboardUtils.hideSoftInput(VendUploadGoodsActivity.this);
                return false;
            }
        });
        rv_content.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {
                int state = mBottomSheetBehavior.getState();
                if (state == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            @Override
            public void onScrollDown() {
                int state = mBottomSheetBehavior.getState();
                if (state == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
                boolean isBootm = rv_content.canScrollVertically(1);
                if (!isBootm) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onScrollStateChanged(int state) {

            }
        });

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rv_content.setAdapter(mLRecyclerViewAdapter);
        View headview = View.inflate(this, R.layout.head_view_vend_upload_goods, null);
        et_title = (EditText) headview.findViewById(R.id.et_title);
        mLRecyclerViewAdapter.addHeaderView(headview);
        View footdview = LayoutInflater.from(this).inflate(R.layout.foot_view_vend_upload_goods, (ViewGroup) findViewById(android.R.id.content), false);
        initFootView(footdview);
        mLRecyclerViewAdapter.addFooterView(footdview);

        rv_content.setPullRefreshEnabled(false);
        rv_content.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initFootView(View view) {
        et_price = (EditText) view.findViewById(R.id.et_price);
        et_repertory = (EditText) view.findViewById(R.id.et_repertory);
        ImageView iv_minus = (ImageView) view.findViewById(R.id.iv_minus);
        ImageView iv_add = (ImageView) view.findViewById(R.id.iv_add);
        ll_img = (LinearLayout) view.findViewById(R.id.ll_img);
        TextView tv_add_attr = (TextView) view.findViewById(R.id.tv_add_attr);
        v_add_img = View.inflate(this, R.layout.item_vend_add_img, null);
        TextView tv_add_img = (TextView) v_add_img.findViewById(R.id.tv_add_img);

        iv_minus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    updateAddOrSubtract(124);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopAddOrSubtract();    //手指抬起时停止发送
                }
                return true;
            }
        });
        iv_add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    updateAddOrSubtract(125);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopAddOrSubtract();    //手指抬起时停止发送
                }
                return true;
            }
        });
        et_repertory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    String s = editable.toString().trim();
                    if (!TextUtils.isEmpty(s)) {
                        if (s.length() > 9) {
                            ToastUtil.showShort(VendUploadGoodsActivity.this, "已超过最大可输入值");
                            et_repertory.setText("999999999");
                            repertoryNum = 999999999;
                        } else {
                            repertoryNum = Integer.parseInt(s);
                        }
                    }
                }
            }
        });
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_add_img.getLayoutParams();
        params.width = sceenW / 5;
        params.height = sceenW / 5;
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
        tv_add_attr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_add_attr:
                KeyboardUtils.hideSoftInput(VendUploadGoodsActivity.this);
                attrSize++;
                VendUploadEntity entity = new VendUploadEntity();
                list.add(entity);
                int size = attrSize + 1;
                if (size >= 0)
                    adapter.add("cesss", 0);
                handler.sendEmptyMessageDelayed(123, 500);
                break;
            case R.id.iv_close:
                int addTag = (int) v.getTag();
                addViewToContainer(false, addTag, null);
                break;
            case R.id.bt_upload:
                upLoadGoods();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 123:
                    int size = adapter.getItemCount() - 1;
                    if (size >= 0) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) rv_content.getLayoutManager();
                        layoutManager.scrollToPositionWithOffset(size, -100);
                        //layoutManager.setStackFromEnd(true);
                    }
                    break;
                case 124:
                    Log.i("num:", "" + repertoryNum);
                    if (repertoryNum > 0) {
                        repertoryNum--;
                        et_repertory.setText("" + repertoryNum);
                    }
                    break;
                case 125:
                    Log.i("num:", "" + repertoryNum);
                    repertoryNum++;
                    et_repertory.setText("" + repertoryNum);
                    break;
            }
        }
    };

    private void addViewToContainer(boolean isAdd, int tag, String path) {
        Log.i("savePath：", "" + path);
        if (isAdd) {
            View v_add_img = View.inflate(this, R.layout.item_vend_add_img_content, null);
            RelativeLayout rl_item = (RelativeLayout) v_add_img.findViewById(R.id.rl_item);
            ImageView iv_close = (ImageView) v_add_img.findViewById(R.id.iv_close);
            ImageView iv_icon = (ImageView) v_add_img.findViewById(R.id.iv_icon);

            Glide.with(VendUploadGoodsActivity.this).load(path).
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
        if (imgMap.size() > 0) {
            View img = ll_img.getChildAt(0);
            TextView tv_img = (TextView) img.findViewById(R.id.tv_img);
            tv_img.setVisibility(View.VISIBLE);
        }
    }

    private void showActionSheet() {
        ActionSheet actionSheet = new ActionSheet(VendUploadGoodsActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true);
        actionSheet
                .addSheetItem(getString(R.string.take_photos), ActionSheet.SheetItemColor.Black,
                        VendUploadGoodsActivity.this)
                .addSheetItem(getString(R.string.from_the_mobile_phone_photo_album_choice), ActionSheet.SheetItemColor.Black,
                        VendUploadGoodsActivity.this);
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
                        File file = new File(savePath, imageName);
                        imageUri = FileProvider.getUriForFile(VendUploadGoodsActivity.this, "com.atgc.cotton.fileProvider", file);//这里进行替换uri的获得方式
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
                        startActivityForResult(intent, OPEN_CAMERA);
                    }
                } else if (cameraType == OPEN_ALBUM) {
                    int size = 4 - addSize;
                    MultiImageSelector.create(VendUploadGoodsActivity.this).
                            showCamera(false).
                            count(size)
                            .multi() // 多选模式, 默认模式;
                            .start(VendUploadGoodsActivity.this, OPEN_ALBUM);
                }
            }

            @Override
            public void onRequestPermissionError() {
                ToastUtil.showShort(VendUploadGoodsActivity.this, getString(R.string.giving_camera_permissions));
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

    private ScheduledExecutorService scheduledExecutor;

    private void updateAddOrSubtract(int viewId) {
        final int vid = viewId;
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = vid;
                handler.sendMessage(msg);
            }
        }, 0, 300, TimeUnit.MILLISECONDS);    //每间隔100ms发送Message
    }

    private void stopAddOrSubtract() {
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdownNow();
            scheduledExecutor = null;
        }
    }

    private void upLoadGoods() {
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOjEwMDAwMDA5NiwiZXhwIjoxNTE0Nzk1MDIxLCJpc3MiOiJ6aGVuZ2NvZ0BnbWFpbC5jb20iLCJuYmYiOjE0OTkyNDMwMjF9.vLKKbhLF6X-fC-zhpoFoL06JZ2iBaKuDvd_xHuDMSAo";
        Map<String, Object> map = new HashMap<>();
        String title = et_title.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            ToastUtil.show(this, "标题不能为空", Toast.LENGTH_SHORT);
            return;
        }
        String repertory = et_repertory.getText().toString().trim();
        if (TextUtils.isEmpty(repertory) || repertoryNum <= 0) {
            ToastUtil.show(this, "库存不能为空", Toast.LENGTH_SHORT);
            return;
        }
        String price = et_price.getText().toString().trim();
        if (TextUtils.isEmpty(price)) {
            ToastUtil.show(this, "价格不能为空", Toast.LENGTH_SHORT);
            return;
        }
        if (imgStrMap.size() < 1) {
            ToastUtil.show(this, "请至少上传一张图片", Toast.LENGTH_SHORT);
            return;
        }
        map.put("name", title);
        map.put("number", repertory);
        map.put("price", price);
        List<VendUploadEntity> list = adapter.getDataList();
        int listSize = list.size();
        JSONArray jsonarray = new JSONArray();
        for (int i = 0; i < listSize; i++) {
            VendUploadEntity entity = list.get(i);
            if (entity != null) {
                JSONObject object = new JSONObject();
                try {
                    object.put("attr_name", entity.getTitle());
                    object.put("attr_value", entity.getContent());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonarray.add(object);
            }
        }
        map.put("attrs", jsonarray);
        int i = 0;
        for (int key : imgStrMap.keySet()) {
            String path = imgStrMap.get(key);
            if (Build.VERSION.SDK_INT >= 24 && path.contains("com.atgc.cotton.fileProvider") &&
                    path.contains("/IMProject/")) {
                String[] arr = path.split("/IMProject/");
                if (arr != null && arr.length > 1) {
                    path = savePath + arr[1];
                }
            }

            Bitmap bitmap = FileUtils.compressImageFromFile(path, sceenW);
            if (bitmap != null) {
                File file = FileUtils.compressImage(bitmap, savePath);
                if (file != null) {
                    map.put("pic" + i, file);
                    i++;
                }
            }
        }
        VendUploadGoodsRequest request = new VendUploadGoodsRequest(TAG, 0);
        request.sendFile(new BaseDataRequest.RequestCallback<String>() {

            @Override
            public void onSuccess(String pojo) {
                Log.i("savePath:", "onSuccess:" + pojo);
                if (!TextUtils.isEmpty(pojo)) {
                    BaseResult dta = BaseResult.parse(pojo);
                    if (dta != null) {
                        if (dta.getCode() == 0 && dta.getStatus() == 200) {
                            ToastUtil.showShort(VendUploadGoodsActivity.this, dta.getMessage());
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtil.showShort(VendUploadGoodsActivity.this, msg);
            }
        }, map, token);
    }
}
