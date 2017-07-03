package com.boss66.meetbusiness.activity.vendingRack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.widget.MyStarView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * Created by GMARUnity on 2017/7/3.
 */
public class OrderEvaluateActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView tv_back;
    private Button bt_upload;
    private ImageView iv_icon;
    private MyStarView starBar;
    private EditText et_content;
    private LinearLayout ll_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_evaluate);
        initView();
    }

    private void initView() {
        ll_img = (LinearLayout) findViewById(R.id.ll_img);
        et_content = (EditText) findViewById(R.id.et_content);
        starBar = (MyStarView) findViewById(R.id.starBar);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        bt_upload = (Button) findViewById(R.id.bt_upload);
        tv_back = (TextView) findViewById(R.id.tv_back);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_back.setOnClickListener(this);
        bt_upload.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_upload:

                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
