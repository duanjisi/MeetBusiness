package com.boss66.meetbusiness.listener;

import android.view.View;

import com.boss66.meetbusiness.entity.VideoEntity;

/**
 * Created by clevo on 2015/7/30.
 */
public interface ItemClickListener {

    void onItemClick(View view, int position, VideoEntity video);
}
