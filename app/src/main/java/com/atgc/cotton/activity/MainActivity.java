package com.atgc.cotton.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.adapter.HomePageAdapter;
import com.atgc.cotton.entity.HomeBaseData;
import com.atgc.cotton.entity.VideoEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.HomePagerRequest;
import com.atgc.cotton.listener.ItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/7.
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PAGER_NUM = 8;
    private int pager = 1;
    private ImageView iv_lock;
    private LRecyclerView lRecyclerView;
    private HomePageAdapter adapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        iv_lock = (ImageView) findViewById(R.id.iv_lock);
        lRecyclerView = (LRecyclerView) findViewById(R.id.recyclerview);
        lRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new HomePageAdapter(context, new clickListener());
        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerView.setFooterViewHint("加载中", "已经没数据", "网络不给力啊，点击再试一次吧");
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        lRecyclerView.setLoadMoreEnabled(true);
        lRecyclerView.refreshComplete(8);
        iv_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(HomePagerActivity.class);
                finish();
            }
        });
        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showToast("刷新完成", false);
                        lRecyclerView.refreshComplete(PAGER_NUM);
                        requestDatas();
                    }
                }, 1000);
            }
        });
        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showToast("加载更多~", false);
                        requestMoreDatas();
                    }
                }, 1000);
            }
        });
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        lRecyclerView.addItemDecoration(decoration);
        requestDatas();
    }

    private class clickListener implements ItemClickListener {
        @Override
        public void onItemClick(View view, int position, VideoEntity video) {
            if (video != null) {

            }
        }
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }

    private void requestDatas() {
        showLoadingDialog();
        pager = 1;
        HomePagerRequest request = new HomePagerRequest(TAG, "" + pager, "" + PAGER_NUM);
        request.send(new BaseDataRequest.RequestCallback<HomeBaseData>() {
            @Override
            public void onSuccess(HomeBaseData pojo) {
                cancelLoadingDialog();
                initData(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void initData(HomeBaseData homeBaseData) {
        if (homeBaseData != null) {
            ArrayList<VideoEntity> videos = homeBaseData.getData();
            int size = videos.size();
            if (videos != null && size != 0) {
                if (size == PAGER_NUM) {
                    lRecyclerView.setNoMore(false);
                } else {
                    lRecyclerView.setNoMore(true);
//                    showToast("加载完成!", true);
                }
                adapter.initDatas(videos);
            } else {
                lRecyclerView.setNoMore(true);
//                showToast("加载完成!", true);
            }
        }
    }

    private void requestMoreDatas() {
        pager++;
        showLoadingDialog();
        HomePagerRequest request = new HomePagerRequest(TAG, "" + pager, "" + PAGER_NUM);
        request.send(new BaseDataRequest.RequestCallback<HomeBaseData>() {
            @Override
            public void onSuccess(HomeBaseData pojo) {
                cancelLoadingDialog();
                bindData(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void bindData(HomeBaseData homeBaseData) {
        if (homeBaseData != null) {
            ArrayList<VideoEntity> videos = homeBaseData.getData();
            int size = videos.size();
            if (videos != null && size != 0) {
                if (size == PAGER_NUM) {
                    lRecyclerView.setNoMore(false);
                } else {
                    lRecyclerView.setNoMore(true);
                    showToast("加载完成!", true);
                }
                adapter.addDatas(videos);
            } else {
                lRecyclerView.setNoMore(true);
                showToast("加载完成!", true);
            }
        }
    }
}
