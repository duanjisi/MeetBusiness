package com.atgc.cotton.activity.vendingRack;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseCompatActivity;
import com.atgc.cotton.adapter.VendUploadGoodsAdapter;
import com.atgc.cotton.entity.GoodsDetailEntity;
import com.atgc.cotton.entity.UnitEntity;
import com.atgc.cotton.entity.VendGoodsAttrEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.UnityRequest;
import com.atgc.cotton.listener.PermissionListener;
import com.atgc.cotton.presenter.VendUploadPresenter;
import com.atgc.cotton.presenter.view.IVendUploadView;
import com.atgc.cotton.util.CommonDialogUtils;
import com.atgc.cotton.util.FileUtils;
import com.atgc.cotton.util.KeyboardUtils;
import com.atgc.cotton.util.PermissonUtil.PermissionUtil;
import com.atgc.cotton.util.PhotoAlbumUtil.MultiImageSelector;
import com.atgc.cotton.util.PhotoAlbumUtil.MultiImageSelectorActivity;
import com.atgc.cotton.util.ToastUtil;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.ActionSheet;
import com.atgc.cotton.widget.GlideRoundTransform;
import com.atgc.cotton.widget.region.OnWheelChangedListener;
import com.atgc.cotton.widget.region.WheelView;
import com.atgc.cotton.widget.region.adapters.ArrayWheelAdapter;
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
public class VendUploadGoodsActivity extends BaseCompatActivity<VendUploadPresenter> implements
        View.OnClickListener, ActionSheet.OnSheetItemClickListener, IVendUploadView {
    private static final String TAG = VendUploadGoodsActivity.class.getSimpleName();
    private LRecyclerView rv_content;
    private TextView tv_back, tv_title;
    private Toolbar toolbar;
    private Button bt_upload;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private VendUploadGoodsAdapter adapter;
    private List<VendGoodsAttrEntity> list;
    private int attrSize = 1;
    private LinearLayout ll_img;
    private View v_add_img;
    private TextView tv_unit;
    private EditText et_repertory, et_price;
    private PopupWindow popupWindow;
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

    private TextView et_title;
    private Map<String, Object> parmMap;
    private List<GoodsDetailEntity.DataBean.GoodsGalleryBean> GoodsGalleryList;
    private StringBuilder stringBuilder;
    private List<String> newUploadList;
    private boolean isChange = false;
    private int upLoadimgTag = 0;
    private String goodsid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vend_upload_goods);
        initView();
    }

    @Override
    protected VendUploadPresenter createPresenter() {
        return new VendUploadPresenter(this);
    }

    private void initView() {
        imgMap = new HashMap<>();
        imgStrMap = new HashMap<>();
        sceenW = UIUtils.getScreenWidth(this);
        sceenH = UIUtils.getScreenHeight(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        bt_upload = (Button) findViewById(R.id.bt_upload);
        tv_back = (TextView) findViewById(R.id.tv_back);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv_content = (LRecyclerView) findViewById(R.id.rv_content);
        tv_back.setOnClickListener(this);
        bt_upload.setOnClickListener(this);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mBottomSheetBehavior = BottomSheetBehavior.from(bt_upload);
        adapter = new VendUploadGoodsAdapter(this);
        list = new LinkedList<>();
        for (int i = 0; i < attrSize; i++) {
            VendGoodsAttrEntity entity = new VendGoodsAttrEntity();
            list.add(entity);
        }
        adapter.setDataList(list);
        adapter.setOnDelListener(new VendUploadGoodsAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
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
                Log.i("num:", "onScrollUp    " + state);
                if (state == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            @Override
            public void onScrollDown() {
                int state = mBottomSheetBehavior.getState();
                Log.i("num:", "onScrollDown    " + state);
                if (state == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
                boolean isBottom = rv_content.canScrollVertically(1);
                if (!isBottom) {
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
        et_title = (TextView) headview.findViewById(R.id.et_title);
        mLRecyclerViewAdapter.addHeaderView(headview);
        View footdview = LayoutInflater.from(this).inflate(R.layout.foot_view_vend_upload_goods, (ViewGroup) findViewById(android.R.id.content), false);
        initFootView(footdview);
        mLRecyclerViewAdapter.addFooterView(footdview);

        rv_content.setPullRefreshEnabled(false);
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        if (intent != null) {
            goodsid = intent.getStringExtra("goodsId");
            if (!TextUtils.isEmpty(goodsid)) {
                tv_title.setText("修改商品");
                isChange = true;
                stringBuilder = new StringBuilder();
                mPresenter.getGoodsDetail(goodsid);
            }
        }
        requestUnits();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            showTipsDialog2();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    protected void showTipsDialog2() {
        String msg = "你确定退出当前页面么?";
        CommonDialogUtils.showDialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialogUtils.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialogUtils.dismiss();
                finish();
            }
        }, context, msg);
    }


    private void requestUnits() {
        UnityRequest request = new UnityRequest(TAG);
        request.send(new BaseDataRequest.RequestCallback<UnitEntity>() {
            @Override
            public void onSuccess(UnitEntity pojo) {
                if (pojo != null) {
                    mUnits = pojo.getData();
                }
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    private void initFootView(View view) {
        et_price = (EditText) view.findViewById(R.id.et_price);
        et_repertory = (EditText) view.findViewById(R.id.et_repertory);
        ImageView iv_minus = (ImageView) view.findViewById(R.id.iv_minus);
        ImageView iv_add = (ImageView) view.findViewById(R.id.iv_add);
        ll_img = (LinearLayout) view.findViewById(R.id.ll_img);
        TextView tv_add_attr = (TextView) view.findViewById(R.id.tv_add_attr);
        tv_unit = (TextView) view.findViewById(R.id.tv_unit);
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
        tv_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow == null) {
                    showRegion(getWindow().getDecorView());
                } else {
                    if (!popupWindow.isShowing()) {
                        showRegion(getWindow().getDecorView());
                    }
                }
            }
        });
        tv_add_attr.setOnClickListener(this);
    }

    private WheelView wvUnit;
    private ArrayList<String> mUnits;
    private String mCurrentUnit;

    private void showRegion(View parent) {
        View view = View.inflate(context, R.layout.dialog_unit_select, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT, false);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getDrawableFromRes(R.drawable.bg_popwindow));

        wvUnit = (WheelView) view.findViewById(R.id.id_unit);

        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView option = (TextView) view.findViewById(R.id.tv_ok);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mCurrentUnit)) {
                    tv_unit.setText(mCurrentUnit);
                }
                popupWindow.dismiss();
            }
        });
        wvUnit.setViewAdapter(new ArrayWheelAdapter(context, mUnits));
        wvUnit.addChangingListener(new wheelChangedListener());
        wvUnit.setVisibleItems(5);
        popupWindow.showAtLocation(parent, 0, 0, Gravity.END);
    }

    private class wheelChangedListener implements OnWheelChangedListener {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            mCurrentUnit = mUnits.get(newValue);
        }
    }

    private Drawable getDrawableFromRes(int resId) {
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, resId);
        return new BitmapDrawable(bmp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
//                finish();
                showTipsDialog2();
                break;
            case R.id.tv_add_attr:
                KeyboardUtils.hideSoftInput(VendUploadGoodsActivity.this);
                attrSize++;
                VendGoodsAttrEntity entity = new VendGoodsAttrEntity();
                list.add(entity);
                int size = attrSize + 1;
                if (size >= 0)
                    adapter.add();
                handler.sendEmptyMessageDelayed(123, 500);
                break;
            case R.id.iv_close:
                int addTag = (int) v.getTag();
                addViewToContainer(false, addTag, null);
                break;
            case R.id.bt_upload:
                if (parmMap == null) {
                    parmMap = new HashMap<>();
                } else {
                    parmMap.clear();
                }
                String name = et_title.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    showToast("标题不能为空", false);
                    return;
                }
                parmMap.put("name", name);
                String num = et_repertory.getText().toString().trim();
                if (!num.equals("" + repertoryNum)) {
                    parmMap.put("number", num);
                } else {
                    parmMap.put("number", String.valueOf(repertoryNum));
                }
                String price = et_price.getText().toString().trim();
                if (TextUtils.isEmpty(price)) {
                    showToast("价格不能为空", false);
                    return;
                }
                parmMap.put("price", price);

                String unit = tv_unit.getText().toString().trim();
                if (TextUtils.isEmpty(unit) || unit.equals("计量单位")) {
                    showToast("计量单位不能为空", false);
                    return;
                }
                parmMap.put("units", unit);

                List<VendGoodsAttrEntity> attrList = adapter.getDataList();
                int attrSize = attrList.size();
                if (attrSize > 0) {
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < attrSize; i++) {
                        VendGoodsAttrEntity entity1 = attrList.get(i);
                        JSONObject object = new JSONObject();
                        object.put("attr_name", entity1.getAttrName());
                        object.put("attr_value", entity1.getAttrValue());
                        jsonArray.add(object);
                    }
                    parmMap.put("attrs", jsonArray.toJSONString());
                }
                upLoadimgTag = 0;
                if (isChange) {
                    if (imgStrMap.size() == 0) {
                        showToast("至少需要一张图片", false);
                        return;
                    }
                    parmMap.put("goodsid", goodsid);
                    if (stringBuilder != null && stringBuilder.length() > 0) {
                        parmMap.put("imgids", stringBuilder.toString());
                    }
                    if (newUploadList != null && newUploadList.size() > 0) {
                        String savePath = Environment.getExternalStorageDirectory() + "/IMProject/";
                        for (int i = 0; i < newUploadList.size(); i++) {
                            String path = newUploadList.get(i);
                            addImgToParms(path, savePath);
                        }
                    }
                } else {
                    int imgSize = imgStrMap.size();
                    if (imgSize > 0) {
                        String savePath = Environment.getExternalStorageDirectory() + "/IMProject/";
                        for (Map.Entry<Integer, String> entry : imgStrMap.entrySet()) {
                            String path = entry.getValue();
                            addImgToParms(path, savePath);
                        }
                    } else {
                        showToast("至少需要一张图片", false);
                        return;
                    }
                }
                if (isChange) {
                    mPresenter.changeMyGoods(parmMap, Integer.parseInt(goodsid));
                } else {
                    mPresenter.uploadGoods(parmMap);
                }
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
                    String num = et_repertory.getText().toString().trim();
                    repertoryNum = Integer.parseInt(num);
                    if (repertoryNum > 0) {
                        repertoryNum--;
                        et_repertory.setText("" + repertoryNum);
                    }
                    break;
                case 125:
                    String num1 = et_repertory.getText().toString().trim();
                    repertoryNum = Integer.parseInt(num1);
                    repertoryNum++;
                    et_repertory.setText("" + repertoryNum);
                    break;
            }
        }
    };

    private void addViewToContainer(boolean isAdd, int tag, String path) {
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
            if (GoodsGalleryList != null && GoodsGalleryList.size() > tag) {
                GoodsDetailEntity.DataBean.GoodsGalleryBean item = GoodsGalleryList.get(tag);
                if (item != null) {
                    stringBuilder.append(item.getImgId() + "|");
                }
            }
            ll_img.removeView(imgMap.get(tag));
            imgMap.remove(tag);
            imgStrMap.remove(tag);
            addSize--;
        }

        View add_img = ll_img.getChildAt(ll_img.getChildCount() - 1);
        if (addSize == 8) {
            UIUtils.hindView(add_img);
        } else if (addSize < 8) {
            UIUtils.showView(add_img);
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
                        String savePath = Environment.getExternalStorageDirectory() + "/IMProject/";
                        File file = new File(savePath, imageName);
                        imageUri = FileProvider.getUriForFile(VendUploadGoodsActivity.this, "com.atgc.cotton.fileProvider", file);//这里进行替换uri的获得方式
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
                        startActivityForResult(intent, OPEN_CAMERA);
                    }
                } else if (cameraType == OPEN_ALBUM) {
                    int size = 8 - addSize;
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
        if (isChange && newUploadList == null) {
            newUploadList = new ArrayList<>();
        }
        if (requestCode == OPEN_CAMERA && resultCode == RESULT_OK) {//打开相机
            if (imageUri != null) {
                String path = null;
                if (Build.VERSION.SDK_INT < 24) {
                    path = FileUtils.getPath(this, imageUri);
                } else {
                    path = imageUri.toString();
                }
                if (isChange) {
                    newUploadList.add(path);
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
                    if (isChange) {
                        newUploadList.add(path);
                    }
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
        }, 0, 150, TimeUnit.MILLISECONDS);    //每间隔100ms发送Message
    }

    private void stopAddOrSubtract() {
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdownNow();
            scheduledExecutor = null;
        }
    }

    private void addImgToParms(String path, String savePath) {
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
                parmMap.put("pic" + upLoadimgTag, file);
                upLoadimgTag++;
            }
        }
    }

    @Override
    public void upLoadSccess() {
        showToast("上传成功", false);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void getGoodsDetail(GoodsDetailEntity.DataBean dataBean) {
        et_title.setText(dataBean.getGoodsName());
        et_price.setText("" + dataBean.getGoodsPrice());
        repertoryNum = dataBean.getGoodsNumber();
        et_repertory.setText("" + dataBean.getGoodsNumber());
        List<VendGoodsAttrEntity> GoodsAttr = dataBean.getGoodsAttr();
        attrSize = GoodsAttr.size();
        adapter.setDataList(GoodsAttr);
        GoodsGalleryList = dataBean.getGoodsGallery();
        if (GoodsGalleryList != null && GoodsGalleryList.size() > 0) {
            for (int i = 0; i < GoodsGalleryList.size(); i++) {
                GoodsDetailEntity.DataBean.GoodsGalleryBean item = GoodsGalleryList.get(i);
                String url = item.getImgUrl();
                addTag = i;
                imgStrMap.put(addTag, url);
                addViewToContainer(true, 0, url);
            }
        }

    }

    @Override
    public void changeGoodsSuccess() {
        showToast("修改成功", false);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onError(String msg) {
        showToast(msg, false);
    }
}
