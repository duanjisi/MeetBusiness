package com.atgc.cotton.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atgc.cotton.adapter.HomePageAdapter;
import com.atgc.cotton.listener.ItemClickListener;
import com.atgc.cotton.R;
import com.atgc.cotton.entity.Product;
import com.atgc.cotton.entity.VideoEntity;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 2017/5/31.
 */
public abstract class MainFragment extends BaseFragment {
    private LRecyclerView lRecyclerView;
    //    private VideoAdapter adapter;
    private HomePageAdapter adapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<Product> productList;
    private List<VideoEntity> videos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_main, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }


    private void initViews(View view) {
        lRecyclerView = (LRecyclerView) view.findViewById(R.id.recyclerview);
        lRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        adapter = new VideoAdapter(getContext());
//        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
//        initData();
//        RecycleItemClickListener clickListener = new RecycleItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        };
//        MasonryAdapter adapter = new MasonryAdapter(productList, clickListener);
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
                        showToast("刷新完成", false);
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
                    }
                }, 1000);
            }
        });

        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        lRecyclerView.addItemDecoration(decoration);
        initDatas();
        adapter.initDatas(videos);
    }


    private class clickListener implements ItemClickListener {
        @Override
        public void onItemClick(View view, int position, VideoEntity video) {
            if (video != null) {
                showToast(video.getAuthor() + "创建该视频!", true);
            }
        }
    }

    private void initData() {
        productList = new ArrayList<Product>();
        Product p1 = new Product(R.drawable.p1, "我是照片1");
        productList.add(p1);
        Product p2 = new Product(R.drawable.p2, "我是照片2");
        productList.add(p2);
        Product p3 = new Product(R.drawable.p3, "我是照片3");
        productList.add(p3);
        Product p4 = new Product(R.drawable.p4, "我是照片4");
        productList.add(p4);
        Product p5 = new Product(R.drawable.p5, "我是照片5");
        productList.add(p5);
        Product p6 = new Product(R.drawable.p6, "我是照片6");
        productList.add(p6);
        Product p7 = new Product(R.drawable.p2, "我是照片7");
        productList.add(p7);
        Product p8 = new Product(R.drawable.p1, "我是照片8");
        productList.add(p8);
        Product p9 = new Product(R.drawable.p4, "我是照片9");
        productList.add(p9);
        Product p10 = new Product(R.drawable.p6, "我是照片10");
        productList.add(p10);
        Product p11 = new Product(R.drawable.p3, "我是照片11");
        productList.add(p11);

    }

    private void initDatas() {
        VideoEntity entity0 = new VideoEntity();
        entity0.setAuthor("葫芦娃");
        entity0.setAuthorIcon("http://img3.imgtn.bdimg.com/it/u=604196165,4105066670&fm=23&gp=0.jpg");
        entity0.setUrl("http://img2.imgtn.bdimg.com/it/u=861196094,950292534&fm=23&gp=0.jpg");
        entity0.setHeight(200);
        entity0.setWidth(100);


        VideoEntity entity1 = new VideoEntity();
        entity1.setAuthor("逗逼");
        entity1.setAuthorIcon("http://img2.imgtn.bdimg.com/it/u=2493647049,1920630849&fm=117&gp=0.jpg");
        entity1.setUrl("http://img0.imgtn.bdimg.com/it/u=2223768434,3875773028&fm=23&gp=0.jpg");
        entity1.setHeight(150);
        entity1.setWidth(90);

        VideoEntity entity2 = new VideoEntity();
        entity2.setAuthor("萌娃");
        entity2.setAuthorIcon("http://img0.imgtn.bdimg.com/it/u=388444222,2315692232&fm=23&gp=0.jpg");
        entity2.setUrl("http://img1.imgtn.bdimg.com/it/u=1202313702,932187721&fm=23&gp=0.jpg");
        entity2.setHeight(200);
        entity2.setWidth(150);


        VideoEntity entity3 = new VideoEntity();
        entity3.setAuthor("萌娃");
        entity3.setAuthorIcon("http://img0.imgtn.bdimg.com/it/u=388444222,2315692232&fm=23&gp=0.jpg");
        entity3.setUrl("http://img1.imgtn.bdimg.com/it/u=1202313702,932187721&fm=23&gp=0.jpg");
        entity3.setHeight(200);
        entity3.setWidth(150);

        VideoEntity entity4 = new VideoEntity();
        entity4.setAuthor("萌娃");
        entity4.setAuthorIcon("http://img0.imgtn.bdimg.com/it/u=388444222,2315692232&fm=23&gp=0.jpg");
        entity4.setUrl("http://img1.imgtn.bdimg.com/it/u=1202313702,932187721&fm=23&gp=0.jpg");
        entity4.setHeight(200);
        entity4.setWidth(150);


        VideoEntity entity5 = new VideoEntity();
        entity5.setAuthor("萌娃");
        entity5.setAuthorIcon("http://img0.imgtn.bdimg.com/it/u=388444222,2315692232&fm=23&gp=0.jpg");
        entity5.setUrl("http://img1.imgtn.bdimg.com/it/u=1202313702,932187721&fm=23&gp=0.jpg");
        entity5.setHeight(200);
        entity5.setWidth(150);


        VideoEntity entity6 = new VideoEntity();
        entity6.setAuthor("狗娃");
        entity6.setAuthorIcon("http://img0.imgtn.bdimg.com/it/u=1380960038,2176872502&fm=23&gp=0.jpg");
        entity6.setUrl("http://img1.imgtn.bdimg.com/it/u=2798388099,1646685914&fm=23&gp=0.jpg");
        entity6.setHeight(148);
        entity6.setWidth(80);

        VideoEntity entity7 = new VideoEntity();
        entity7.setAuthor("小蜜蜂");
        entity7.setAuthorIcon("http://img5.imgtn.bdimg.com/it/u=99226227,3196889264&fm=23&gp=0.jpg");
        entity7.setUrl("http://img4.imgtn.bdimg.com/it/u=1400029800,1056498640&fm=23&gp=0.jpg");
        entity7.setHeight(180);
        entity7.setWidth(118);


        VideoEntity entity8 = new VideoEntity();
        entity8.setAuthor("摩托车");
        entity8.setAuthorIcon("http://img1.imgtn.bdimg.com/it/u=1224591117,133457104&fm=23&gp=0.jpg");
        entity8.setUrl("http://img1.imgtn.bdimg.com/it/u=4166527390,3721053277&fm=23&gp=0.jpg");
        entity8.setHeight(40);
        entity8.setWidth(90);


        VideoEntity entity9 = new VideoEntity();
        entity9.setAuthor("小可爱");
        entity9.setAuthorIcon("http://img1.imgtn.bdimg.com/it/u=2706173994,1924386605&fm=23&gp=0.jpg");
        entity9.setUrl("http://img0.imgtn.bdimg.com/it/u=2196140019,2323276639&fm=23&gp=0.jpg");
        entity9.setHeight(100);
        entity9.setWidth(60);


        VideoEntity entity10 = new VideoEntity();
        entity10.setAuthor("小可爱");
        entity10.setAuthorIcon("http://img1.imgtn.bdimg.com/it/u=2706173994,1924386605&fm=23&gp=0.jpg");
        entity10.setUrl("http://img0.imgtn.bdimg.com/it/u=2196140019,2323276639&fm=23&gp=0.jpg");
        entity10.setHeight(100);
        entity10.setWidth(60);

        VideoEntity entity11 = new VideoEntity();
        entity11.setAuthor("摩托车");
        entity11.setAuthorIcon("http://img1.imgtn.bdimg.com/it/u=1224591117,133457104&fm=23&gp=0.jpg");
        entity11.setUrl("http://img1.imgtn.bdimg.com/it/u=4166527390,3721053277&fm=23&gp=0.jpg");
        entity11.setHeight(40);
        entity11.setWidth(90);


        VideoEntity entity12 = new VideoEntity();
        entity12.setAuthor("摩托车");
        entity12.setAuthorIcon("http://img1.imgtn.bdimg.com/it/u=1224591117,133457104&fm=23&gp=0.jpg");
        entity12.setUrl("http://img1.imgtn.bdimg.com/it/u=4166527390,3721053277&fm=23&gp=0.jpg");
        entity12.setHeight(40);
        entity12.setWidth(90);


        VideoEntity entity13 = new VideoEntity();
        entity13.setAuthor("摩托车");
        entity13.setAuthorIcon("http://img1.imgtn.bdimg.com/it/u=1224591117,133457104&fm=23&gp=0.jpg");
        entity13.setUrl("http://img1.imgtn.bdimg.com/it/u=4166527390,3721053277&fm=23&gp=0.jpg");
        entity13.setHeight(40);
        entity13.setWidth(90);


        VideoEntity entity14 = new VideoEntity();
        entity14.setAuthor("摩托车");
        entity14.setAuthorIcon("http://img1.imgtn.bdimg.com/it/u=1224591117,133457104&fm=23&gp=0.jpg");
        entity14.setUrl("http://img1.imgtn.bdimg.com/it/u=4166527390,3721053277&fm=23&gp=0.jpg");
        entity14.setHeight(40);
        entity14.setWidth(90);


        VideoEntity entity15 = new VideoEntity();
        entity15.setAuthor("摩托车");
        entity15.setAuthorIcon("http://img1.imgtn.bdimg.com/it/u=1224591117,133457104&fm=23&gp=0.jpg");
        entity15.setUrl("http://img1.imgtn.bdimg.com/it/u=4166527390,3721053277&fm=23&gp=0.jpg");
        entity15.setHeight(40);
        entity15.setWidth(90);


        VideoEntity entity16 = new VideoEntity();
        entity16.setAuthor("摩托车");
        entity16.setAuthorIcon("http://img1.imgtn.bdimg.com/it/u=1224591117,133457104&fm=23&gp=0.jpg");
        entity16.setUrl("http://img1.imgtn.bdimg.com/it/u=4166527390,3721053277&fm=23&gp=0.jpg");
        entity16.setHeight(40);
        entity16.setWidth(90);

        VideoEntity entity17 = new VideoEntity();
        entity17.setAuthor("摩托车");
        entity17.setAuthorIcon("http://img1.imgtn.bdimg.com/it/u=1224591117,133457104&fm=23&gp=0.jpg");
        entity17.setUrl("http://img1.imgtn.bdimg.com/it/u=4166527390,3721053277&fm=23&gp=0.jpg");
        entity17.setHeight(40);
        entity17.setWidth(90);

        VideoEntity entity18 = new VideoEntity();
        entity18.setAuthor("摩托车");
        entity18.setAuthorIcon("http://img1.imgtn.bdimg.com/it/u=1224591117,133457104&fm=23&gp=0.jpg");
        entity18.setUrl("http://img1.imgtn.bdimg.com/it/u=4166527390,3721053277&fm=23&gp=0.jpg");
        entity18.setHeight(40);
        entity18.setWidth(90);

        videos = new ArrayList<>();
        videos.add(entity0);
        videos.add(entity1);
        videos.add(entity2);
        videos.add(entity3);
        videos.add(entity4);
        videos.add(entity5);
        videos.add(entity6);
        videos.add(entity7);
        videos.add(entity8);
        videos.add(entity9);
        videos.add(entity10);
        videos.add(entity11);
        videos.add(entity12);
        videos.add(entity13);
        videos.add(entity14);
        videos.add(entity15);
        videos.add(entity16);
        videos.add(entity17);
        videos.add(entity18);
//        adapter.setDatas(list);
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
}
