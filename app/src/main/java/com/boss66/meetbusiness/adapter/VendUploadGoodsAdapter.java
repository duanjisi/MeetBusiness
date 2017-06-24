package com.boss66.meetbusiness.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.adapter.base.ListBaseAdapter;
import com.boss66.meetbusiness.adapter.base.SuperViewHolder;
import com.boss66.meetbusiness.widget.SwipeMenuView;

/**
 * Created by GMARUnity on 2017/6/19.
 */
public class VendUploadGoodsAdapter extends ListBaseAdapter<String> {

    public VendUploadGoodsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_vend_attr;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        ImageView iv_delete = holder.getView(R.id.iv_delete);
//
        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView) holder.itemView).setIos(false).setLeftSwipe(true);
//
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(position);
                }
            }
        });
    }

    public void add(String text, int position) {
        mDataList.add(text);
        notifyItemInserted(getItemCount());
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

}
