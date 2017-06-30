package com.boss66.meetbusiness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.videorange.VideoThumbnailInfo;
import com.boss66.meetbusiness.videorange.VideoThumbnailTask;
import com.ksyun.media.shortvideo.kit.KSYEditKit;

/**
 * Created by Johnny on 2017/6/29.
 */
public class VideoThumbAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private VideoThumbnailInfo[] mList;
    private Context mContext;
    private KSYEditKit mRetriever;

    public VideoThumbAdapter(Context mContext, VideoThumbnailInfo[] mList, KSYEditKit mRetriever) {
        this.mContext = mContext;
        this.mList = mList;
        this.mRetriever = mRetriever;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.length;
    }

    @Override
    public Object getItem(int i) {
        return mList[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_thumbnail, parent, false);
            holder = new Holder();
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        ViewGroup.LayoutParams param = convertView.getLayoutParams();
        param.width = mList[position].mWidth;
        convertView.setLayoutParams(param);
        holder.thumbnail.setImageBitmap(null);
        if (mList[position].mBitmap != null) {
            holder.thumbnail.setImageBitmap(mList[position].mBitmap);
        } else {
            if (mList[position].mType == VideoThumbnailInfo.TYPE_NORMAL) {
                VideoThumbnailTask.loadBitmap(mContext, holder.thumbnail,
                        null, (long) (mList[position].mCurrentTime * 1000), mList[position],
                        mRetriever, null);
            }
        }
        return convertView;
    }

    /**
     * View holder for the views we need access to
     */
    private static class Holder {
        public ImageView thumbnail;
    }
}
