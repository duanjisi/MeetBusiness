package com.atgc.cotton.activity.production.other;

import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.VideoEntity;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.MyScrollView;
import com.ksyun.media.shortvideo.kit.KSYEditKit;
import com.ksyun.media.shortvideo.utils.ShortVideoConstants;

/**
 * Created by Johnny on 2017/7/13.
 * 他人作品播放页
 */
public class OtherPlayerActivity extends BaseActivity implements MyScrollView.ScrollViewListener, View.OnClickListener {
    private GLSurfaceView mEditPreviewView;
    private KSYEditKit mEditKit;
    private RelativeLayout rl_top_bar;
    private ImageView iv_back, iv_more;
    private TextView tv_title;
    private MyScrollView scrollView;
    private RelativeLayout relativeLayout;
    private View middle;
    private float mImageHeight;
    private Resources resources;
    public final static String SRC_URL = "srcurl";
    private VideoEntity videoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_player);
        initViews();
    }

    private void initViews() {
        resources = getResources();
        rl_top_bar = (RelativeLayout) findViewById(R.id.rl_top_bar);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_more = (ImageView) findViewById(R.id.iv_more);
        tv_title = (TextView) findViewById(R.id.tv_title);
        mEditPreviewView = (GLSurfaceView) findViewById(R.id.edit_preview);
        iv_back.setOnClickListener(this);
        iv_more.setOnClickListener(this);

        scrollView = (MyScrollView) findViewById(R.id.scrollview);
        middle = findViewById(R.id.item_midle);
        scrollView.setScrollViewListener(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_top_view);
        mImageHeight = UIUtils.getScreenHeight(context);

        Log.i("info", "================mImageHeight:" + mImageHeight);
        relativeLayout.getLayoutParams().width = (int) mImageHeight;
        relativeLayout.getLayoutParams().height = (int) mImageHeight;

        mEditPreviewView.getLayoutParams().width = (int) mImageHeight;
        mEditPreviewView.getLayoutParams().height = (int) mImageHeight;
        initEditKit();
        startEditPreview();
    }

    private void initEditKit() {
        mEditKit = new KSYEditKit(this);
        mEditKit.setDisplayPreview(mEditPreviewView);
        mEditKit.setOnErrorListener(mOnErrorListener);
        mEditKit.setOnInfoListener(mOnInfoListener);
        Bundle bundle = getIntent().getExtras();
        videoEntity = (VideoEntity) bundle.getSerializable("obj");
//        url = bundle.getString(SRC_URL);
        String url = videoEntity.getMediaPath();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:

                break;
        }
    }

    @Override
    public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
        Log.i("info", "================x:" + x + "\n" + "y:" + y + "  oldx:" + oldx + "\n" + "oldy:" + oldy);
        switchView(y);
    }

    private void showTopBar(int y) {
        if (y > mImageHeight) {
            iv_back.setImageResource(R.drawable.nav_return);
            iv_more.setImageResource(R.drawable.nav_more);
            rl_top_bar.setBackgroundColor(resources.getColor(R.color.white));
            tv_title.setText("视频详情");
        } else {
            iv_back.setImageResource(R.drawable.play_return);
            iv_more.setImageResource(R.drawable.play_more);
            rl_top_bar.setBackgroundColor(resources.getColor(R.color.transparent));
            tv_title.setText("");
        }
    }

    private void switchView(int y) {
        showTopBar(y);
        if (y != 0) {
            if (middle.getVisibility() == View.VISIBLE) {
                middle.setVisibility(View.GONE);
            }
        } else {
            if (middle.getVisibility() != View.VISIBLE) {
                middle.setVisibility(View.VISIBLE);
            }
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
                    break;
                case ShortVideoConstants.SHORTVIDEO_ERROR_SDK_AUTHFAILED:
                    break;
                case ShortVideoConstants.SHORTVIDEO_ERROR_UPLOAD_KS3_TOKEN_ERROR:
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
                    break;
                case ShortVideoConstants.SHORTVIDEO_COMPOSE_START: {
                    mEditKit.pauseEditPreview();
                    return null;
                }
                case ShortVideoConstants.SHORTVIDEO_COMPOSE_FINISHED: {
                }
                case ShortVideoConstants.SHORTVIDEO_COMPOSE_ABORTED:
                    break;
                case ShortVideoConstants.SHORTVIDEO_GET_KS3AUTH: {
                    if (msgs.length == 6) {
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

}
