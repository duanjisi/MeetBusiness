package com.atgc.cotton.activity;

import android.os.Bundle;
import android.view.View;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;

/**
 * Created by liw on 2017/7/11.
 */
public class ChooseAddressActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);
        initUI();
    }

    private void initUI() {

        findViewById(R.id.btn_add).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        openActivity(EditAddressActivity.class);
    }
}
