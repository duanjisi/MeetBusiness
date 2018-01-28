package com.atgc.cotton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.activity.production.other.OtherPlayerActivity;
import com.atgc.cotton.adapter.MsgAdapter;
import com.atgc.cotton.adapter.ShoppingCarAdapter;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.entity.MsgEntity;
import com.atgc.cotton.presenter.MsgPresenter;
import com.atgc.cotton.presenter.view.IMsgView;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

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

    private int page = 1;
    private boolean isOnRefresh = false;      //是否是刷新
    private String token;
    private List<MsgEntity.DataBean> allDatas = new ArrayList<>();
    private int delPostion = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        initUI();
        initData();
    }

    private void initUI() {
        token = App.getInstance().getToken();

        rvContent.setLayoutManager(new LinearLayoutManager(this));

        rvContent.setHeaderViewColor(R.color.red_fuwa, R.color.red_fuwa_alpa_stroke, android.R.color.white);
        rvContent.setRefreshProgressStyle(ProgressStyle.Pacman); //设置下拉刷新Progress的样式
        rvContent.setFooterViewHint("拼命加载中", "我是有底线的", "网络不给力啊，点击再试一次吧");
        adapter = new MsgAdapter(this);
        LRecyclerViewAdapter adapter1 = new LRecyclerViewAdapter(adapter);
        rvContent.setAdapter(adapter1);

        rvContent.setLoadMoreEnabled(true);


        rvContent.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvContent.refreshComplete(10);
                        isOnRefresh = true;
                        initData();
                    }
                }, 1000);


            }
        });
        rvContent.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isOnRefresh = false;
                        initData();
                    }
                }, 1000);
            }
        });
        adapter.setOnDelListener(new ShoppingCarAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                delPostion = pos;
                int id = allDatas.get(pos).getId();
                mPresenter.deleteMsg(token, id);

            }
        });

        adapter.setItemClickListener(new MsgAdapter.ItemClickListener() {
            @Override
            public void onItemClick(MsgEntity.DataBean bean) {
                if (bean != null) {
                    String feedId = "" + bean.getFeedId();
                    if (!TextUtils.isEmpty(feedId)) {
                        Intent intent = new Intent(context, OtherPlayerActivity.class);
                        intent.putExtra("feedid", feedId);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void initData() {
        if (isOnRefresh) {
            page = 1;
        }
        mPresenter.searchMsg(token, page, 10);
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

    /**
     * 查询消息访问接口成功
     *
     * @param s
     */
    @Override
    public void searchMsgSuccess(String s) {
        BaseResult result = JSON.parseObject(s, BaseResult.class);
        if (result.getCode() == 1) {
            showToast("没有消息哦");
            return;
        }
        MsgEntity msgEntity = JSON.parseObject(s, MsgEntity.class);
        if (msgEntity != null) {
            int code = msgEntity.getCode();
            if (code == 0) {

                List<MsgEntity.DataBean> datas = msgEntity.getData();
                if (datas.size() == 10) {
                    rvContent.setNoMore(false);
                    page++;
                } else {
                    rvContent.setNoMore(true);
                }
                if (!isOnRefresh) {      //第一次和加载更多
                    allDatas.addAll(datas);
                } else {    //下拉刷新
                    if (allDatas.size() > 0) {
                        allDatas.clear();
                    }
                    allDatas.addAll(datas);
                }
                adapter.setDatas(allDatas);
                adapter.notifyDataSetChanged();
            } else {
                showToast(msgEntity.getMessage());
            }

        }
    }

    /**
     * 删除消息接口访问成功
     *
     * @param s
     */
    @Override
    public void deleteMsgSuccess(String s) {
        BaseResult result = JSON.parseObject(s, BaseResult.class);
        if (result != null) {
            if (result.getCode() == 0) {
                showToast("删除成功");
                adapter.remove(delPostion);
            } else {
                showToast(result.getMessage());
            }
        }
    }

    /**
     * 请求网络网络不好
     *
     * @param s
     */
    @Override
    public void applyFailure(String s) {
        showToast(s);

    }
}
