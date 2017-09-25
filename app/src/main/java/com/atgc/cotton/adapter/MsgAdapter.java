package com.atgc.cotton.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.entity.MsgEntity;
import com.atgc.cotton.util.ToastUtil;
import com.atgc.cotton.widget.SwipeMenuView;
import com.bumptech.glide.Glide;

/**
 * Created by liw on 2017/7/22.
 */

public class MsgAdapter extends BaseRecycleViewAdapter {
    private Context context;

    public MsgAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindItemHolder(RecyclerView.ViewHolder holder, final int position) {
        MsgViewHolder holder1 = (MsgViewHolder) holder;
        final MsgEntity.DataBean data = (MsgEntity.DataBean) datas.get(position);
        Glide.with(context).load(data.getAvatar()).into(holder1.iv_icon);
        String userName = data.getUserName();

        String msgContent = data.getMsgContent();
        holder1.tv_title.setText(userName);
        holder1.tv_content.setText(msgContent);
        ((SwipeMenuView) holder1.itemView).setIos(false).setLeftSwipe(true);
        holder1.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(context, position + "");
                mOnSwipeListener.onDel(position);
            }
        });
        holder1.rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(data);
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_msg, parent, false);
        return new MsgViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MsgViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_content;
        ImageView iv_icon;
        ImageView iv_delete;
        TextView tv_title;
        TextView tv_content;

        public MsgViewHolder(View itemView) {
            super(itemView);
            rl_content = (RelativeLayout) itemView.findViewById(R.id.rl_content);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }


    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);
    }

    public interface ItemClickListener {
        void onItemClick(MsgEntity.DataBean bean);
    }

    private ItemClickListener itemClickListener;

    private ShoppingCarAdapter.onSwipeListener mOnSwipeListener;

    public void setOnDelListener(ShoppingCarAdapter.onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
