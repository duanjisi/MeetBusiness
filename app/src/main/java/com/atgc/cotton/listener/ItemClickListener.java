package com.atgc.cotton.listener;

import android.view.View;

import com.atgc.cotton.entity.VideoEntity;

/**
 * Created by clevo on 2015/7/30.
 */
public interface ItemClickListener {
    void onItemClick(View view, int position, VideoEntity video);

    void onAvatarClick(VideoEntity video);
}
