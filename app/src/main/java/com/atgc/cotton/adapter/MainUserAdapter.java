package com.atgc.cotton.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.adapter.base.ABaseAdapter;
import com.atgc.cotton.entity.SUserEntity;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Johnny on 2017-10-23.
 */
public class MainUserAdapter extends ABaseAdapter<SUserEntity> {
    private ImageLoader imageLoader;

    public MainUserAdapter(Context context) {
        super(context);
        imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    @Override
    protected View setConvertView(int position, SUserEntity entity, View convertView) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_search_user, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (entity != null) {
            holder.tvName.setText(entity.getUserName());
            holder.tvSign.setText(entity.getSignature());
            imageLoader.displayImage(entity.getAvatar(), holder.imageView, ImageLoaderUtils.getDisplayImageOptions());
        }
        return convertView;
    }

    private class ViewHolder {
        CircleImageView imageView;
        TextView tvName;
        TextView tvSign;

        public ViewHolder(View view) {
            this.imageView = (CircleImageView) view.findViewById(R.id.image);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tvSign = (TextView) view.findViewById(R.id.tv_sign);
        }
    }
}
