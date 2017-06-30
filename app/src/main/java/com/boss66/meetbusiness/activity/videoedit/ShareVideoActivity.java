package com.boss66.meetbusiness.activity.videoedit;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.activity.base.BaseActivity;

/**
 * Created by Johnny on 2017/6/27.
 * 分享视频
 */
public class ShareVideoActivity extends BaseActivity {
    private ImageView ivClose, iv_video_bg;
    private TextView tvPublish;
    private EditText editText;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_video);
    }

    private void initViews() {

    }

    private class CheckListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup arg0, int arg1) {

            switch (arg1) {
                case R.id.rb_qq:

                    break;
                case R.id.rb_zone:

                    break;
                case R.id.rb_wx:

                    break;
                case R.id.rb_wx_circle:

                    break;
                default:
                    break;
            }
        }
    }
}
