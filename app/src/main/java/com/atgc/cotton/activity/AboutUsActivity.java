package com.atgc.cotton.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.util.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2017-08-19.
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        tvVersion.setText("V " + AppUtil.getVersionName(context));
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
