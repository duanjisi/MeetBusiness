package com.atgc.cotton.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.adapter.base.ListBaseAdapter;
import com.atgc.cotton.adapter.base.SuperViewHolder;
import com.atgc.cotton.entity.FilterEntity;
import com.bumptech.glide.Glide;

/**
 * Created by liw on 2017/6/29.
 */

public class FilterAdapter extends ListBaseAdapter<FilterEntity> {

    public FilterAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_filter;
    }

    @Override
    public void onBindItemHolder(final SuperViewHolder holder, final int position) {
        TextView tv_name = holder.getView(R.id.tv_name);

        ImageView img_content = holder.getView(R.id.img_content);
        tv_name.setText(mDataList.get(position).getName());
        Glide.with(mContext).load(mDataList.get(position).getFilter()).into(img_content);

        final View contentview = holder.getView(R.id.rl_content);

        contentview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("liwya","---------");
                contentview.setFocusableInTouchMode(true);
                contentview.requestFocus();
                contentview.setFocusableInTouchMode(false);

                mOnItenClickListener.onItemClick(holder.getLayoutPosition());
            }
        });
    }
    public interface onItemClickListener{
        void onItemClick(int postion);
    }

    private onItemClickListener mOnItenClickListener;

    public  void setItemClickListener(onItemClickListener mOnItemClickListener){
        this.mOnItenClickListener = mOnItemClickListener;

    }
}
