package com.boss66.meetbusiness.activity.videoedit;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.activity.base.BaseActivity;
import com.boss66.meetbusiness.util.UIUtils;
import com.ksyun.media.shortvideo.kit.KSYEditKit;

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

                break;
            case R.id.tv_record://录音

                break;
            case R.id.tv_native://本地

                break;
            case R.id.tv_next:

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
}
