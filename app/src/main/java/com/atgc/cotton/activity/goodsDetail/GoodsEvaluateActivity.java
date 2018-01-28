package com.atgc.cotton.activity.goodsDetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.adapter.GoodsEvaluateAdapter;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.entity.GoodsEvaluateEntity;
import com.atgc.cotton.presenter.GoodsEvaluatePresenter;
import com.atgc.cotton.presenter.view.ISingleView;
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
 * Created by liw on 2017/7/26.
 */

public class GoodsEvaluateActivity extends MvpActivity<GoodsEvaluatePresenter> implements ISingleView {
    @Bind(R.id.tv_back)
    TextView tvBack;
    @Bind(R.id.rv_content)
    LRecyclerView rvContent;
    private GoodsEvaluateAdapter adapter;

    private int page = 1;
    private boolean isOnRefresh = false;      //是否是刷新
    private String goodId;
    private String token;

    private List<GoodsEvaluateEntity.DataBean> allDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_evaluate);
        ButterKnife.bind(this);
        initUI();

        initData();

    }

    @Override
    protected GoodsEvaluatePresenter createPresenter() {
        return new GoodsEvaluatePresenter(this);
    }

    private void initData() {
        if (isOnRefresh) {
            page = 1;
        }
        mPresenter.searchEvaluate(token,Integer.parseInt(goodId),page,20);

    }

    private void initUI() {
        token = App.getInstance().getToken();

        Intent intent = getIntent();
        if (intent != null) {
            goodId = intent.getStringExtra("goodId");
        }
        rvContent.setLayoutManager(new LinearLayoutManager(this));

        rvContent.setHeaderViewColor(R.color.red_fuwa, R.color.red_fuwa_alpa_stroke, android.R.color.white);
        rvContent.setRefreshProgressStyle(ProgressStyle.Pacman); //设置下拉刷新Progress的样式
        rvContent.setFooterViewHint("拼命加载中", "我是有底线的", "网络不给力啊，点击再试一次吧");
        adapter = new GoodsEvaluateAdapter(this);
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
    }

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onSuccess(String s) {
//        s ="{\"Status\":200,\"Code\":0,\"Name\":\"PublicController\",\"Message\":\"success\",\"Data\":[{\"AddTime\":1500363218,\"Avatar\":\"http://gb.cri.cn/mmsource/images/2006/06/13/el060613102.jpg\",\"Content\":\"测试评论商品\",\"GoodsId\":21,\"Id\":10,\"OrderId\":17,\"Pics\":[\"http://gb.cri.cn/mmsource/images/2006/06/13/el060613102.jpg\",\"http://gb.cri.cn/mmsource/images/2006/06/13/el060613102.jpg\",\"http://gb.cri.cn/mmsource/images/2006/06/13/el060613102.jpg\",\"http://gb.cri.cn/mmsource/images/2006/06/13/el060613102.jpg\"],\"Score\":5,\"UserId\":100000000,\"UserName\":\"无怨无悔\"}]}";
        BaseResult result = JSON.parseObject(s, BaseResult.class);
        if (result.getCode() ==1) {
            showToast("没有更多数据");
            return;
        }

        GoodsEvaluateEntity entity = JSON.parseObject(s, GoodsEvaluateEntity.class);
        if (entity != null) {
            int code = entity.getCode();
            if (code == 0) {
                List<GoodsEvaluateEntity.DataBean> datas = entity.getData();
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
            }
        } else {
            showToast("服务器异常");
        }

    }

    @Override
    public void onError(String msg) {
        showToast(msg);
    }
}
