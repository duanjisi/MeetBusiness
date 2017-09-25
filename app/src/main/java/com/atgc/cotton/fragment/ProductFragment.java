package com.atgc.cotton.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.production.other.OtherPlayerActivity;
import com.atgc.cotton.adapter.ProductAdapter;
import com.atgc.cotton.entity.HomeBaseData;
import com.atgc.cotton.entity.VideoEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.MyLikeRequest;
import com.atgc.cotton.http.request.MyProRequest;
import com.atgc.cotton.listener.ItemClickListener;
import com.paging.gridview.PagingGridView;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/6.
 */
public abstract class ProductFragment extends BaseFragment {
    private PagingGridView gridView;
    private ProductAdapter adapter;
    private static final String TAG = ProductFragment.class.getSimpleName();
    private boolean loadMore = true;
    private static final int PAGER_NUM = 8;
    private int pager = 1;
//    private LRecyclerView lRecyclerView;
//    private ProductionAdapter adapter;
//    private LRecyclerViewAdapter lRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        gridView = (PagingGridView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        adapter = new ProductAdapter(getActivity());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new itemClickListener());
        gridView.setHasMoreItems(true);
        gridView.setPagingableListener(new PagingGridView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (loadMore) {
                    requestMoreDatas();
                } else {
                    gridView.onFinishLoading(false, null);
                }
            }
        });
//        lRecyclerView = (LRecyclerView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
//        lRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        adapter = new ProductionAdapter(getContext(), new clickListener());
//        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
//        lRecyclerView.setFooterViewHint("加载中", "已经没数据", "网络不给力啊，点击再试一次吧");
//        lRecyclerView.setAdapter(lRecyclerViewAdapter);
//        lRecyclerView.setLoadMoreEnabled(true);
//        lRecyclerView.refreshComplete(8);
//        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        showToast("加载更多~", false);
//                        requestMoreDatas();
//                    }
//                }, 1000);
//            }
//        });
//        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
//        lRecyclerView.addItemDecoration(decoration);
        requestDatas();
    }

    private class itemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            VideoEntity videoEntity = (VideoEntity) adapterView.getItemAtPosition(i);
            if (videoEntity != null) {
                Intent intent = new Intent(getContext(), OtherPlayerActivity.class);
                intent.putExtra("obj", videoEntity);
//                startActivity(intent);
                startActivityForResult(intent, 102, null);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == getActivity().RESULT_OK) {
            if (getType() == 1) {
                requestDatas();
            }
        }
    }

    private class clickListener implements ItemClickListener {
        @Override
        public void onItemClick(View view, int position, VideoEntity video) {
            if (video != null) {

            }
        }

        @Override
        public void onAvatarClick(VideoEntity video) {

        }
    }


    private void requestDatas() {
        pager = 1;
        BaseDataRequest request = null;
        switch (getType()) {
            case 1:
                request = new MyProRequest(TAG, "" + pager, "" + PAGER_NUM);
                break;
            case 2:
                request = new MyLikeRequest(TAG, "" + pager, "" + PAGER_NUM);
                break;
        }
        request.send(new BaseDataRequest.RequestCallback<HomeBaseData>() {
            @Override
            public void onSuccess(HomeBaseData pojo) {
                cancelLoadingDialog();
                initData(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                if (msg.contains("no record")) {
                    gridView.onFinishLoading(false, null);
                    loadMore = false;
                    showToast("没有数据", true);
                } else {
                    showToast(msg, true);
                }
            }
        });
    }

    private void initData(HomeBaseData homeBaseData) {
        if (homeBaseData != null) {
            ArrayList<VideoEntity> videos = homeBaseData.getData();
            int size = videos.size();
            adapter.removeAllItems();
            if (videos != null && size != 0) {
//                if (size == PAGER_NUM) {
//                    lRecyclerView.setNoMore(false);
//                } else {
//                    lRecyclerView.setNoMore(true);
//                }
//                adapter.initDatas(videos);
                adapter.addMoreItems(videos);
            } else {
//                lRecyclerView.setNoMore(true);
                loadMore = false;
                gridView.onFinishLoading(false, null);
            }
        }
    }

    private void requestMoreDatas() {
        pager++;
        BaseDataRequest request = null;
        switch (getType()) {
            case 1:
                request = new MyProRequest(TAG, "" + pager, "" + PAGER_NUM);
                break;
            case 2:
                request = new MyLikeRequest(TAG, "" + pager, "" + PAGER_NUM);
                break;
        }
        request.send(new BaseDataRequest.RequestCallback<HomeBaseData>() {
            @Override
            public void onSuccess(HomeBaseData pojo) {
                cancelLoadingDialog();
                bindData(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                if (msg.contains("no record")) {
                    gridView.onFinishLoading(false, null);
                    loadMore = false;
                    showToast("没有数据", true);
                } else {
                    showToast(msg, true);
                }
            }
        });
    }

    private void bindData(HomeBaseData homeBaseData) {
        if (homeBaseData != null) {
            ArrayList<VideoEntity> videos = homeBaseData.getData();
            int size = videos.size();
            if (videos != null && size != 0) {
//                if (size == PAGER_NUM) {
//                    lRecyclerView.setNoMore(false);
//                } else {
//                    lRecyclerView.setNoMore(true);
//                    showToast("加载完成!", true);
//                }
//                adapter.addDatas(videos);
                gridView.onFinishLoading(true, videos);
            } else {
//                lRecyclerView.setNoMore(true);
//                showToast("加载完成!", true);
                gridView.onFinishLoading(false, null);
                loadMore = false;
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

    protected abstract int getType();
}
