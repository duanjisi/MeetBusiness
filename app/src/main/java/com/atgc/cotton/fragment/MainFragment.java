package com.atgc.cotton.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.production.other.OtherPlayerActivity;
import com.atgc.cotton.activity.production.other.OtherProActivity;
import com.atgc.cotton.adapter.StaggeredRecycleViewAdapter;
import com.atgc.cotton.entity.HomeBaseData;
import com.atgc.cotton.entity.VideoEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.FocusRequest;
import com.atgc.cotton.http.request.HomePagerRequest;
import com.atgc.cotton.http.request.NearRequest;
import com.atgc.cotton.listener.ItemClickListener;
import com.atgc.cotton.listener.PermissionListener;
import com.atgc.cotton.util.PermissonUtil.PermissionUtil;
import com.atgc.cotton.util.ToastUtil;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 2017/5/31.
 */
public abstract class MainFragment extends BaseFragment implements AMapLocationListener {
    private static final String TAG = MainFragment.class.getSimpleName();
    public static final int TYPE_FOCUS = 1000;
    public static final int TYPE_DISCOVER = 1010;
    public static final int TYPE_HOT = 1011;
    public static final int TYPE_NEAR = 1012;
    private static final int PAGER_NUM = 8;
    private int pager = 1;
    private List<VideoEntity> allList;
    private PullLoadMoreRecyclerView pullLoadMoreRecyclerView;
    private StaggeredRecycleViewAdapter mRecyclerViewAdapter;
    //    private SwipeRefreshLayout mRefreshLayout;
//    private RecyclerView lRecyclerView;
//    private PagerAdapter adapter;
//    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private PermissionListener permissionListener;

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isInited = false;
    private long curTime = 0;
    private String lat, lng;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_main, null);
//        return inflater.inflate(R.layout.activity_test_main, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
//        if (getType() == TYPE_NEAR) {
//            getPermission();
//        } else {
//            requestDatas();
//        }
        requestDatas();
    }


    private void initViews(View view) {

//        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe_refresh);
//        lRecyclerView = (LRecyclerView) view.findViewById(R.id.recyclerview);
//        lRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

//        adapter = new PagerAdapter(getContext(), new clickListener());
//        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        pullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
        //mPullLoadMoreRecyclerView.setRefreshing(true);
        pullLoadMoreRecyclerView.setStaggeredGridLayout(2);
        mRecyclerViewAdapter = new StaggeredRecycleViewAdapter(getActivity(), new clickListener(), getType());
        pullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener());
//        lRecyclerView.setFooterViewHint("加载中", "已经没数据", "网络不给力啊，点击再试一次吧");
//        lRecyclerView.setAdapter(lRecyclerViewAdapter);
//        lRecyclerView.setLoadMoreEnabled(true);
//        lRecyclerView.refreshComplete(8);
//        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        lRecyclerView.refreshComplete(PAGER_NUM);
//                        if (getType() != TYPE_NEAR) {
//                            showToast("刷新完成", false);
//                            requestDatas();
//                        }
//                    }
//                }, 1000);
//            }
//        });

//        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (getType() != TYPE_NEAR) {
//                            requestMoreDatas();
//                        } else {
//                            lRecyclerView.setNoMore(false);
//                        }
//                    }
//                }, 1000);
//            }
//        });
    }

    class PullLoadMoreListener implements PullLoadMoreRecyclerView.PullLoadMoreListener {
        @Override
        public void onRefresh() {
            if (getType() != TYPE_NEAR) {
//                showToast("刷新完成", false);
                requestDatas();
            } else {
                if (!TextUtils.isEmpty(lng) && !TextUtils.isEmpty(lat)) {
                    requestNear(lng, lat);
                }
            }
        }

        @Override
        public void onLoadMore() {
            if (getType() != TYPE_NEAR) {
                requestMoreDatas();
            }
        }
    }

    private class clickListener implements ItemClickListener {
        @Override
        public void onItemClick(View view, int position, VideoEntity video) {
            if (video != null) {
                Intent intent = new Intent(getContext(), OtherPlayerActivity.class);
                intent.putExtra("obj", video);
                startActivity(intent);
            }
        }

        @Override
        public void onAvatarClick(VideoEntity video) {
            if (video != null) {
                Intent intent = new Intent(getContext(), OtherProActivity.class);
                intent.putExtra("obj", video);
                startActivity(intent);
            }
        }
    }


    protected abstract int getType();

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
        pullLoadMoreRecyclerView.setHasMore(true);
        BaseDataRequest request = null;
        pager = 1;
        switch (getType()) {
            case TYPE_FOCUS:
                request = new FocusRequest(TAG, "" + pager, "" + PAGER_NUM);
                break;
            case TYPE_DISCOVER:
                request = new HomePagerRequest(TAG, "" + pager, "" + PAGER_NUM, "addtime");
                break;
            case TYPE_HOT:
                request = new HomePagerRequest(TAG, "" + pager, "" + PAGER_NUM, "bcount");
                break;
        }
        showLoadingDialog();
        request.send(new BaseDataRequest.RequestCallback<HomeBaseData>() {
            @Override
            public void onSuccess(HomeBaseData pojo) {
                cancelLoadingDialog();
                initData(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                if (msg.equals("no record")) {
//                    showToast("数据加载完成!", true);
//                    lRecyclerView.setNoMore(false);
                    pullLoadMoreRecyclerView.setHasMore(false);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        }
                    }, 1000);
                }
            }
        });
    }

    private void initData(HomeBaseData homeBaseData) {
        if (homeBaseData != null) {
            final ArrayList<VideoEntity> videos = homeBaseData.getData();
            int size = videos.size();
            if (videos != null && size != 0) {
                mRecyclerViewAdapter.getDataList().clear();
                if (size == PAGER_NUM) {
                    pager++;
                }
//                else {
//                    pullLoadMoreRecyclerView.setHasMore(false);
//                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerViewAdapter.addAllData(videos);
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 1000);
            } else {
                pullLoadMoreRecyclerView.setHasMore(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 1000);
            }
        }
    }
//    private void initData(HomeBaseData homeBaseData) {
//        if (homeBaseData != null) {
//            ArrayList<VideoEntity> videos = homeBaseData.getData();
//            int size = videos.size();
//            if (videos != null && size != 0) {
//                if (allList == null) {
//                    allList = new ArrayList<>();
//                }
//                if (size == PAGER_NUM) {
//                    pager++;
////                    lRecyclerView.setNoMore(false);
//                } else {
//                    pager++;
////                    lRecyclerView.setNoMore(true);
//                }
//                if (allList.size() > 0) {
//                    allList.clear();
//                }
//                allList.addAll(videos);
//                adapter.setDatas(videos);
////                adapter.initDatas(videos);
//            } else {
////                lRecyclerView.setNoMore(true);
//            }
//        }
//    }

    private void requestMoreDatas() {
        BaseDataRequest request = null;
        switch (getType()) {
            case TYPE_FOCUS:
                request = new FocusRequest(TAG, "" + pager, "" + PAGER_NUM);
                break;
            case TYPE_DISCOVER:
                request = new HomePagerRequest(TAG, "" + pager, "" + PAGER_NUM, "addtime");
                break;
            case TYPE_HOT:
                request = new HomePagerRequest(TAG, "" + pager, "" + PAGER_NUM, "bcount");
                break;
        }
//        showLoadingDialog();
        request.send(new BaseDataRequest.RequestCallback<HomeBaseData>() {
            @Override
            public void onSuccess(HomeBaseData pojo) {
//                cancelLoadingDialog();
                bindData(pojo);
            }

            @Override
            public void onFailure(String msg) {
//                cancelLoadingDialog();
                if (msg.equals("no record")) {
//                    showToast("数据加载完成!", true);
//                    lRecyclerView.setNoMore(false);
                    pullLoadMoreRecyclerView.setHasMore(false);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        }
                    }, 1000);
                } else {
                    showToast(msg, true);
                }
            }
        });
    }

    private void bindData(HomeBaseData homeBaseData) {
        if (homeBaseData != null) {
            final ArrayList<VideoEntity> videos = homeBaseData.getData();
            int size = videos.size();
            if (videos != null && size != 0) {
                if (size == PAGER_NUM) {
                    pager++;
                }
//                else {
//                    pullLoadMoreRecyclerView.setHasMore(false);
//                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerViewAdapter.addAllData(videos);
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 1000);
            } else {
                pullLoadMoreRecyclerView.setHasMore(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 1000);
            }
        }
    }
//    private void bindData(HomeBaseData homeBaseData) {
//        if (homeBaseData != null) {
//            ArrayList<VideoEntity> videos = homeBaseData.getData();
//            int size = videos.size();
//            if (videos != null && size != 0) {
//                if (allList == null) {
//                    allList = new ArrayList<>();
//                }
//                if (size == PAGER_NUM) {
////                    lRecyclerView.setNoMore(false);
//                    pager++;
//                } else {
////                    lRecyclerView.setNoMore(true);
////                    showToast("加载完成!", true);
//                }
//                allList.addAll(videos);
//                adapter.setDatas(allList);
////                adapter.addDatas(videos);
//            } else {
////                lRecyclerView.setNoMore(true);
////                showToast("加载完成!", true);
//            }
//        }
//    }

    private void getPermission() {
        permissionListener = new PermissionListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, permissionListener);
            }

            @Override
            public void onRequestPermissionSuccess() {
                setUp();
            }

            @Override
            public void onRequestPermissionError() {
                ToastUtil.showShort(getActivity(), "请给予定位权限");
            }
        };
        PermissionUtil
                .with(this)
                .permissions(
                        PermissionUtil.PERMISSIONS_GROUP_LOACATION //定位授权
                ).request(permissionListener);
    }

    private void setUp() {
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(getActivity());
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            //设置定位参数
            mLocationOption.setInterval(30000);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            lng = String.valueOf(aMapLocation.getLongitude());
            lat = String.valueOf(aMapLocation.getLatitude());
            if (!isInited) {
                requestNear(lng, lat);
            }
//            if (curTime == 0) {
//                curTime = System.currentTimeMillis();
//                requestNear(lng, lat);
//            } else {
//                long time = System.currentTimeMillis();
//                long dif = time - curTime;
//                if (dif / 1000 > 120) {
//                    curTime = time;
//                    requestNear(lng, lat);
//                }
//            }
        }
    }


    private void requestNear(String lgt, String lat) {
        Log.i("info", "====================" + "lgt:" + lgt + "\n" + "lat:" + lat);
        NearRequest request = new NearRequest(TAG, lgt, lat);
        showLoadingDialog();
        request.send(new BaseDataRequest.RequestCallback<HomeBaseData>() {
            @Override
            public void onSuccess(HomeBaseData pojo) {
                cancelLoadingDialog();
                initNear(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void initNear(HomeBaseData homeBaseData) {
        if (homeBaseData != null) {
            isInited = true;
            final ArrayList<VideoEntity> videos = homeBaseData.getData();
            if (videos != null && videos.size() != 0) {
                mRecyclerViewAdapter.getDataList().clear();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerViewAdapter.addAllData(videos);
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 1000);
            } else {
                pullLoadMoreRecyclerView.setHasMore(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 1000);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, permissionListener);
    }
}
