package com.atgc.cotton.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.entity.VideoEntity;
import com.atgc.cotton.listener.ItemClickListener;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.TimeUtil;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.CircleImageView;
import com.atgc.cotton.widget.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuXiaolong on 2015/9/14.
 */
public class StaggeredRecycleViewAdapter extends RecyclerView.Adapter<StaggeredRecycleViewAdapter.ViewHolder> {
    public static final int TYPE_FOCUS = 1000;
    public static final int TYPE_DISCOVER = 1010;
    public static final int TYPE_HOT = 1011;
    public static final int TYPE_NEAR = 1012;
    private Context mContext;
    private List<VideoEntity> dataList;

    public List<VideoEntity> getDataList() {
        return dataList;
    }

    private int widthScreen;
    private ImageLoader imageLoader;
    private ItemClickListener clickListener;
    private Resources resources;
    private int mType;

    public StaggeredRecycleViewAdapter(Context context, ItemClickListener listener, int type) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        mContext = context;
        this.clickListener = listener;
        this.mType = type;
        widthScreen = UIUtils.getScreenWidth(context) / 2;
        this.imageLoader = ImageLoaderUtils.createImageLoader(context);
        resources = context.getResources();
    }

    public void addAllData(List<VideoEntity> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RoundImageView ivBg;
        private CircleImageView ivAvatar;
        private TextView tvAuthor;
        private TextView tvPraiseNum;
        private TextView tvTime;
        private TextView tvDistance;

        public ViewHolder(View itemView) {
            super(itemView);
            ivBg = (RoundImageView) itemView.findViewById(R.id.iv_bg);
            ivAvatar = (CircleImageView) itemView.findViewById(R.id.iv_author);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvPraiseNum = (TextView) itemView.findViewById(R.id.tv_praiseNum);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final VideoEntity entity = (VideoEntity) dataList.get(position);
        if (entity != null) {
            final ViewHolder holder = (ViewHolder) viewHolder;
            holder.tvAuthor.setText(entity.getUserName());
            String sex = entity.getSex();
            Drawable nav_up = null;
            if (sex.equals("1")) {
                nav_up = resources.getDrawable(R.drawable.man);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            } else if (sex.equals("2")) {
                nav_up = resources.getDrawable(R.drawable.lady);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            } else {
                nav_up = resources.getDrawable(R.drawable.lady);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            }
            holder.tvAuthor.setCompoundDrawables(null, null, nav_up, null);
            switch (mType) {
                case TYPE_FOCUS:
                    UIUtils.showView(holder.tvTime);
                    UIUtils.hindView(holder.tvDistance);
                    UIUtils.hindView(holder.tvPraiseNum);
                    break;
                case TYPE_DISCOVER:
                    UIUtils.hindView(holder.tvTime);
                    UIUtils.hindView(holder.tvDistance);
                    UIUtils.showView(holder.tvPraiseNum);
                    break;
                case TYPE_HOT:
                    UIUtils.hindView(holder.tvTime);
                    UIUtils.hindView(holder.tvDistance);
                    UIUtils.showView(holder.tvPraiseNum);
                    break;
                case TYPE_NEAR:
                    UIUtils.hindView(holder.tvTime);
                    UIUtils.showView(holder.tvDistance);
                    UIUtils.hindView(holder.tvPraiseNum);
                    break;
            }
            holder.tvPraiseNum.setText(entity.getLikeCount());
            holder.tvTime.setText(TimeUtil.getTimeStr(entity.getAddTime()));
            String distance = entity.getDistance();
            if (!TextUtils.isEmpty(distance)) {
                holder.tvDistance.setText(getDistance(entity.getDistance()));
            }
            imageLoader.displayImage(entity.getAvatar(), holder.ivAvatar, ImageLoaderUtils.getDisplayImageOptions());
            scalLoadImageVideo(holder, entity.getMediaPath());
            holder.ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onAvatarClick(entity);
                    }
                }
            });
            holder.ivBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onItemClick(holder.itemView, position, entity);
                    }
                }
            });
        }
    }

    private String getDistance(String dis) {
        Log.i("info", "==============dis:" + dis);
        String distance = "";
        float ad = Float.parseFloat(dis);
        if (ad > 1000) {
            int k = (int) ad / 1000;
            int m = (int) (ad % 1000) / 100;
            if (m != 0) {
                distance = k + "." + m + " km";
            } else {
                distance = k + " km";
            }
        } else {
            distance = (int) ad + " m";
        }
        return distance;
    }

    private String[] getSize(String url) {
        String str = url.substring(url.indexOf("-"), url.length());
        String size = str.substring(str.indexOf("-") + 1, str.indexOf("."));
        return size.split("x");
    }

    private void scalLoadImageVideo(ViewHolder holder, String videoPath) {
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
        return dataList.size();
    }
}
