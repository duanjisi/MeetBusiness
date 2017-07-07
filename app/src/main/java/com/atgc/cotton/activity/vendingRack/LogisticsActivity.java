package com.atgc.cotton.activity.vendingRack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.entity.LogisticsModel;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.LogisticsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMARUnity on 2017/7/1.
 */
public class LogisticsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv_back, tv_name, tv_num;
    private LogisticsView logisticsView;
    private ImageView iv_icon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);
        initView();
        initData();
    }

    private void initData() {
        List<LogisticsModel> datas = new ArrayList<>();
        LogisticsModel step1 = new LogisticsModel("您已提交订单，等待系统确认", LogisticsModel.STATE_COMPLETED);
        LogisticsModel step2 = new LogisticsModel("订单已确认并打包，预计12月16日送达", LogisticsModel.STATE_COMPLETED);
        LogisticsModel step3 = new LogisticsModel("包裹正在路上", LogisticsModel.STATE_COMPLETED);
        LogisticsModel step4 = new LogisticsModel("包裹正在派送", LogisticsModel.STATE_PROCESSING);
        LogisticsModel step5 = new LogisticsModel("感谢光临港棉纺织（店铺号10086），约淘店铺，" +
                "关注店铺更多动态尽在约淘动态！", LogisticsModel.STATE_DEFAULT);
        datas.add(0, step1);
        datas.add(0, step2);
        datas.add(0, step3);
        datas.add(0, step4);
        datas.add(0, step5);
        logisticsView.setmDatas(datas);
    }

    private void initView() {
        int sceenW = UIUtils.getScreenWidth(this);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_num = (TextView) findViewById(R.id.tv_num);
        logisticsView = (LogisticsView) findViewById(R.id.logisticsView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_back = (TextView) findViewById(R.id.tv_back);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv_icon.getLayoutParams();
        lp.width = sceenW / 7;
        lp.height = sceenW / 7;
        iv_icon.setLayoutParams(lp);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
