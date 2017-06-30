package com.boss66.meetbusiness.activity.videoedit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.activity.base.BaseActivity;
import com.boss66.meetbusiness.adapter.VideoThumbAdapter;
import com.boss66.meetbusiness.photoedit.OperateUtils;
import com.boss66.meetbusiness.photoedit.OperateView;
import com.boss66.meetbusiness.photoedit.TextObject;
import com.boss66.meetbusiness.util.UIUtils;
import com.boss66.meetbusiness.videorange.VideoThumbnailInfo;
import com.boss66.meetbusiness.videorange.VideoThumbnailTask;
import com.ksyun.media.shortvideo.kit.KSYEditKit;
import com.ksyun.media.shortvideo.utils.ShortVideoConstants;

/**
 * Created by Johnny on 2017/6/26.
 */
public class EditVideoActivity extends BaseActivity implements View.OnClickListener {

    private GLSurfaceView mEditPreviewView;
    private KSYEditKit mEditKit;
    private AppCompatSeekBar mOriginAudioVolumeSeekBar;
    private AppCompatSeekBar mBgmVolumeSeekBar;
    private SeekBarChangedObserver mSeekBarChangedObsesrver;
    private static final int LONG_VIDEO_MAX_LEN = 300000;
    private int mMaxClipSpanMs = LONG_VIDEO_MAX_LEN;  //默认的最大裁剪时长

    private RadioGroup mRadioGroup;
    private View vFilter, vSound, vTailor;
    private ImageView ivClose;
    private TextView tvNext, tvSwitcher, tvRecord, tvNative;
    private LinearLayout content_layout;
    private OperateView operateView;
    private OperateUtils operateUtils;
    private Bitmap resizeBmp = null;
    final Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (content_layout.getWidth() != 0) {
                    fillContent();
                    addFont();
                }
            }
        }
    };
    //    private Gallery gallery;
    private LinearLayout linearLayout;
    private VideoThumbAdapter mVideoThumbnailAdapter;

    public final static String SRC_URL = "srcurl";

    private boolean isOriginalVoice = false;
    private BottomSheetDialog sheetDialog;
    private boolean mIsRecord = false;
    //    private MP3Recorder mRecorder;
    private int nowTime, allTime = 8;
    private ProgressBar pb_progress_bar;
    private ImageView iv_record;
    private boolean isTouch;
    private String filePath;


    public static void startActivity(Context context, String srcurl) {
        Intent intent = new Intent(context, EditVideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SRC_URL, srcurl);
        context.startActivity(intent);
    }

    private void fillContent() {
        if (resizeBmp != null) {
            if (content_layout.getChildCount() != 0) {
                content_layout.removeAllViews();
            }
            operateView = new OperateView(context, resizeBmp);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    resizeBmp.getWidth(), resizeBmp.getHeight());
            operateView.setLayoutParams(layoutParams);
            content_layout.addView(operateView);
            operateView.setMultiAdd(false); //设置此参数，可以添加多个文字
        }
    }

    private void addFont() {
        if (operateView != null) {
            TextObject textObj = operateUtils.getTextObject("添加文字",
                    operateView, 5, 150, 150);
            if (textObj != null) {
                textObj.setTextSize(22);
                textObj.commit();
                operateView.addItem(textObj);
                operateView.setOnListener(new OperateView.MyListener() {
                    public void onClick(TextObject tObject) {
                        alert(tObject);
                    }
                });
            }
//            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_normal_2);
//            ImageObject imgObject = operateUtils.getImageObject(bmp, operateView,
//                    5, 150, 100);
//            operateView.addItem(imgObject);
        }
    }

    private void alert(final TextObject tObject) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);
        initViews();
    }

    private void initViews() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvSwitcher = (TextView) findViewById(R.id.tv_switcher_voice);
        tvRecord = (TextView) findViewById(R.id.tv_record);
        tvNative = (TextView) findViewById(R.id.tv_native);
//        gallery = (Gallery) findViewById(R.id.gallery);
        linearLayout = (LinearLayout) findViewById(R.id.container);
        mEditPreviewView = (GLSurfaceView) findViewById(R.id.edit_preview);

        ivClose.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        tvSwitcher.setOnClickListener(this);
        tvRecord.setOnClickListener(this);
        tvNative.setOnClickListener(this);

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
        content_layout = (LinearLayout) findViewById(R.id.mainLayout);
        operateUtils = new OperateUtils(this);
        startEditPreview();
    }

    private void initEditKit() {
        mEditKit = new KSYEditKit(this);
        mEditKit.setDisplayPreview(mEditPreviewView);
        mEditKit.setOnErrorListener(mOnErrorListener);
        mEditKit.setOnInfoListener(mOnInfoListener);
//        mEditKit.addStickerView(mKSYStickerView);
//        mEditKit.addTextStickerView(mTextView);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString(SRC_URL);
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
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    resizeBmp = operateUtils.compressionFiller(bitmap,
                            content_layout);
                    Log.i("info", "==========resizeBmp:" + resizeBmp);
                    if (resizeBmp != null) {
                        Message message = new Message();
                        message.what = 1;
                        myHandler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
                showBottomView();
                break;
            case R.id.tv_native://本地
                openActvityForResult(LocalMusicActivity.class, 101);
                break;
            case R.id.tv_next:

                break;
            case R.id.iv_record_colse:
//                resolveStopRecord();
                break;
            case R.id.iv_record_ok:
//                resolveStopRecord();
                if (!TextUtils.isEmpty(filePath))
                    mEditKit.changeBgmMusic(filePath);
                break;
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

    private void onOriginAudioClick(boolean isCheck) {
        //是否删除原始音频
        mEditKit.enableOriginAudio(isCheck);

        mOriginAudioVolumeSeekBar.setEnabled(isCheck);
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
//    /**
//     * 停止录音
//     */
//    private void resolveStopRecord() {
//        if (sheetDialog != null && sheetDialog.isShowing()) {
//            sheetDialog.dismiss();
//        }
//        if (mRecorder != null && mRecorder.isRecording()) {
//            mRecorder.setPause(false);
//            mRecorder.stop();
//        }
//        mIsRecord = false;
//    }
//
//    //录音异常
//    private void resolveError() {
//        FileUtils.deleteFile(filePath);
//        filePath = "";
//        if (mRecorder != null && mRecorder.isRecording()) {
//            mRecorder.stop();
//        }
//    }
//
//    /**
//     * 暂停
//     */
//    private void resolvePause() {
//        if (!mIsRecord)
//            return;
//        if (mRecorder.isPause()) {
//            mRecorder.setPause(false);
//        } else {
//            mRecorder.setPause(true);
//        }
//    }

    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {         // handle message
            switch (msg.what) {
                case 1:
                    if (nowTime >= allTime) {
//                        resolveStopRecord();
                        if (!TextUtils.isEmpty(filePath))
                            mEditKit.changeBgmMusic(filePath);
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
//                    if (mComposeAlertDialog != null) {
//                        mComposeAlertDialog.setCancelable(false);
//                        mComposeAlertDialog.show();
//                        mComposeAlertDialog.composeStarted();
//                    }
                    return null;
                }
                case ShortVideoConstants.SHORTVIDEO_COMPOSE_FINISHED: {
//                    mAudioReverbSpinner.setSelection(0);
//                    mAudioEffectSpinner.setSelection(0);
//                    if (mComposeAlertDialog != null) {
//                        mComposeAlertDialog.composeFinished(msgs[0]);
//                        mComposeFinished = true;
//                    }
//                    //上传必要信息：bucket,objectkey，及PutObjectResponseHandler上传过程回调
//                    mCurObjectKey = getPackageName() + "/" + System.currentTimeMillis() + ".mp4";
//                    KS3ClientWrap.KS3UploadInfo bucketInfo = new KS3ClientWrap.KS3UploadInfo
//                            ("ksvsdemo", mCurObjectKey, mPutObjectResponseHandler);
//                    return bucketInfo;
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
        for (int i = 0; i < listData.length; i++) {
            VideoThumbnailInfo info = listData[i];
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(info.mWidth, LinearLayout.LayoutParams.MATCH_PARENT));
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

    public void onResume() {
        super.onResume();
        mEditKit.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mEditKit.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEditKit.stopEditPreview();
        mEditKit.release();
    }
}
