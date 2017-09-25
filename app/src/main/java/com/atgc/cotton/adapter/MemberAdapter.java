package com.atgc.cotton.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.adapter.base.ABaseAdapter;
import com.atgc.cotton.entity.MemberEntity;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Johnny on 2017-08-26.
 */
public class MemberAdapter extends ABaseAdapter<MemberEntity> {

    private ImageLoader imageLoader;
    private Resources resources;

    public MemberAdapter(Context context) {
        super(context);
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        resources = context.getResources();
    }

    @Override
    protected View setConvertView(int position, MemberEntity entity, View convertView) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_mylist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (entity != null) {
            String sex = entity.getSex();
            Drawable nav_up = null;
            if (sex.equals("1")) {
                nav_up = resources.getDrawable(R.drawable.works_man);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            } else if (sex.equals("2")) {
                nav_up = resources.getDrawable(R.drawable.works_lady);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            }
            holder.tvName.setCompoundDrawables(null, null, nav_up, null);
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
            this.imageView = (CircleImageView) view.findViewById(R.id.iv_avatar);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tvSign = (TextView) view.findViewById(R.id.tv_sign);
        }
    }
}
