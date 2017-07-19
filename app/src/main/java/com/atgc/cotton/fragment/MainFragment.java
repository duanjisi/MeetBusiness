package com.atgc.cotton.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.atgc.cotton.adapter.HomePageAdapter;
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
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/5/31.
 */
public abstract class MainFragment extends BaseFragment implements AMapLocationListener {
    private static final String TAG = MainFragment.class.getSimpleName();
    public static final int TYPE_FOCUS = 1000;
    public static final int TYPE_DISCOVER = 1010;
    public static final int TYPE_NEAR = 1011;
    private static final int PAGER_NUM = 8;
    private int pager = 1;
    private LRecyclerView lRecyclerView;
    private HomePageAdapter adapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private PermissionListener permissionListener;

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private long curTime = 0;
    private String lat, lng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_main, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        if (getType() == TYPE_NEAR) {
            getPermission();
        } else {
            requestDatas();
        }
    }


    private void initViews(View view) {
        lRecyclerView = (LRecyclerView) view.findViewById(R.id.recyclerview);
        lRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new HomePageAdapter(getContext(), new clickListener());
        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerView.setFooterViewHint("加载中", "已经没数据", "网络不给力啊，点击再试一次吧");
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        lRecyclerView.setLoadMoreEnabled(true);
        lRecyclerView.refreshComplete(8);
        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getType() != TYPE_NEAR) {
                            showToast("刷新完成", false);
                            lRecyclerView.refreshComplete(PAGER_NUM);
                            requestDatas();
                        }
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
                        if (getType() != TYPE_NEAR) {
//                            showToast("加载更多~", false);
                            requestMoreDatas();
                        }
                    }
                }, 1000);
            }
        });

        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        lRecyclerView.addItemDecoration(decoration);
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
        BaseDataRequest request = null;
        switch (getType()) {
            case TYPE_FOCUS:
                request = new FocusRequest(TAG, "" + pager, "" + PAGER_NUM);
                break;
            case TYPE_DISCOVER:
                request = new HomePagerRequest(TAG, "" + pager, "" + PAGER_NUM);
                break;
        }
        showLoadingDialog();
        pager = 1;
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
                    showToast("数据加载完成!", true);
                    lRecyclerView.setNoMore(false);
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
        BaseDataRequest request = null;
        switch (getType()) {
            case TYPE_FOCUS:
                request = new FocusRequest(TAG, "" + pager, "" + PAGER_NUM);
                break;
            case TYPE_DISCOVER:
                request = new HomePagerRequest(TAG, "" + pager, "" + PAGER_NUM);
                break;
        }
        pager++;
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
                    showToast("数据加载完成!", true);
                    lRecyclerView.setNoMore(false);
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
            if (curTime == 0) {
                curTime = System.currentTimeMillis();
                requestNear(lng, lat);
            } else {
                long time = System.currentTimeMillis();
                long dif = time - curTime;
                if (dif / 1000 > 120) {
                    curTime = time;
                    requestNear(lng, lat);
                }
            }
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
            ArrayList<VideoEntity> videos = homeBaseData.getData();
            if (videos != null && videos.size() != 0) {
                adapter.initDatas(videos);
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
