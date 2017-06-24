/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.boss66.meetbusiness.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.entity.ViewTypeBean;
import com.boss66.meetbusiness.listener.OnItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class MenuViewTypeAdapter extends SwipeMenuAdapter{

    public static final int VIEW_TYPE_MENU_NONE = 1;
    public static final int VIEW_TYPE_MENU_SINGLE = 2;
    public static final int VIEW_TYPE_MENU_MULTI = 3;
    public static final int VIEW_TYPE_MENU_LEFT = 4;

    private List<ViewTypeBean> mViewTypeBeanList;

    private OnItemClickListener mOnItemClickListener;

    public MenuViewTypeAdapter(List<ViewTypeBean> viewTypeBeanList) {
        this.mViewTypeBeanList = viewTypeBeanList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DefaultViewHolder){
            DefaultViewHolder holder1 = (DefaultViewHolder) holder;
            holder1.setData(mViewTypeBeanList.get(position));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mViewTypeBeanList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return mViewTypeBeanList == null ? 0 : mViewTypeBeanList.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_MENU_NONE){

            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu2, parent, false);
        }else{

            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        if(viewType==VIEW_TYPE_MENU_NONE){
            StringViewHolder viewHolder = new StringViewHolder(realContentView);
//            viewHolder.mOnItemClickListener = mOnItemClickListener;
            return viewHolder;
        }else{
            DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
            viewHolder.mOnItemClickListener = mOnItemClickListener;
            return viewHolder;
        }

    }

//    @Override
//    public void onBindViewHolder(MenuViewTypeAdapter.DefaultViewHolder holder, int position) {
//        holder.setData(mViewTypeBeanList.get(position));
//    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }

        public void setData(ViewTypeBean viewTypeBean) {
            this.tvTitle.setText(viewTypeBean.getContent());
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
    static class StringViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name;
        public StringViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

}
