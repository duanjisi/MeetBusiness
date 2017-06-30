package com.boss66.meetbusiness.activity.videoedit;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.activity.base.BaseActivity;
import com.boss66.meetbusiness.util.UIUtils;
import com.czt.mp3recorder.MP3Recorder;
import com.ksyun.media.shortvideo.kit.KSYEditKit;
import com.shuyu.waveview.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Johnny on 2017/6/26.
 */
public class EditVideoActivity extends BaseActivity implements View.OnClickListener {

    private GLSurfaceView mEditPreviewView;
    private KSYEditKit mEditKit;
    private AppCompatSeekBar mOriginAudioVolumeSeekBar;
    private AppCompatSeekBar mBgmVolumeSeekBar;
    private SeekBarChangedObserver mSeekBarChangedObsesrver;

    private RadioGroup mRadioGroup;
    private View vFilter, vSound;
    private ImageView ivClose;
    private TextView tvNext, tvSwitcher, tvRecord, tvNative;

    public final static String SRC_URL = "srcurl";

    private boolean isOriginalVoice = false;
    private BottomSheetDialog sheetDialog;
    private boolean mIsRecord = false;
    private MP3Recorder mRecorder;
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
        mEditPreviewView = (GLSurfaceView) findViewById(R.id.edit_preview);

        ivClose.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        tvSwitcher.setOnClickListener(this);
        tvRecord.setOnClickListener(this);
        tvNative.setOnClickListener(this);

        initEditKit();

        vFilter = findViewById(R.id.edit_video_filter);
        vSound = findViewById(R.id.edit_video_sound);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(new CheckListener());

        mSeekBarChangedObsesrver = new SeekBarChangedObserver();
        mOriginAudioVolumeSeekBar = (AppCompatSeekBar) findViewById(R.id.origin_audio_volume);
        mOriginAudioVolumeSeekBar.setOnSeekBarChangeListener(mSeekBarChangedObsesrver);
        mBgmVolumeSeekBar = (AppCompatSeekBar) findViewById(R.id.music_audio_volume);
        mBgmVolumeSeekBar.setOnSeekBarChangeListener(mSeekBarChangedObsesrver);
        startEditPreview();
    }

    private void initEditKit() {
        mEditKit = new KSYEditKit(this);
        mEditKit.setDisplayPreview(mEditPreviewView);
//        mEditKit.setOnErrorListener(mOnErrorListener);
//        mEditKit.setOnInfoListener(mOnInfoListener);
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
                resolveStopRecord();
                break;
            case R.id.iv_record_ok:
                resolveStopRecord();
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
                    UIUtils.showView(vFilter);
                    UIUtils.hindView(vSound);
                    break;
                case R.id.rb_sound:
                    UIUtils.showView(vSound);
                    UIUtils.hindView(vFilter);
                    break;
                case R.id.rb_cover:

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
                                resolvePause();
                            } else {
                                resolveRecord();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            isTouch = false;
                            iv_record.setSelected(false);
                            resolvePause();
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
    private void resolveRecord() {
        filePath = FileUtils.getAppPath();
        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Toast.makeText(this, "创建文件失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        filePath = FileUtils.getAppPath() + UUID.randomUUID().toString() + ".mp3";
        mRecorder = new MP3Recorder(new File(filePath));
        mRecorder.setErrorHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MP3Recorder.ERROR_TYPE) {
                    Toast.makeText(EditVideoActivity.this, "没有麦克风权限", Toast.LENGTH_SHORT).show();
                    resolveError();
                }
            }
        });
        try {
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(EditVideoActivity.this, "录音出现异常", Toast.LENGTH_SHORT).show();
            resolveError();
            return;
        }
        mIsRecord = true;
    }

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
                        resolveStopRecord();
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
}
