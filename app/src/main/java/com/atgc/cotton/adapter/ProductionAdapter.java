package com.atgc.cotton.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.atgc.cotton.R;
import com.atgc.cotton.entity.VideoEntity;
import com.atgc.cotton.listener.ItemClickListener;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 2017/6/1.
 */
public class ProductionAdapter extends RecyclerView.Adapter<ProductionAdapter.VideoView> {
    private int widthScreen;
    private ImageLoader imageLoader;
    private List<VideoEntity> videos;
    private ItemClickListener clickListener;
    private float mImageHeight;

    public ProductionAdapter(Context context, List<VideoEntity> videos) {
        this.videos = videos;
        widthScreen = UIUtils.getScreenWidth(context) / 2;
        this.imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    public ProductionAdapter(Context context, ItemClickListener listener) {
        if (videos == null) {
            videos = new ArrayList<>();
        }
        this.clickListener = listener;
        widthScreen = UIUtils.getScreenWidth(context) / 2;
        mImageHeight = (UIUtils.getScreenWidth(context) - UIUtils.dip2px(context, 30)) / 3;
        this.imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    public void initDatas(List<VideoEntity> list) {
        videos.clear();
        videos.addAll(list);
        notifyDataSetChanged();
    }

    public void addDatas(List<VideoEntity> list) {
        videos.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public VideoView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false);
//        ImageView imageView = new ImageView(parent.getContext());
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
//        imageView.getLayoutParams().width = (int) mImageHeight;
//        imageView.getLayoutParams().height = (int) mImageHeight;
        return new VideoView(view);
    }

    @Override
    public void onBindViewHolder(final VideoView holder, final int position) {
        final VideoEntity entity = (VideoEntity) videos.get(position);
        if (entity != null) {
            if (clickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onItemClick(holder.itemView, position, entity);
                    }
                });
            }
            holder.imageView.getLayoutParams().width = (int) mImageHeight;
            holder.imageView.getLayoutParams().height = (int) mImageHeight;
            imageLoader.displayImage(entity.getAvatar(), holder.imageView, ImageLoaderUtils.getDisplayImageOptions());
        }
    }


    @Override
    public int getItemCount() {
        return videos != null ? videos.size() : 0;
    }


    public static class VideoView extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public VideoView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }
}
