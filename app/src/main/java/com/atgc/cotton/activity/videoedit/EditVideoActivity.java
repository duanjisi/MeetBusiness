package com.atgc.cotton.activity.videoedit;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.adapter.FilterAdapter;
import com.atgc.cotton.adapter.VideoThumbAdapter;
import com.atgc.cotton.entity.ActionEntity;
import com.atgc.cotton.entity.FilterEntity;
import com.atgc.cotton.listener.PermissionListener;
import com.atgc.cotton.photoedit.TextObject;
import com.atgc.cotton.util.FileUtils;
import com.atgc.cotton.util.PermissonUtil.PermissionUtil;
import com.atgc.cotton.util.ToastUtil;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.videorange.VideoThumbnailInfo;
import com.atgc.cotton.videorange.VideoThumbnailTask;
import com.atgc.cotton.widget.Sticker.StickerView;
import com.czt.mp3recorder.MP3Recorder;
import com.ksyun.media.shortvideo.kit.KSYEditKit;
import com.ksyun.media.shortvideo.utils.ShortVideoConstants;
import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautyToneCurveFilter;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.StreamerConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Johnny on 2017/6/26.
 */
public class EditVideoActivity extends BaseActivity implements View.OnClickListener, StickerView.OnStickerTouchListener {
    private int[] bubbles = {R.drawable.bubble_00, R.drawable.bubble_01,
            R.drawable.bubble_02, R.drawable.bubble_03,
            R.drawable.bubble_04, R.drawable.bubble_05,
            R.drawable.bubble_06, R.drawable.bubble_07,
            R.drawable.bubble_08, R.drawable.bubble_09,
            R.drawable.bubble_10, R.drawable.bubble_11};

    public final static int FRAME_RATE = 20;
    public final static int VIDEO_BITRATE = 1000;
    public final static int AUDIO_BITRATE = 64;
    public final static int VIDEO_RESOLUTION = StreamerConstants.VIDEO_RESOLUTION_480P;
    public final static int ENCODE_TYPE = AVConst.CODEC_ID_HEVC;
    public final static int ENCODE_METHOD = StreamerConstants.ENCODE_METHOD_SOFTWARE;
    public final static int ENCODE_PROFILE = VideoEncodeFormat.ENCODE_PROFILE_BALANCE;

    private HashMap<Integer, int[]> maps = new HashMap<>();
    private GLSurfaceView mEditPreviewView;
    private KSYEditKit mEditKit;
    private StickerView stickerView;
    private AppCompatSeekBar mOriginAudioVolumeSeekBar;
    private AppCompatSeekBar mBgmVolumeSeekBar;
    private SeekBarChangedObserver mSeekBarChangedObsesrver;
    private static final int LONG_VIDEO_MAX_LEN = 300000;
    private int mMaxClipSpanMs = LONG_VIDEO_MAX_LEN;  //默认的最大裁剪时长

    private RadioGroup mRadioGroup;
    private View vFilter, vSound, vTailor;
    private ImageView ivClose;
    private TextView tvNext, tvSwitcher, tvRecord, tvNative, tvMore;
    private RelativeLayout content_layout;
    private ImageView iv_video_bg;
    private TextView tvContent, tv00, tv01, tv02;
    private PopupWindow popupWindow;
    private String url;
    //    private OperateView operateView;
    //    private OperateUtils operateUtils;
//    private Bitmap resizeBmp = null;
//    final Handler myHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 1) {
//                if (content_layout.getWidth() != 0) {
//                    fillContent();
//                    addFont();
//                }
//            }
//        }
//    };
    //    private Gallery gallery;
    private LinearLayout linearLayout, ll_bubble;
    private VideoThumbAdapter mVideoThumbnailAdapter;

    private RecyclerView rv_filter;

    public final static String SRC_URL = "srcurl";

    private List<FilterEntity> datas;

    private ImgBeautyToneCurveFilter acvFilter;

    private boolean isOriginalVoice = false;
    private BottomSheetDialog sheetDialog;
    private boolean mIsRecord = false;
    private MP3Recorder mRecorder;
    private int nowTime, allTime = 8;
    private ProgressBar pb_progress_bar;
    private ImageView iv_record;
    private boolean isTouch;
    private String filePath;
    private PermissionListener permissionListener;


    public static void startActivity(Context context, String srcurl) {
        Intent intent = new Intent(context, EditVideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SRC_URL, srcurl);
        context.startActivity(intent);
    }

//    private void fillContent() {
//        if (resizeBmp != null) {
//            if (content_layout.getChildCount() != 0) {
//                content_layout.removeAllViews();
//            }
//            operateView = new OperateView(context, resizeBmp);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                    resizeBmp.getWidth(), resizeBmp.getHeight());
//            operateView.setLayoutParams(layoutParams);
//            content_layout.addView(operateView);
//            operateView.setMultiAdd(false); //设置此参数，可以添加多个文字
//        }
//    }
//
//    private void addFont() {
//        if (operateView != null) {
//            TextObject textObj = operateUtils.getTextObject("添加文字",
//                    operateView, 5, 150, 150);
//            if (textObj != null) {
//                textObj.setTextSize(22);
//                textObj.commit();
//                operateView.addItem(textObj);
//                operateView.setOnListener(new OperateView.MyListener() {
//                    public void onClick(TextObject tObject) {
//                        alert(tObject);
//                    }
//                });
//            }
////            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_normal_2);
////            ImageObject imgObject = operateUtils.getImageObject(bmp, operateView,
////                    5, 150, 100);
////            operateView.addItem(imgObject);
//        }
//    }

    private void alert(final TextObject tObject) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);
        EventBus.getDefault().register(this);
        initViews();
    }

    @Subscribe
    public void onMessageEvent(ActionEntity event) {
        if (event != null) {
            String action = event.getAction();
            if (action.equals(Constants.Action.EXIT_CURRENT_ACTIVITY)) {
                finish();
            }
        }
    }

    private void initViews() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvSwitcher = (TextView) findViewById(R.id.tv_switcher_voice);
        tvRecord = (TextView) findViewById(R.id.tv_record);
        tvNative = (TextView) findViewById(R.id.tv_native);
        tvMore = (TextView) findViewById(R.id.tv_more);
//        gallery = (Gallery) findViewById(R.id.gallery);
        linearLayout = (LinearLayout) findViewById(R.id.container);
        ll_bubble = (LinearLayout) findViewById(R.id.ll_bubble);
        mEditPreviewView = (GLSurfaceView) findViewById(R.id.edit_preview);

        ivClose.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        tvSwitcher.setOnClickListener(this);
        tvRecord.setOnClickListener(this);
        tvNative.setOnClickListener(this);
        tvMore.setOnClickListener(this);
        initEditKit();

        vFilter = findViewById(R.id.edit_video_filter);
        vSound = findViewById(R.id.edit_video_sound);
        vTailor = findViewById(R.id.edit_video_tailor);

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(new CheckListener());
        mSeekBarChangedObsesrver = new SeekBarChangedObserver();
        mOriginAudioVolumeSeekBar = (AppCompatSeekBar) findViewById(R.id.origin_audio_volume);
        mOriginAudioVolumeSeekBar.setOnSeekBarChangeListener(mSeekBarChangedObsesrver);
        mBgmVolumeSeekBar = (AppCompatSeekBar) findViewById(R.id.music_audio_volume);
        mBgmVolumeSeekBar.setOnSeekBarChangeListener(mSeekBarChangedObsesrver);
        content_layout = (RelativeLayout) findViewById(R.id.mainLayout);
        iv_video_bg = (ImageView) findViewById(R.id.iv_video_bg);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tv00 = (TextView) findViewById(R.id.tv00);
        tv01 = (TextView) findViewById(R.id.tv01);
        tv02 = (TextView) findViewById(R.id.tv02);
        tvContent.setOnTouchListener(new touchListener());
        tv00.setOnClickListener(this);
        tv01.setOnClickListener(this);
        tv02.setOnClickListener(this);
//        operateUtils = new OperateUtils(this);
        startEditPreview();
//        addStikerTextView();
        initBuble();

        //先显示滤镜
        UIUtils.showView(vFilter);
        UIUtils.hindView(vSound);

        rv_filter = (RecyclerView) findViewById(R.id.rv_filter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_filter.setLayoutManager(manager);

        initData();
        FilterAdapter adapter = new FilterAdapter(this);
        adapter.setDataList(datas);
        rv_filter.setAdapter(adapter);
        adapter.setItemClickListener(new FilterAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                Log.i("liwya", "setItemClickListener" + postion);
                switch (postion) {
                    case 0:
                        break;
                    case 1:
                        acvFilter = new ImgBeautyToneCurveFilter(mEditKit
                                .getGLRender());
                        acvFilter.setFromCurveFileInputStream(
                                EditVideoActivity.this.getResources().openRawResource(R.raw.bohe));
                        mEditKit.getImgTexFilterMgt().setFilter(acvFilter);
                        break;
                    case 2:
                        acvFilter = new ImgBeautyToneCurveFilter(mEditKit
                                .getGLRender());
                        acvFilter.setFromCurveFileInputStream(
                                EditVideoActivity.this.getResources().openRawResource(R.raw.fugu));
                        mEditKit.getImgTexFilterMgt().setFilter(acvFilter);
                        break;
                    case 3:
                        acvFilter = new ImgBeautyToneCurveFilter(mEditKit
                                .getGLRender());
                        acvFilter.setFromCurveFileInputStream(
                                EditVideoActivity.this.getResources().openRawResource(R.raw.jiaopian));
                        mEditKit.getImgTexFilterMgt().setFilter(acvFilter);
                        break;
                    case 4:
                        acvFilter = new ImgBeautyToneCurveFilter(mEditKit
                                .getGLRender());
                        acvFilter.setFromCurveFileInputStream(
                                EditVideoActivity.this.getResources().openRawResource(R.raw.langman));
                        mEditKit.getImgTexFilterMgt().setFilter(acvFilter);
                        break;
                    case 5:
                        acvFilter = new ImgBeautyToneCurveFilter(mEditKit
                                .getGLRender());
                        acvFilter.setFromCurveFileInputStream(
                                EditVideoActivity.this.getResources().openRawResource(R.raw.mihuan));
                        mEditKit.getImgTexFilterMgt().setFilter(acvFilter);
                        break;
                    case 6:
                        acvFilter = new ImgBeautyToneCurveFilter(mEditKit
                                .getGLRender());
                        acvFilter.setFromCurveFileInputStream(
                                EditVideoActivity.this.getResources().openRawResource(R.raw.nianhua));
                        mEditKit.getImgTexFilterMgt().setFilter(acvFilter);
                        break;
                    case 7:
                        acvFilter = new ImgBeautyToneCurveFilter(mEditKit
                                .getGLRender());
                        acvFilter.setFromCurveFileInputStream(
                                EditVideoActivity.this.getResources().openRawResource(R.raw.s1874));
                        mEditKit.getImgTexFilterMgt().setFilter(acvFilter);
                        break;
                    case 8:
                        acvFilter = new ImgBeautyToneCurveFilter(mEditKit
                                .getGLRender());
                        acvFilter.setFromCurveFileInputStream(
                                EditVideoActivity.this.getResources().openRawResource(R.raw.yinxiang));
                        mEditKit.getImgTexFilterMgt().setFilter(acvFilter);
                        break;
                    case 9:
                        acvFilter = new ImgBeautyToneCurveFilter(mEditKit
                                .getGLRender());
                        acvFilter.setFromCurveFileInputStream(
                                EditVideoActivity.this.getResources().openRawResource(R.raw.yinyue));
                        mEditKit.getImgTexFilterMgt().setFilter(acvFilter);
                        break;


                }
            }
        });


    }

    private void initData() {
        datas = new ArrayList<>();
        datas.add(new FilterEntity(R.drawable.wu, "无"));
        datas.add(new FilterEntity(R.drawable.bohe, "薄荷"));
        datas.add(new FilterEntity(R.drawable.fugu, "复古"));
        datas.add(new FilterEntity(R.drawable.jiaopian, "胶片"));
        datas.add(new FilterEntity(R.drawable.langman, "浪漫"));
        datas.add(new FilterEntity(R.drawable.mihuan, "迷幻"));
        datas.add(new FilterEntity(R.drawable.nianhua, "年华"));
        datas.add(new FilterEntity(R.drawable.s1874, "1874"));
        datas.add(new FilterEntity(R.drawable.yinxiang, "印象"));
        datas.add(new FilterEntity(R.drawable.yinyue, "银月"));
    }

    private void initEditKit() {
        mEditKit = new KSYEditKit(this);
        mEditKit.setDisplayPreview(mEditPreviewView);
        mEditKit.setOnErrorListener(mOnErrorListener);
        mEditKit.setOnInfoListener(mOnInfoListener);
//        mEditKit.addStickerView(mKSYStickerView);
//        mEditKit.addTextStickerView(mTextView);

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString(SRC_URL);
        if (!TextUtils.isEmpty(url)) {
            mEditKit.setEditPreviewUrl(url);
        }
    }

    private void startEditPreview() {
        //设置预览的音量
        mEditKit.setVolume(0.4f);
        //设置是否循环预览
        mEditKit.setLooping(true);
        //开启预览
        mEditKit.startEditPreview();
        mOriginAudioVolumeSeekBar.setProgress((int) (mEditKit.getOriginAudioVolume() * 100));
    }

    private void intiVideoThumb(final Bitmap bitmap) {
        if (bitmap != null) {
            iv_video_bg.setImageBitmap(bitmap);
        }
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    resizeBmp = operateUtils.compressionFiller(bitmap,
//                            content_layout);
//                    Log.i("info", "==========resizeBmp:" + resizeBmp);
//                    if (resizeBmp != null) {
//                        Message message = new Message();
//                        message.what = 1;
//                        myHandler.sendMessage(message);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    private int lastY;
    private final long doubleClickTimeLimit = 200;
    private long preClicktime;

    private class touchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int ea = event.getAction();
//            final int screenWidth = dm.widthPixels;
//            final int screenHeight = dm.heightPixels;
            switch (v.getId()) {
                case R.id.tv_content:
                    switch (ea) {
                        case MotionEvent.ACTION_DOWN:
//                            lastX = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
                            lastY = (int) event.getRawY();
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - preClicktime > doubleClickTimeLimit) {
                                preClicktime = currentTime;
                            } else {
                                showBgFram();
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int dx = (int) event.getRawX();
                            int dy = (int) event.getRawY() - lastY;
                            int l = v.getLeft();
                            int b = v.getBottom() + dy;
                            int r = v.getRight();
                            int t = v.getTop() + dy;
                            // 下面判断移动是否超出屏幕
                            if (l < 0) {
                                l = 0;
                                r = l + v.getWidth();
                            }
                            if (t < 0) {
                                t = 0;
                                b = t + v.getHeight();
                            }
//                            if (r > screenWidth) {
//                                r = screenWidth;
//                                l = r - v.getWidth();
//                            }
//                            if (b > screenHeight) {
//                                b = screenHeight;
//                                t = b - v.getHeight();
//                            }
                            v.layout(l, t, r, b);
                            lastY = (int) event.getRawY();
                            v.postInvalidate();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        default:
                            break;
                    }
                default:
                    break;
            }
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_switcher_voice://原音开
                isOriginalVoice = isOriginalVoice ? false : true;
                Log.i("isOriginalVoice:", "" + isOriginalVoice);
                onOriginAudioClick(isOriginalVoice);
                break;
            case R.id.tv_record://录音
                getPermission();
                break;
            case R.id.tv_native://本地
                openActvityForResult(LocalMusicActivity.class, 101);
                break;
            case R.id.tv_more://更多
                if (ll_bubble.getVisibility() != View.VISIBLE) {
                    ll_bubble.setVisibility(View.VISIBLE);
                    tvMore.setVisibility(View.GONE);
                } else {
                    ll_bubble.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_next:
                onNextClick();
                break;
            case R.id.iv_record_colse:
//                resolveStopRecord();
                break;
            case R.id.iv_record_ok:
//                resolveStopRecord();
                nowTime = 0;
                pb_progress_bar.setProgress(0);
                resolveStopRecord();
                if (!TextUtils.isEmpty(filePath))
                    mEditKit.changeBgmMusic(filePath);
                break;
            case R.id.tv00:
                tvContent.setBackgroundColor(getResources().getColor(R.color.bg_color00));
                showBgFram();
                break;
            case R.id.tv01:
                tvContent.setBackgroundColor(getResources().getColor(R.color.bg_color01));
                showBgFram();
                break;
            case R.id.tv02:
                tvContent.setBackgroundColor(getResources().getColor(R.color.bg_color02));
                showBgFram();
                break;
        }
    }

    private String img_path;
    private String composeUrl;
    private boolean mComposeFinished = false;
    private ComposeAlertDialog mComposeAlertDialog;

    private void onNextClick() {
        if (stickerView != null) {
            stickerView.setShowDrawController(false);
        }
        Bitmap bmp = FileUtils.getBitmapByView(content_layout);
        if (bmp != null) {
            String str = FileUtils.getFileNameFromPath(url);
            String fileName = str.substring(0, str.indexOf("."));
            img_path = FileUtils.saveBitmap(bmp, fileName + ".png");
        } else {
            getBitmapsFromVideo();
        }
//        showComposingDialog();
        mEditKit.setTargetResolution(VIDEO_RESOLUTION);
        mEditKit.setVideoFps(FRAME_RATE);
        mEditKit.setVideoCodecId(ENCODE_TYPE);
        mEditKit.setVideoEncodeProfile(ENCODE_PROFILE);
        mEditKit.setAudioKBitrate(AUDIO_BITRATE);
        mEditKit.setVideoKBitrate(VIDEO_BITRATE);
        //关闭上一次合成窗口
        if (mComposeAlertDialog != null) {
            mComposeAlertDialog.closeDialog();
        }

        mComposeAlertDialog = new ComposeAlertDialog(context, R.style.dialog);
        //设置合成路径
        String fileFolder = "/sdcard/MeetBus";
        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdir();
        }
        composeUrl = fileFolder + "/" + System.currentTimeMillis() + ".mp4";
        //开始合成
        mEditKit.startCompose(composeUrl);
    }


    private class ComposeAlertDialog extends AlertDialog {
        private RelativeLayout mProgressLayout;
        private ProgressBar mComposeProgess;
        private TextView mStateTextView;
        private TextView mProgressText;

        private int mScreenWidth;
        private int mScreenHeight;
        private String mFilePath = null;
        public boolean mNeedResumePlay = false;
        private AlertDialog mConfimDialog;

        private Timer mTimer;

        protected ComposeAlertDialog(Context context, int themeResId) {
            super(context, themeResId);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Display display = getWindowManager().getDefaultDisplay();
            mScreenWidth = display.getWidth();
            mScreenHeight = display.getHeight();
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(mScreenWidth, mScreenHeight);
            LayoutInflater inflater = LayoutInflater.from(context);
            View viewDialog = inflater.inflate(R.layout.compose_layout, null);
            setContentView(viewDialog, layoutParams);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mProgressLayout = (RelativeLayout) findViewById(R.id.compose_root);
            mComposeProgess = (ProgressBar) findViewById(R.id.state_progress);
            mProgressText = (TextView) findViewById(R.id.progress_text);
            mStateTextView = (TextView) findViewById(R.id.state_text);
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (!mComposeFinished) {
                        mConfimDialog = new AlertDialog.Builder(context).setCancelable
                                (true)
                                .setTitle("中止合成?")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        mConfimDialog = null;
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        if (!mComposeFinished) {
                                            mEditKit.stopCompose();
                                            mComposeFinished = false;
                                            closeDialog();
                                            resumeEditPreview();
                                        }
                                        mConfimDialog = null;
                                    }
                                }).show();
                    } else {
                        closeDialog();
                        resumeEditPreview();
                    }
                    break;
                default:
                    break;
            }
            return false;
        }

        public void uploadProgress(double progress) {
            updateProgress((int) progress);
        }

        private void updateProgress(final int progress) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgressText.getVisibility() == View.VISIBLE) {
                        mProgressText.setText(String.valueOf(progress) + "%");
                    }
                }
            });

        }

        public void closeDialog() {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
            ComposeAlertDialog.this.dismiss();
        }

        public void composeStarted() {
            mStateTextView.setVisibility(View.VISIBLE);
            mStateTextView.setText(R.string.compose_file);
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    final int progress = mEditKit.getProgress();
                    updateProgress(progress);
                }
            }, 500, 500);
        }

        public void composeFinished(String path) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mConfimDialog != null) {
                        mConfimDialog.dismiss();
                        mConfimDialog = null;
                    }
//                    mStateTextView.setText("上传鉴权中");
                }
            });
            mFilePath = path;
//            startPreview();
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
        }
    }

    private void resumeEditPreview() {
        mEditKit.resumeEditPreview();
    }

    private void showBgFram() {
        if (content_layout.getChildCount() == 2) {
            content_layout.removeView(stickerView);
        }
        UIUtils.showView(tvContent);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            showPopChat(context, getWindow().getDecorView(), true);
        }
    }

    private class SeekBarChangedObserver implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }
            float val = progress / 100.f;
            switch (seekBar.getId()) {
                case R.id.origin_audio_volume:
                    mEditKit.setOriginAudioVolume(val);
                    break;
                case R.id.music_audio_volume:
                    mEditKit.setBgmMusicVolume(val);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private class CheckListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup arg0, int arg1) {
            switch (arg1) {
                case R.id.rb_yj:
                    mEditKit.resumeEditPreview();
                    UIUtils.showView(mEditPreviewView);
                    UIUtils.showView(vFilter);
                    UIUtils.hindView(vSound);
                    UIUtils.hindView(vTailor);
                    break;
                case R.id.rb_sound:
                    mEditKit.resumeEditPreview();
                    UIUtils.showView(mEditPreviewView);
                    UIUtils.showView(vSound);
                    UIUtils.hindView(vFilter);
                    UIUtils.hindView(vTailor);
                    break;
                case R.id.rb_cover:
                    mEditKit.pauseEditPreview();
                    UIUtils.showView(vTailor);
                    UIUtils.hindView(mEditPreviewView);
                    UIUtils.hindView(vSound);
                    UIUtils.hindView(vFilter);
                    break;
                default:
                    break;
            }
        }
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
        EventBus.getDefault().unregister(this);
        if (mComposeAlertDialog != null) {
            mComposeAlertDialog.closeDialog();
            mComposeAlertDialog = null;
        }
        mEditKit.stopEditPreview();
        mEditKit.release();
    }

    private void onOriginAudioClick(boolean isCheck) {
        if (!isCheck) {
            tvSwitcher.setText("原音开");
        } else {
            tvSwitcher.setText("原音关");
        }
        //是否删除原始音频
        mEditKit.enableOriginAudio(isCheck);
        boolean isEnable = isCheck ? false : true;
        mOriginAudioVolumeSeekBar.setEnabled(isEnable);
    }

    private void showBottomView() {
        if (sheetDialog == null) {
            sheetDialog = new BottomSheetDialog(this);
            View view = LayoutInflater.from(this).inflate(R.layout.view_bottom_record, null);
            view.findViewById(R.id.iv_record_colse).setOnClickListener(this);
            view.findViewById(R.id.iv_record_ok).setOnClickListener(this);
            pb_progress_bar = (ProgressBar) view.findViewById(R.id.pb_progress_bar);
            iv_record = (ImageView) view.findViewById(R.id.iv_record);
            iv_record.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            isTouch = true;
                            iv_record.setSelected(true);
                            Message message = handler.obtainMessage(1);
                            handler.sendMessageDelayed(message, 1000);
                            if (mIsRecord) {
//                                resolvePause();
                            } else {
//                                resolveRecord();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            isTouch = false;
                            iv_record.setSelected(false);
//                            resolvePause();
                            resolvePause();
                            Log.i("nowTime:", "ACTION_DOWN:" + "filepath:" + filePath);
                            break;
                    }
                    return true;
                }
            });
            sheetDialog.setContentView(view);
            View view1 = sheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
            final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view1);
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        sheetDialog.dismiss();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
        }
        sheetDialog.show();
    }

    /**
     * 开始录音
     */
//    private void resolveRecord() {
//        filePath = FileUtils.getAppPath();
//        File file = new File(filePath);
//        if (!file.exists()) {
//            if (!file.mkdirs()) {
//                Toast.makeText(this, "创建文件失败", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//        filePath = FileUtils.getAppPath() + UUID.randomUUID().toString() + ".mp3";
//        mRecorder = new MP3Recorder(new File(filePath));
//        mRecorder.setErrorHandler(new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if (msg.what == MP3Recorder.ERROR_TYPE) {
//                    Toast.makeText(EditVideoActivity.this, "没有麦克风权限", Toast.LENGTH_SHORT).show();
//                    resolveError();
//                }
//            }
//        });
//        try {
//            mRecorder.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(EditVideoActivity.this, "录音出现异常", Toast.LENGTH_SHORT).show();
//            resolveError();
//            return;
//        }
//        mIsRecord = true;
//    }
//

    /**
     * 停止录音
     */
    private void resolveStopRecord() {
        if (sheetDialog != null && sheetDialog.isShowing()) {
            sheetDialog.dismiss();
        }
        if (mRecorder != null && mRecorder.isRecording()) {
            mRecorder.setPause(false);
            mRecorder.stop();
        }
        mIsRecord = false;
    }

    //录音异常
    private void resolveError() {
        FileUtils.deleteFile(filePath);
        filePath = "";
        if (mRecorder != null && mRecorder.isRecording()) {
            mRecorder.stop();
        }
    }

    /**
     * 暂停
     */
    private void resolvePause() {
        if (!mIsRecord)
            return;
        if (mRecorder.isPause()) {
            mRecorder.setPause(false);
        } else {
            mRecorder.setPause(true);
        }
    }

    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {         // handle message
            switch (msg.what) {
                case 1:
                    if (nowTime >= allTime) {
//                        resolveStopRecord();
                        if (!TextUtils.isEmpty(filePath))
                            mEditKit.changeBgmMusic(filePath);
                        nowTime = 0;
                        pb_progress_bar.setProgress(0);
                        Log.i("nowTime:", "filepath" + filePath);
                    } else {
                        if (isTouch) {
                            Message message = handler.obtainMessage(1);
                            handler.sendMessageDelayed(message, 1000);
                            nowTime++;
                        }
                    }
                    int pen = nowTime * 100 / allTime;
                    pb_progress_bar.setProgress(pen);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            filePath = data.getStringExtra("filePath");
            mEditKit.changeBgmMusic(filePath);
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
                showBottomView();
            }

            @Override
            public void onRequestPermissionError() {
                ToastUtil.showShort(EditVideoActivity.this, getString(R.string.giving_record_permissions));
            }
        };
        PermissionUtil
                .with(this)
                .permissions(
                        PermissionUtil.PERMISSIONS_GROUP_RECORD_AUDIO //相机权限
                ).request(permissionListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, permissionListener);
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
//                    Log.d(TAG, "compose failed:" + type);
//                    Toast.makeText(EditActivity.this,
//                            "Compose Failed:" + type, Toast.LENGTH_LONG).show();
//                    if (mComposeAlertDialog != null) {
//                        mComposeAlertDialog.closeDialog();
//                        resumeEditPreview();
//                    }
                    break;
                case ShortVideoConstants.SHORTVIDEO_ERROR_SDK_AUTHFAILED:
//                    Log.d(TAG, "sdk auth failed:" + type);
//                    Toast.makeText(EditActivity.this,
//                            "Auth failed can't start compose:" + type, Toast.LENGTH_LONG).show();
                    break;
                case ShortVideoConstants.SHORTVIDEO_ERROR_UPLOAD_KS3_TOKEN_ERROR:
//                    Log.d(TAG, "ks3 upload token error, upload to ks3 failed");
//                    Toast.makeText(EditActivity.this,
//                            "Auth failed can't start upload:" + type, Toast.LENGTH_LONG).show();
//                    if (mComposeAlertDialog != null) {
//                        mComposeAlertDialog.uploadFinished(false);
//                    }
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
//                    mEditPreviewDuration = mEditKit.getEditDuration();
//                    initSeekBar();
                    initThumbnailAdapter();
                    break;
                case ShortVideoConstants.SHORTVIDEO_COMPOSE_START: {
                    mEditKit.pauseEditPreview();
//                    mBeautySpinner.setSelection(0);
                    if (mComposeAlertDialog != null) {
                        mComposeAlertDialog.setCancelable(false);
                        mComposeAlertDialog.show();
                        mComposeAlertDialog.composeStarted();
                    }
                    return null;
                }
                case ShortVideoConstants.SHORTVIDEO_COMPOSE_FINISHED: {
//                    mAudioReverbSpinner.setSelection(0);
//                    mAudioEffectSpinner.setSelection(0);
                    if (mComposeAlertDialog != null) {
                        Log.i("info", "===================msgs[0]:" + msgs[0]);
                        Log.i("info", "===================imPath:" + img_path);
                        mComposeAlertDialog.composeFinished(msgs[0]);
                        mComposeFinished = true;
                        mComposeAlertDialog.closeDialog();
                        showToast("合成视频完成!", true);
                        Intent intent = new Intent(context, ShareVideoActivity.class);
                        intent.putExtra("BgPath", img_path);
                        intent.putExtra("videoPath", msgs[0]);
                        startActivity(intent);
                    }
                }
                case ShortVideoConstants.SHORTVIDEO_COMPOSE_ABORTED:
                    break;
                case ShortVideoConstants.SHORTVIDEO_GET_KS3AUTH: {
                    if (msgs.length == 6) {
//                        if (mTokenTask == null) {
//                            mTokenTask = new KS3TokenTask(getApplicationContext());
//                        }
//                        return mTokenTask.requsetTokenToAppServer(msgs[0], msgs[1],
//                                msgs[2], msgs[3], msgs[4], msgs[5]);
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

    /**
     * init video thumbnail
     */
    private void initThumbnailAdapter() {
        float picWidth;  //每个thumbnail显示的宽度
//        if (mVideoRangeSeekBar == null) {
//        picWidth = 60;
//        } else {
//            picWidth = mVideoRangeSeekBar.getFrameWidth();
//        }
        picWidth = UIUtils.px2dip(this, 120);
        long durationMS = mEditKit.getEditDuration();
        //list区域需要显示的item个数
        int totalFrame;
        //比最大裁剪时长大的视频,每长mMaxClipSpanMs长度,则增加8个thumbnail
        //比最大裁剪时长小的视频,最多显示8个thumbnail
        if (durationMS > mMaxClipSpanMs) {
            totalFrame = (int) (durationMS * 8) / mMaxClipSpanMs;
        } else {
            totalFrame = 10;
        }
        Log.i("info", "==============durationMS:" + durationMS + "\n" +
                "totalFrame" + totalFrame);
        int mm = totalFrame;
        VideoThumbnailInfo[] listData = new VideoThumbnailInfo[totalFrame];
        for (int i = 0; i < totalFrame; i++) {
            listData[i] = new VideoThumbnailInfo();
            if (durationMS > mMaxClipSpanMs) {
                listData[i].mCurrentTime = i * ((float) durationMS / 1000) * (1.0f / mm);
            } else {
                if (i > 0 && i < 9) {
                    listData[i].mCurrentTime = (i - 1) * ((float) durationMS / 1000) * (1.0f / 8);
                }
            }
//            if (i == 0 && mVideoRangeSeekBar != null) {
//                listData[i].mType = VideoThumbnailInfo.TYPE_START;
//                listData[i].mWidth = (int) mVideoRangeSeekBar.getMaskWidth();
//            } else if (i == totalFrame - 1 && mVideoRangeSeekBar != null) {
//                listData[i].mType = VideoThumbnailInfo.TYPE_END;
//                listData[i].mWidth = (int) mVideoRangeSeekBar.getMaskWidth();
//            } else {
            listData[i].mType = VideoThumbnailInfo.TYPE_NORMAL;
            listData[i].mWidth = (int) picWidth;
//            }
        }
//        mVideoThumbnailAdapter = new VideoThumbAdapter(this, listData, mEditKit);
//        gallery.setAdapter(mVideoThumbnailAdapter);
//        gallery.setOnItemClickListener(new ItemClickListener());
        if (listData != null && listData.length != 0) {
            initDatas(listData);
        }
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            VideoThumbnailInfo info = (VideoThumbnailInfo) adapterView.getItemAtPosition(i);
            if (info != null) {
                Bitmap bitmap = info.mBitmap;
                if (bitmap != null) {
                    intiVideoThumb(bitmap);
                }
            }
        }
    }


    private void initDatas(VideoThumbnailInfo[] listData) {
        VideoThumbnailInfo data = listData[0];
        if (data != null) {
            VideoThumbnailTask.loadBitmap(this, iv_video_bg,
                    null, (long) (data.mCurrentTime * 1000), data,
                    mEditKit, null);
        }
        int width = (UIUtils.getScreenWidth(context) - UIUtils.dip2px(context, 80)) / 8;
        for (int i = 0; i < listData.length; i++) {
            VideoThumbnailInfo info = listData[i];
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(info.mWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            params.leftMargin = 5;
            imageView.setLayoutParams(params);
            imageView.getLayoutParams().width = (int) width;
            Bitmap bitmap = info.mBitmap;
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                VideoThumbnailTask.loadBitmap(this, imageView,
                        null, (long) (info.mCurrentTime * 1000), info,
                        mEditKit, null);
            }
            imageView.setId(i);
            imageView.setTag(info);
            imageView.setOnClickListener(new clickListener());
            linearLayout.addView(imageView);
        }
    }

    private void initBuble() {
        initMap();
        for (int i = 0; i < bubbles.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.leftMargin = 10;
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageResource(bubbles[i]);
            imageView.setId(i);
            imageView.setTag(bubbles[i]);
            imageView.setOnClickListener(new clickBubbleListener());
            ll_bubble.addView(imageView);
        }
    }

    private void initMap() {
        maps.put(0, new int[]{17, 20, 118, 68});
        maps.put(1, new int[]{10, 20, 118, 85});
        maps.put(2, new int[]{3, 19, 118, 53});
        maps.put(3, new int[]{15, 10, 120, 60});
        maps.put(4, new int[]{17, 20, 100, 80});
        maps.put(5, new int[]{25, 25, 110, 72});
        maps.put(6, new int[]{10, 25, 75, 90});
        maps.put(7, new int[]{23, 10, 105, 71});
        maps.put(8, new int[]{26, 20, 103, 80});
        maps.put(9, new int[]{10, 20, 118, 60});
        maps.put(10, new int[]{10, 20, 116, 60});
        maps.put(11, new int[]{11, 16, 118, 56});
    }

    private class clickBubbleListener implements View.OnClickListener {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onClick(View view) {
            int id = view.getId();
            int res = (int) view.getTag();
            if (content_layout.getChildCount() == 2) {
                content_layout.removeView(stickerView);
            }
            stickerView = new StickerView(EditVideoActivity.this, true);
            stickerView.setOnStickerTouchListener(EditVideoActivity.this);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            int[] point = maps.get(id);
            rl.addRule(RelativeLayout.CENTER_IN_PARENT);
            stickerView.setLayoutParams(rl);
            stickerView.setTextDraw(bitmap, point[0], point[1], point[2], point[3]);
            if (tvContent.getVisibility() == View.VISIBLE) {
                UIUtils.hindView(tvContent);
            }
            content_layout.addView(stickerView);
        }
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            VideoThumbnailInfo info = (VideoThumbnailInfo) view.getTag();
            if (info != null) {
                Bitmap bitmap = info.mBitmap;
                if (bitmap != null) {
                    intiVideoThumb(bitmap);
                }
            }
        }
    }
//    private void addStikerTextView() {
//        stickerView = new StickerView(this, true);
//        stickerView.setOnStickerTouchListener(this);
//        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        rl.addRule(RelativeLayout.CENTER_HORIZONTAL | RelativeLayout.CENTER_VERTICAL);
//        rl.bottomMargin = 50;
//        stickerView.setLayoutParams(rl);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_lovely_1);
//        stickerView.setTextDraw(bitmap, 10, 19.5f, 118, 85);
//        content_layout.addView(stickerView);
//    }

    @Override
    public void onCopy(StickerView stickerView) {
    }

    @Override
    public void onDelete(StickerView stickerView) {
    }

    @Override
    public void onMoveToHead(StickerView stickerView) {
    }

    @Override
    public void onDoubleClick(StickerView sticker) {
//        PrintUtils.println("双击调用");
//        editLayout.setVisibility(View.VISIBLE);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            showPopChat(context, getWindow().getDecorView(), false);
        }
    }


    private void showPopChat(final Context context, View parent, final boolean isBg) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.popwindow_item_direct_chat, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style);

        int[] location = new int[2];
        parent.getLocationOnScreen(location);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getDrawableFromRes(R.drawable.bg_popwindow));
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        LinearLayout topArea = (LinearLayout) view.findViewById(R.id.top_area);

        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            imm.showSoftInput(editText, 0);
        } else {
            imm.showSoftInput((View) editText.getWindowToken(),
                    InputMethodManager.SHOW_FORCED);
        }
        String text;
        if (isBg) {
            text = getText(tvContent);
        } else {
            text = stickerView.getText();
        }
        if (text != null && !text.equals("")) {
            editText.setText(text);
            editText.setSelection(text.length());
        }
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                tObject.setText(s.toString());
//                tObject.commit();
//                operateView.invalidate();
                if (isBg) {
                    tvContent.setText(s.toString());
                } else {
                    stickerView.resetText(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        topArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        view.findViewById(R.id.btn_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String msg = editText.getText().toString().trim();
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private Drawable getDrawableFromRes(int resId) {
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, resId);
        return new BitmapDrawable(bmp);
    }


    public void getBitmapsFromVideo() {
//        String dataPath = Environment.getExternalStorageDirectory() + "/testVideo.mp4";
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(url);
// 取得视频的长度(单位为毫秒)
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
// 取得视频的长度(单位为秒)
        int seconds = Integer.valueOf(time) / 1000;
        String str = FileUtils.getFileNameFromPath(url);
        String fileName = str.substring(0, str.indexOf("."));
// 得到每一秒时刻的bitmap比如第一秒,第二秒
        Bitmap bitmap = retriever.getFrameAtTime(1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        String path = Constants.CACHE_IMG_DIR + fileName + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            img_path = path;
        }
//        for (int i = 1; i <= seconds; i++) {
//            Bitmap bitmap = retriever.getFrameAtTime(i * 1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//            String path = Constants.CACHE_IMG_DIR + fileName + ".png";
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(path);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
//                fos.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}
