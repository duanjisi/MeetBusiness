package com.boss66.meetbusiness.activity.videoedit;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.activity.base.BaseActivity;
import com.boss66.meetbusiness.adapter.FilterAdapter;
import com.boss66.meetbusiness.entity.FilterEntity;
import com.boss66.meetbusiness.util.UIUtils;
import com.ksyun.media.shortvideo.kit.KSYEditKit;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautyToneCurveFilter;

import java.util.ArrayList;
import java.util.List;

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

    private RecyclerView rv_filter;

    public final static String SRC_URL = "srcurl";

    private List<FilterEntity> datas;

    private ImgBeautyToneCurveFilter acvFilter;

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
                Log.i("liwya","setItemClickListener"+postion);

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
        mEditKit.stopEditPreview();
        mEditKit.release();
    }
}
