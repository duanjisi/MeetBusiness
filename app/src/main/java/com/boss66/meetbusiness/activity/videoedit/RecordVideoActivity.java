package com.boss66.meetbusiness.activity.videoedit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.activity.base.BaseActivity;
import com.boss66.meetbusiness.entity.AuthEntity;
import com.boss66.meetbusiness.http.BaseDataRequest;
import com.boss66.meetbusiness.http.request.AuthRequest;
import com.boss66.meetbusiness.widget.recordclip.RecordProgressController;
import com.boss66.meetbusiness.widget.recordclip.filter.ImgFaceunityFilter;
import com.ksyun.media.shortvideo.kit.KSYRecordKit;
import com.ksyun.media.shortvideo.utils.AuthInfoManager;
import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.capture.camera.CameraTouchHelper;
import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.logstats.StatsLogReport;

import java.io.File;

/**
 * Created by Johnny on 2017/6/26.
 * 录制视频
 */
public class RecordVideoActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "RecordActivity";
    public static final int MAX_DURATION = 5 * 60 * 1000;  //最长拍摄时长
    public static final int MIN_DURATION = 5 * 1000;  //最短拍摄时长
    private final static int PERMISSION_REQUEST_CAMERA_AUDIOREC = 1;

    public final static int FRAME_RATE = 20;
    public final static int VIDEO_BITRATE = 1000;
    public final static int AUDIO_BITRATE = 64;
    public final static int VIDEO_RESOLUTION = StreamerConstants.VIDEO_RESOLUTION_480P;
    public final static int ENCODE_TYPE = AVConst.CODEC_ID_HEVC;
    public final static int ENCODE_METHOD = StreamerConstants.ENCODE_METHOD_SOFTWARE;
    public final static int ENCODE_PROFILE = VideoEncodeFormat.ENCODE_PROFILE_BALANCE;
    private final int READ_VIDEO = 4;//本地视频

    //断点拍摄进度控制
    private RecordProgressController mRecordProgressCtl;
    private View mBarBottomLayout;
    private boolean mIsFileRecording = false;
    private boolean mIsFlashOpened = false;
    private String mRecordUrl;
    private LinearLayout ll_photo;
    private ImageView ivClose, iv_record;
    private TextView tvNext, tvDelete;
    private View mSwitchCameraView;
    private View mFlashView;
    private Chronometer mChronometer;
    private GLSurfaceView mCameraPreviewView;
    private KSYRecordKit mKSYRecordKit;
    private boolean mHWEncoderUnsupported;
    private boolean mSWEncoderUnsupported;
    private ImgFaceunityFilter mImgFaceunityFilter;
    private Handler mMainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_record_video);
        //must set
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //默认设置为竖屏，当前暂时只支持竖屏，后期完善
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //init UI
        //录制预览部分宽高1:1比例显示（用户可以按照自己的需求处理）
        //just PORTRAIT
        WindowManager windowManager = (WindowManager) getApplication().
                getSystemService(getApplication().WINDOW_SERVICE);
        initViews();
    }


    private void initViews() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        iv_record = (ImageView) findViewById(R.id.iv_record);
        ll_photo = (LinearLayout) findViewById(R.id.ll_photo);

        tvNext = (TextView) findViewById(R.id.tv_next);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        mSwitchCameraView = findViewById(R.id.switch_cam);
        mFlashView = findViewById(R.id.flash);
        ivClose.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        iv_record.setOnClickListener(this);
        ll_photo.setOnClickListener(this);

        mSwitchCameraView.setOnClickListener(this);
        mFlashView.setOnClickListener(this);

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mCameraPreviewView = (GLSurfaceView) findViewById(R.id.camera_preview);
        mBarBottomLayout = (View) findViewById(R.id.bar_bottom);
        //断点拍摄UI初始化
        //mBarBottomLayout为拍摄进度显示的父控件
        mRecordProgressCtl = new RecordProgressController(mBarBottomLayout);
        //拍摄时长变更回调
        mRecordProgressCtl.setRecordingLengthChangedListener(mRecordLengthChangedListener);
        mRecordProgressCtl.start();
        initKSYRecorder();
        CameraTouchHelper cameraTouchHelper = new CameraTouchHelper();
        cameraTouchHelper.setCameraCapture(mKSYRecordKit.getCameraCapture());
        mCameraPreviewView.setOnTouchListener(cameraTouchHelper);
        // set CameraHintView to show focus rect and zoom ratio
//        cameraTouchHelper.setCameraHintView(mCameraHintView);
        startCameraPreviewWithPermCheck();
        requestAuth();
    }

    private void initKSYRecorder() {
        mKSYRecordKit = new KSYRecordKit(this);
        mKSYRecordKit.setPreviewFps(FRAME_RATE);
        mKSYRecordKit.setTargetFps(FRAME_RATE);
        mKSYRecordKit.setVideoKBitrate(VIDEO_BITRATE);
        mKSYRecordKit.setAudioBitrate(AUDIO_BITRATE);
        mKSYRecordKit.setPreviewResolution(VIDEO_RESOLUTION);
        mKSYRecordKit.setTargetResolution(VIDEO_RESOLUTION);
        mKSYRecordKit.setVideoCodecId(ENCODE_TYPE);
        mKSYRecordKit.setEncodeMethod(ENCODE_METHOD);
        mKSYRecordKit.setVideoEncodeProfile(ENCODE_PROFILE);
        mKSYRecordKit.setRotateDegrees(0);

        mKSYRecordKit.setDisplayPreview(mCameraPreviewView);
        mKSYRecordKit.setEnableRepeatLastFrame(false);
        mKSYRecordKit.setCameraFacing(CameraCapture.FACING_FRONT);
//        mKSYRecordKit.setFrontCameraMirror(mFrontMirrorCheckBox.isChecked());
        mKSYRecordKit.setOnInfoListener(mOnInfoListener);
        mKSYRecordKit.setOnErrorListener(mOnErrorListener);
        mKSYRecordKit.setOnLogEventListener(mOnLogEventListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_next:
                onNextClick();
                break;
            case R.id.tv_delete:
                onDeleteClick();
                break;
            case R.id.switch_cam:
                onSwitchCamera();
                break;
            case R.id.flash:
                onFlashClick();
                break;
            case R.id.iv_record:
                onRecordClick();
                break;
            case R.id.ll_photo:
                openActvityForResult(LocalVideoFilesActivity.class, READ_VIDEO);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case READ_VIDEO:
                    if (data != null) {
                        String url = data.getStringExtra("filePath");
                        Log.i("info", "===============url:" + url);
                        showToast("启动到编辑视频界面!", true);
                        if (!TextUtils.isEmpty(url)) {
                            EditVideoActivity.startActivity(this, url);
                        }
                    }
                    break;
            }
        }
    }

    private void onDeleteClick() {
        if (mKSYRecordKit.getRecordedFilesCount() >= 1) {
            if (!tvDelete.isSelected()) {
                tvDelete.setSelected(true);
                //设置最后一个文件为待删除文件
                mRecordProgressCtl.setLastClipPending();
            } else {
                tvDelete.setSelected(false);
                //删除文件时，若文件正在录制，则需要停止录制
                if (mIsFileRecording) {
                    stopRecord(false);
                }
                //删除录制文件
                mKSYRecordKit.deleteRecordFile(mKSYRecordKit.getLastRecordedFiles());
                mRecordProgressCtl.rollback();
                updateDeleteView();
                tvDelete.setEnabled(true);
            }
        }
    }

    /**
     * 开始/停止录制
     */
    private void onRecordClick() {
        if (mIsFileRecording) {
            stopRecord(false);
        } else {
            startRecord();
        }
        //清除back按钮的状态
        clearBackoff();
    }

    /**
     * 清除back按钮的状态（删除），并设置最后一个录制的文件为普通文件
     *
     * @return
     */
    private boolean clearBackoff() {
        if (tvDelete.isSelected()) {
            tvDelete.setSelected(false);
            //设置最后一个文件为普通文件
            mRecordProgressCtl.setLastClipNormal();
            return true;
        }
        return false;
    }

    private void onSwitchCamera() {
        mKSYRecordKit.switchCamera();
    }

    private void onFlashClick() {
        if (mIsFlashOpened) {
            mKSYRecordKit.toggleTorch(false);
            mIsFlashOpened = false;
        } else {
            mKSYRecordKit.toggleTorch(true);
            mIsFlashOpened = true;
        }
    }

    /**
     * 进入编辑页面
     */
    private void onNextClick() {
//        clearBackoff();
//        clearRecordState();
//        mRecordView.getDrawable().setLevel(1);
        //进行编辑前需要停止录制，并且结束断点拍摄
        stopRecord(true);
    }

    private RecordProgressController.RecordingLengthChangedListener mRecordLengthChangedListener =
            new RecordProgressController.RecordingLengthChangedListener() {
                @Override
                public void passMinPoint(boolean pass) {
                    if (pass) {
                        //超过最短时长显示下一步按钮，否则不能进入编辑，最短时长可自行设定，Demo中当前设定为5s
//                        mNextView.setVisibility(View.VISIBLE);
                        tvNext.setVisibility(View.VISIBLE);
                    } else {
                        tvNext.setVisibility(View.GONE);
                    }
                }

                @Override
                public void passMaxPoint() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //到达最大拍摄时长时，需要主动停止录制
                            stopRecord(false);
//                            mRecordView.getDrawable().setLevel(1);
//                            mRecordView.setEnabled(false);
                            showToast("录制结束，请继续操作", true);
                        }
                    });
                }
            };

    //start recording to a local file
    private void startRecord() {
        String fileFolder = "/sdcard/MeetBus";
        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdir();
        }
        mRecordUrl = fileFolder + "/" + System.currentTimeMillis() + ".mp4";
        Log.d(TAG, "record url:" + mRecordUrl);
//        float val = mMicAudioVolumeSeekBar.getProgress() / 100.f;
//        mKSYRecordKit.setVoiceVolume(val);
        mKSYRecordKit.startRecord(mRecordUrl);
        mIsFileRecording = true;
        iv_record.setImageResource(R.drawable.shot_begin);
//        mRecordView.getDrawable().setLevel(2);
    }

    /**
     * 停止拍摄
     *
     * @param finished 代表是否结束断点拍摄
     */
    private void stopRecord(boolean finished) {
        //录制完成进入编辑
        //若录制文件大于1则需要触发文件合成
        if (finished) {
            String fileFolder = getRecordFileFolder();
            //合成文件路径
            String outFile = fileFolder + "/" + "merger_" + System.currentTimeMillis() + ".mp4";
            //合成过程为异步，需要block下一步处理
            final MegerFilesAlertDialog dialog = new MegerFilesAlertDialog(this, R.style.dialog);
            dialog.setCancelable(false);
            dialog.show();
            mKSYRecordKit.stopRecord(outFile, new KSYRecordKit.MegerFilesFinishedListener() {
                @Override
                public void onFinished(final String filePath) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            mRecordUrl = filePath;
                            //合成结束启动编辑
                            EditVideoActivity.startActivity(getApplicationContext(), mRecordUrl);
                        }
                    });
                }
            });
        } else {
            //普通录制停止
            mKSYRecordKit.stopRecord();
        }
        //更新进度显示
        mRecordProgressCtl.stopRecording();
//        mRecordView.getDrawable().setLevel(1);
        updateDeleteView();
        refreshSystemMedia();
        mIsFileRecording = false;
        iv_record.setImageResource(R.drawable.shot_not_begin);
        stopChronometer();
    }


    private void refreshSystemMedia() {
        if (mRecordUrl != null && !mRecordUrl.equals("")) {
            File file = new File(mRecordUrl);
            if (file.exists()) {
                updataMedia(file);
            }
        }
    }

    private void updataMedia(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()},
                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {
                            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            scanIntent.setData(uri);
                            sendBroadcast(scanIntent);
                        }
                    });
        } else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.fromFile(file)));
        }
    }


    public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mMs;
        private File mFile;

        public SingleMediaScanner(Context context, File f) {
            mFile = f;
            mMs = new MediaScannerConnection(context, this);
            mMs.connect();
        }

        @Override
        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mMs.disconnect();
            Log.i("TAG", "Finished scanning " + path);
        }

    }

    /**
     * 开始拍摄更新，删除按钮状态
     */
    private void updateDeleteView() {
        if (mKSYRecordKit.getRecordedFilesCount() >= 1) {
//            mBackView.getDrawable().setLevel(2);
            tvDelete.setVisibility(View.VISIBLE);
        } else {
//            mBackView.getDrawable().setLevel(1);
            tvDelete.setVisibility(View.GONE);
        }
    }

    private String getRecordFileFolder() {
        String fileFolder = "/sdcard/MeetBus";
        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdir();
        }
        return fileFolder;
    }

    private void stopChronometer() {
        if (mIsFileRecording) {
            return;
        }

        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.stop();
    }

    private void setCameraAntiBanding50Hz() {
        Camera.Parameters parameters = mKSYRecordKit.getCameraCapture().getCameraParameters();
        if (parameters != null) {
            parameters.setAntibanding(Camera.Parameters.ANTIBANDING_50HZ);
            mKSYRecordKit.getCameraCapture().setCameraParameters(parameters);
        }
    }

    /**
     * 前后摄像头切换时，需要更新摄像头信息
     * 分辨率发生变化时，需要更新分辨率信息
     */
    private void updateFaceunitParams() {
        if (mImgFaceunityFilter != null) {
            mImgFaceunityFilter.setTargetSize(mKSYRecordKit.getTargetWidth(),
                    mKSYRecordKit.getTargetHeight());
            if (mKSYRecordKit.isFrontCamera()) {
                mImgFaceunityFilter.setMirror(true);
            } else {
                mImgFaceunityFilter.setMirror(false);
            }
        }
    }

    private KSYStreamer.OnInfoListener mOnInfoListener = new KSYStreamer.OnInfoListener() {
        @Override
        public void onInfo(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_INIT_DONE:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_INIT_DONE");
                    setCameraAntiBanding50Hz();
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_FACEING_CHANGED:
                    updateFaceunitParams();
                    break;
                case StreamerConstants.KSY_STREAMER_OPEN_FILE_SUCCESS:
                    Log.d(TAG, "KSY_STREAMER_OPEN_FILE_SUCCESS");
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mChronometer.start();
                    mRecordProgressCtl.startRecording();
                    break;
                default:
                    Log.d(TAG, "OnInfo: " + what + " msg1: " + msg1 + " msg2: " + msg2);
                    break;
            }
        }
    };
    private StatsLogReport.OnLogEventListener mOnLogEventListener =
            new StatsLogReport.OnLogEventListener() {
                @Override
                public void onLogEvent(StringBuilder singleLogContent) {
                    Log.i(TAG, "***onLogEvent : " + singleLogContent.toString());
                }
            };

    private KSYStreamer.OnErrorListener mOnErrorListener = new KSYStreamer.OnErrorListener() {
        @Override
        public void onError(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_ERROR_AV_ASYNC:
                    Log.d(TAG, "KSY_STREAMER_ERROR_AV_ASYNC " + msg1 + "ms");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_SERVER_DIED");
                    break;
                //Camera was disconnected due to use by higher priority user.
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_EVICTED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_EVICTED");
                    break;
                default:
                    Log.d(TAG, "what=" + what + " msg1=" + msg1 + " msg2=" + msg2);
                    break;
            }
            switch (what) {
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_EVICTED:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    mKSYRecordKit.stopCameraPreview();
                    break;
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_CLOSE_FAILED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_OPEN_FAILED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_FORMAT_NOT_SUPPORTED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_WRITE_FAILED:
                    stopRecord(false);
                    rollBackClipForError();
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN: {
                    handleEncodeError();
                    stopRecord(false);
                    rollBackClipForError();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startRecord();
                        }
                    }, 3000);
                }
                break;
                default:
                    break;
            }
        }
    };

    private void handleEncodeError() {
        int encodeMethod = mKSYRecordKit.getVideoEncodeMethod();
        if (encodeMethod == StreamerConstants.ENCODE_METHOD_HARDWARE) {
            mHWEncoderUnsupported = true;
            if (mSWEncoderUnsupported) {
                mKSYRecordKit.setEncodeMethod(
                        StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT);
                Log.e(TAG, "Got HW encoder error, switch to SOFTWARE_COMPAT mode");
            } else {
                mKSYRecordKit.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
                Log.e(TAG, "Got HW encoder error, switch to SOFTWARE mode");
            }
        } else if (encodeMethod == StreamerConstants.ENCODE_METHOD_SOFTWARE) {
            mSWEncoderUnsupported = true;
            if (mHWEncoderUnsupported) {
                mKSYRecordKit.setEncodeMethod(
                        StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT);
                Log.e(TAG, "Got SW encoder error, switch to SOFTWARE_COMPAT mode");
            } else {
                mKSYRecordKit.setEncodeMethod(StreamerConstants.ENCODE_METHOD_HARDWARE);
                Log.e(TAG, "Got SW encoder error, switch to HARDWARE mode");
            }
        }
    }

    /**
     * 拍摄错误停止后，删除多余文件的进度
     */
    private void rollBackClipForError() {
        //当拍摄异常停止时，SDk内部会删除异常文件，如果ctl比SDK返回的文件小，则需要更新ctl中的进度信息
        int clipCount = mRecordProgressCtl.getClipListSize();
        int fileCount = mKSYRecordKit.getRecordedFilesCount();
        if (clipCount > fileCount) {
            int diff = clipCount - fileCount;
            for (int i = 0; i < diff; i++) {
                mRecordProgressCtl.rollback();
            }
        }
    }

    private void startCameraPreviewWithPermCheck() {
        int cameraPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int audioPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (cameraPerm != PackageManager.PERMISSION_GRANTED ||
                audioPerm != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Log.e(TAG, "No CAMERA or AudioRecord permission, please check");
                Toast.makeText(this, "No CAMERA or AudioRecord permission, please check",
                        Toast.LENGTH_LONG).show();
            } else {
                String[] permissions = {Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions,
                        PERMISSION_REQUEST_CAMERA_AUDIOREC);
            }
        } else {
            mKSYRecordKit.startCameraPreview();
        }
    }

    private class MegerFilesAlertDialog extends AlertDialog {

        protected MegerFilesAlertDialog(Context context, int themID) {
            super(context, themID);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setContentView(R.layout.meger_record_files_layout);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mKSYRecordKit.setDisplayPreview(mCameraPreviewView);
        mKSYRecordKit.onResume();
        // camera may be occupied by other app in background
        startCameraPreviewWithPermCheck();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mKSYRecordKit.onPause();
        if (!mKSYRecordKit.isRecording() && !mKSYRecordKit.isFileRecording()) {
            mKSYRecordKit.stopCameraPreview();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        mRecordProgressCtl.stop();
        mRecordProgressCtl.setRecordingLengthChangedListener(null);
        mRecordProgressCtl.release();
        mKSYRecordKit.setOnLogEventListener(null);
        mKSYRecordKit.release();
        AuthInfoManager.getInstance().removeAuthResultListener(mCheckAuthResultListener);
    }

    private void requestAuth() {
        AuthRequest request = new AuthRequest("sadasd");
        request.send(new BaseDataRequest.RequestCallback<AuthEntity>() {
            @Override
            public void onSuccess(AuthEntity pojo) {
                Log.i("info", "=================:" + pojo.toString());
                //初始化鉴权信息
                AuthInfoManager.getInstance().setAuthInfo(getApplicationContext(),
                        pojo.getAuthorization(), pojo.getAmz());
                //添加鉴权结果回调接口(不是必须)
                AuthInfoManager.getInstance().addAuthResultListener(mCheckAuthResultListener);
                //开始向KSServer申请鉴权
                AuthInfoManager.getInstance().checkAuth();
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    private AuthInfoManager.CheckAuthResultListener mCheckAuthResultListener = new AuthInfoManager
            .CheckAuthResultListener() {
        @Override
        public void onAuthResult(int result) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    AuthInfoManager.getInstance().removeAuthResultListener(mCheckAuthResultListener);
                    if (AuthInfoManager.getInstance().getAuthState()) {
                        Toast.makeText(context, "Auth Success", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(context, "Auth Failed", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        }
    };
}
