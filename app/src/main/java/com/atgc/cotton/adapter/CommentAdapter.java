package com.atgc.cotton.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.adapter.base.ABaseAdapter;
import com.atgc.cotton.entity.Comment;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.TimeUtil;
import com.atgc.cotton.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Johnny on 2017/7/14.
 */
public class CommentAdapter extends ABaseAdapter<Comment> {

    private ImageLoader imageLoader;

    public CommentAdapter(Context context) {
        super(context);
        imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    @Override
    protected View setConvertView(int position, Comment entity, View convertView) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_comment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (entity != null) {
            holder.tv_name.setText(entity.getUidFromName());
            holder.tv_time.setText(TimeUtil.getDateTime(entity.getAddTime()));
            holder.tv_msg.setText(entity.getContent());
            imageLoader.displayImage(entity.getUidFromAvatar(), holder.image, ImageLoaderUtils.getDisplayImageOptions());
        }
        return convertView;
    }


    private class ViewHolder {
        CircleImageView image;
        TextView tv_name;
        TextView tv_time;
        TextView tv_msg;

        public ViewHolder(View view) {
            this.image = (CircleImageView) view.findViewById(R.id.iv_avatar);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
            this.tv_time = (TextView) view.findViewById(R.id.tv_time);
            this.tv_msg = (TextView) view.findViewById(R.id.tv_content);
        }
    }
}
