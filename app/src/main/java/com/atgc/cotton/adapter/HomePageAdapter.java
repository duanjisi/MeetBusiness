package com.atgc.cotton.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.entity.VideoEntity;
import com.atgc.cotton.listener.ItemClickListener;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.CircleImageView;
import com.atgc.cotton.widget.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 2017/6/1.
 */
public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.VideoView> {
    private int widthScreen;
    private ImageLoader imageLoader;
    private List<VideoEntity> videos;
    private ItemClickListener clickListener;

    public HomePageAdapter(Context context, List<VideoEntity> videos) {
        this.videos = videos;
        widthScreen = UIUtils.getScreenWidth(context) / 2;
        this.imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    public HomePageAdapter(Context context, ItemClickListener listener) {
        if (videos == null) {
            videos = new ArrayList<>();
        }
        this.clickListener = listener;
        widthScreen = UIUtils.getScreenWidth(context) / 2;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
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
            holder.tvAuthor.setText(entity.getUserName());
            holder.tvPraiseNum.setText(entity.getLikeCount());
//            scaleSize(holder.ivBg, entity.getWidth(), entity.getHeight());
//            imageLoader.displayImage(entity.getUrl(), holder.ivBg, ImageLoaderUtils.getDisplayImageOptions());
            imageLoader.displayImage(entity.getAvatar(), holder.ivAvatar, ImageLoaderUtils.getDisplayImageOptions());
            scalLoadImageVideo(holder, entity.getMediaPath());
        }
    }

    private String[] getSize(String url) {
        String str = url.substring(url.indexOf("-"), url.length());
        String size = str.substring(str.indexOf("-") + 1, str.indexOf("."));
        return size.split("x");
    }

    private void scalLoadImageVideo(VideoView holder, String videoPath) {
        String imageUrl = "";
        if (videoPath.contains(".mp4")) {
            imageUrl = videoPath.replace(".mp4", ".jpg");
        } else if (videoPath.contains(".mov")) {
            imageUrl = videoPath.replace(".mov", ".jpg");
        }
        String[] size = getSize(imageUrl);
        int width = Integer.parseInt(size[0]);
        int height = Integer.parseInt(size[1]);
        scaleSize(holder.ivBg, width, height);
        imageLoader.displayImage(imageUrl, holder.ivBg, ImageLoaderUtils.getDisplayImageOptions());
    }

    private void scaleSize(RoundImageView iv, int w, int h) {
        int height = (widthScreen * h) / w;
        Log.i("info", "=============width:" + widthScreen + "\n" + "height:" + height);
        iv.getLayoutParams().width = widthScreen;
        iv.getLayoutParams().height = height;
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.size() : 0;
    }


    public static class VideoView extends RecyclerView.ViewHolder {
        private RoundImageView ivBg;
        private CircleImageView ivAvatar;
        private TextView tvAuthor;
        private TextView tvPraiseNum;

        public VideoView(View itemView) {
            super(itemView);
            ivBg = (RoundImageView) itemView.findViewById(R.id.iv_bg);
            ivAvatar = (CircleImageView) itemView.findViewById(R.id.iv_author);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvPraiseNum = (TextView) itemView.findViewById(R.id.tv_praiseNum);
        }
    }
}
