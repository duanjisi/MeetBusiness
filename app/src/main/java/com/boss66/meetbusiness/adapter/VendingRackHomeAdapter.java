package com.boss66.meetbusiness.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.adapter.base.ListBaseAdapter;
import com.boss66.meetbusiness.adapter.base.SuperViewHolder;
import com.boss66.meetbusiness.widget.SwipeMenuView;

import java.util.List;

/**
 * Created by GMARUnity on 2017/6/19.
 */
public class VendingRackHomeAdapter extends ListBaseAdapter<String> {

    public VendingRackHomeAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_vend_rack_home;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        View contentView = holder.getView(R.id.rl_content);
        ImageView iv_delete = holder.getView(R.id.iv_delete);
        TextView tv_num = holder.getView(R.id.tv_num);
        List<String> list = getDataList();
        if (list != null && list.size() > 0) {
            tv_num.setText(list.get(position));
        }
//        TextView title = holder.getView(R.id.title);
//        Button btnDelete = holder.getView(R.id.btnDelete);
//        Button btnUnRead = holder.getView(R.id.btnUnRead);
//        Button btnTop = holder.getView(R.id.btnTop);
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
//        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick() called with: v = [" + v + "]");
            }
        });
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
