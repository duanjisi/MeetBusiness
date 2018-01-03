package com.atgc.cotton.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.adapter.base.ABaseAdapter;
import com.atgc.cotton.entity.SVideoEntity;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Johnny on 2017-10-23.
 */
public class MainVideoAdapter extends ABaseAdapter<SVideoEntity> {
    private ImageLoader imageLoader;

    public MainVideoAdapter(Context context) {
        super(context);
        imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    @Override
    protected View setConvertView(int position, SVideoEntity entity, View convertView) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_search_video, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (entity != null) {
            holder.tvName.setText(entity.getContent());
            String videoPath = entity.getMediaPath();
            String imageUrl = "";
            if (videoPath.contains(".mp4")) {
                imageUrl = videoPath.replace(".mp4", ".jpg");
            } else if (videoPath.contains(".mov")) {
                imageUrl = videoPath.replace(".mov", ".jpg");
            }
            imageLoader.displayImage(imageUrl, holder.imageView, ImageLoaderUtils.getDisplayImageOptions());
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView tvName;

        public ViewHolder(View view) {
            this.imageView = (ImageView) view.findViewById(R.id.image);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
