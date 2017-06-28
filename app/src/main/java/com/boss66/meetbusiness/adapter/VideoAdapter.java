package com.boss66.meetbusiness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.util.ImageLoaderUtils;
import com.boss66.meetbusiness.util.UIUtils;
import com.boss66.meetbusiness.widget.CircleImageView;
import com.boss66.meetbusiness.widget.RoundImage;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Johnny on 2017/6/1.
 */
public class VideoAdapter extends BaseRecycleViewAdapter {
    private int widthScreen;
    private ImageLoader imageLoader;
//    private List<VideoEntity> videos;


    public VideoAdapter(Context context) {
        widthScreen = UIUtils.getScreenWidth(context) / 2;
        this.imageLoader = ImageLoaderUtils.createImageLoader(context);
    }


    @Override
    public VideoView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoView(view);
    }


    private void scaleSize(ImageView iv, int w, int h) {
//        int width =( w/widthScreen)*;
        int height = (w / widthScreen) * h;
        iv.getLayoutParams().width = widthScreen;
        iv.getLayoutParams().height = height;
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public void onBindItemHolder(RecyclerView.ViewHolder holder1, int position) {
        VideoView holder = (VideoView) holder1;
        VideoEntity entity = (VideoEntity) datas.get(position);
        if (entity != null) {
            holder.tvAuthor.setText(entity.getAuthor());
            holder.tvPraiseNum.setText(entity.getPraiseNum());
//            scaleSize(holder.ivBg, entity.getWidth(), entity.getHeight());
            imageLoader.displayImage(entity.getUrl(), holder.ivBg, ImageLoaderUtils.getDisplayImageOptions());
            imageLoader.displayImage(entity.getAuthorIcon(), holder.ivAvatar, ImageLoaderUtils.getDisplayImageOptions());
        }
    }

    public static class VideoView extends RecyclerView.ViewHolder {
        private RoundImage ivBg;
        private CircleImageView ivAvatar;
        private TextView tvAuthor;
        private TextView tvPraiseNum;

        public VideoView(View itemView) {
            super(itemView);
            ivBg = (RoundImage) itemView.findViewById(R.id.iv_bg);
            ivAvatar = (CircleImageView) itemView.findViewById(R.id.iv_author);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvPraiseNum = (TextView) itemView.findViewById(R.id.tv_praiseNum);
        }
    }
}
