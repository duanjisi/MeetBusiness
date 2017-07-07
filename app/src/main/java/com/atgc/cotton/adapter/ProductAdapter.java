package com.atgc.cotton.adapter;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.atgc.cotton.adapter.base.ABaseAdapter;
import com.atgc.cotton.entity.ProEntity;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Johnny on 2017/7/6.
 */
public class ProductAdapter extends ABaseAdapter<ProEntity> {

    private ImageLoader imageLoader;
    private Context context;
    private float mImageHeight;

    public ProductAdapter(Context context) {
        super(context);
        this.context = context;
        mImageHeight = (UIUtils.getScreenWidth(context) - UIUtils.dip2px(context, 30)) / 3;
        imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    @Override
    protected View setConvertView(int position, ProEntity entity, View convertView) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
        imageView.getLayoutParams().width = (int) mImageHeight;
        imageView.getLayoutParams().height = (int) mImageHeight;
        if (entity != null) {
            imageLoader.displayImage(entity.getAvatar(), imageView, ImageLoaderUtils.getDisplayImageOptions());
        }
        return imageView;
    }
}
