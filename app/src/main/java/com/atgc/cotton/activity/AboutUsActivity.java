package com.atgc.cotton.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by Johnny on 2017-08-19.
 */
public class AboutUsActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
