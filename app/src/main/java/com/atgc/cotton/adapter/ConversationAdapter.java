package com.atgc.cotton.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.adapter.base.ABaseAdapter;
import com.atgc.cotton.entity.BaseConversation;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.PrefKey;
import com.atgc.cotton.util.PreferenceUtils;
import com.atgc.cotton.util.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Johnny on 2016/7/11.
 */
public class ConversationAdapter extends ABaseAdapter<BaseConversation> {
    private ImageLoader imageLoader;
    private Context context;

    public ConversationAdapter(Context context) {
        super(context);
        this.context = context;
        imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    @Override
    protected View setConvertView(int position, final BaseConversation entity, View convertView) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_conversation, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (entity != null) {
            holder.name.setText(entity.getUser_name());
            String avatar = entity.getAvatar();
            String key = PrefKey.UN_READ_NEWS_KEY + "/" + entity.getConversation_id();
            String newsKey = PrefKey.NEWS_NOTICE_KEY + "/" + entity.getConversation_id();

            String msg = PreferenceUtils.getString(context, newsKey, "");
            holder.msg.setText(msg);
            int num = PreferenceUtils.getInt(context, key, 0);
            if (num != 0) {
                UIUtils.showView(holder.newsNum);
                holder.newsNum.setText("" + num);
            } else {
                UIUtils.hindView(holder.newsNum);
            }
//            holder.time.setText(TimeUtil.getChatTime(Long.parseLong(entity.getNewest_msg_time())));
            imageLoader.displayImage(entity.getAvatar(), holder.imageView,
                    ImageLoaderUtils.getDisplayImageOptions());
            Log.i("info", "================setConvertView()");
        }
        return convertView;
    }


    private class ViewHolder {
        ImageView imageView;
        TextView name;
        TextView newsNum;
        TextView msg;
        TextView time;

        public ViewHolder(View view) {
            this.imageView = (ImageView) view.findViewById(R.id.image);
            this.name = (TextView) view.findViewById(R.id.tv_name);
            this.newsNum = (TextView) view.findViewById(R.id.tv_notify);
            this.msg = (TextView) view.findViewById(R.id.tv_msg);
            this.time = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
