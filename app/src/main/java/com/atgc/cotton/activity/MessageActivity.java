package com.atgc.cotton.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.adapter.MsgAdapter;
import com.atgc.cotton.presenter.MsgPresenter;
import com.atgc.cotton.presenter.view.IMsgView;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liw on 2017/6/28.
 */

public class MessageActivity extends MvpActivity<MsgPresenter> implements IMsgView {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.rv_content)
    LRecyclerView rvContent;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        initUI();
        initData();
    }

    private void initUI() {
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setHeaderViewColor(R.color.red_fuwa, R.color.red_fuwa_alpa_stroke, android.R.color.white);
        rvContent.setRefreshProgressStyle(ProgressStyle.Pacman); //设置下拉刷新Progress的样式
        adapter = new MsgAdapter(this);
        LRecyclerViewAdapter adapter1 = new LRecyclerViewAdapter(adapter);
        rvContent.setAdapter(adapter1);
        rvContent.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvContent.refreshComplete(10);
                        initData();
                    }
                }, 1000);


            }
        });
    }

    private void initData() {

    }

    @Override
    protected MsgPresenter createPresenter() {
        return new MsgPresenter(this);
    }

    @OnClick({R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;

        }
    }
}
