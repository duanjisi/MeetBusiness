package com.atgc.cotton.activity.production.mine;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.widget.CircleImageView;

/**
 * Created by Johnny on 2017/7/5.
 * 我的二维码
 */
public class QrCodeActivity extends BaseActivity {
    private ImageView iv_back, iv_share, iv_qr_code;
    private CircleImageView avatar;
    private TextView tv_name, tv_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        avatar = (CircleImageView) findViewById(R.id.iv_head);
        iv_qr_code = (ImageView) findViewById(R.id.iv_qcode);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_id = (TextView) findViewById(R.id.tv_id);
    }
}
