package com.boss66.meetbusiness.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.adapter.base.ListBaseAdapter;
import com.boss66.meetbusiness.adapter.base.SuperViewHolder;
import com.boss66.meetbusiness.entity.ShoopingEntity;
import com.boss66.meetbusiness.widget.SwipeMenuView;

/**
 * Created by liw on 2017/6/27.
 */

public class ShoppingCarAdapter extends ListBaseAdapter<ShoopingEntity> {

    public ShoppingCarAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        switch (holder.getItemViewType()){
            case 1:
                View contentView = holder.getView(R.id.rl_content);
                ImageView iv_delete = holder.getView(R.id.iv_delete);

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
//        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
                contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("TAG", "onClick() called with: v = [" + v + "]");
                    }
                });
                break;
            case 2:


                break;
        }

    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return super.onCreateViewHolder(parent, viewType);
//        View itemView = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);

        View itemView;
        if(viewType==1){
             itemView = LayoutInflater.from(mContext).inflate(R.layout.item_goods, parent, false);
        }else{
             itemView = LayoutInflater.from(mContext).inflate(R.layout.item_title, parent, false);
        }
        return new SuperViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);

        if (mDataList.get(position).getType() == 1) {
            return 1;
        } else {
            return 2;

        }
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
