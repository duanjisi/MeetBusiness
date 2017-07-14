package com.atgc.cotton.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.atgc.cotton.entity.VideoEntity;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.paging.gridview.PagingBaseAdapter;

/**
 * Created by Johnny on 2017/7/6.
 */
public class ProductAdapter extends PagingBaseAdapter<VideoEntity> {

    private ImageLoader imageLoader;
    private Context context;
    private float mImageHeight;

    public ProductAdapter(Context context) {
        this.context = context;
        mImageHeight = (UIUtils.getScreenWidth(context) - UIUtils.dip2px(context, 30)) / 3;
        imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

//    @Override
//    protected View setConvertView(int position, ProEntity entity, View convertView) {
//        ImageView imageView = new ImageView(context);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
//        imageView.getLayoutParams().width = (int) mImageHeight;
//        imageView.getLayoutParams().height = (int) mImageHeight;
//        if (entity != null) {
//            imageLoader.displayImage(entity.getAvatar(), imageView, ImageLoaderUtils.getDisplayImageOptions());
//        }
//        return imageView;
//    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
        imageView.getLayoutParams().width = (int) mImageHeight;
        imageView.getLayoutParams().height = (int) mImageHeight;
        VideoEntity entity = (VideoEntity) getItem(i);
        if (entity != null) {
            String videoPath = entity.getMediaPath();
            String imageUrl = "";
            if (videoPath.contains(".mp4")) {
                imageUrl = videoPath.replace(".mp4", ".jpg");
            } else if (videoPath.contains(".mov")) {
                imageUrl = videoPath.replace(".mov", ".jpg");
            }
            imageLoader.displayImage(imageUrl, imageView, ImageLoaderUtils.getDisplayImageOptions());
        }
        return imageView;
    }
}
