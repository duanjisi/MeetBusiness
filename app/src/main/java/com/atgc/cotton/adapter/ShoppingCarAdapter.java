package com.atgc.cotton.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.adapter.base.ListBaseAdapter;
import com.atgc.cotton.adapter.base.SuperViewHolder;
import com.atgc.cotton.entity.OrderGoodsEntity;
import com.atgc.cotton.entity.ShoopingEntity;
import com.atgc.cotton.widget.SwipeMenuView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liw on 2017/6/27.
 */

public class ShoppingCarAdapter extends ListBaseAdapter<OrderGoodsEntity> {

    public ShoppingCarAdapter(Context context) {
        super(context);
    }


    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:
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

                //TODO     ui展示
                OrderGoodsEntity entity = mDataList.get(position);
                ImageView iv_icon = holder.getView(R.id.iv_icon);
                ImageView img_choose = holder.getView(R.id.img_choose);
                Glide.with(mContext).load(entity.getImgUrl()).into(iv_icon);
                TextView tv_title = holder.getView(R.id.tv_title);
                tv_title.setText(entity.getGoodsName());
                TextView tv_content = holder.getView(R.id.tv_content);
                tv_content.setText(entity.getType());
                TextView tv_price = holder.getView(R.id.tv_price);
                tv_price.setText("¥ " + entity.getGoodsPrice());
                TextView tv_num = holder.getView(R.id.tv_num);
                tv_num.setText("x" + entity.getBuyNum());

                final boolean check1 = mDataList.get(position).isCheck();
                if (check1) {
                    img_choose.setImageResource(R.drawable.selected);
                } else {
                    img_choose.setImageResource(R.drawable.unchecked);
                }

                final int[] shopPos = new int[1];
                img_choose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Boolean> checks = new ArrayList<Boolean>();   //当前商品id的选中状态
                        List<Boolean> allChecks = new ArrayList<Boolean>(); //所有id的选中状态
                        mDataList.get(position).setCheck(!check1); //改变当前的选中状态
                        Integer goodsId = mDataList.get(position).getGoodsId();
                        //检查其他的统一id是否选中
                        for (int i = 0; i < mDataList.size(); i++) {
                            Integer head = mDataList.get(i).getHead();
                            if (head == 0) {      //商品的data
                                Integer id = mDataList.get(i).getGoodsId();

                                if (id == goodsId) { //找到id相同的商品
                                    boolean check = mDataList.get(i).isCheck();
                                    checks.add(check);     //包id相同商品的选中状态存list，然后看是否contains false或者true
                                }
                            } else {
                                shopPos[0] = i;
                            }
                        }

                        if (!checks.contains(false)) {   //全选中
                            mDataList.get(shopPos[0]).setCheck(true);
                        }
                        if (checks.contains(false)) { //没有全选中,把activity全选状态移除，然后价格改变
                            mDataList.get(shopPos[0]).setCheck(false);
                        }

                        for (int i = 0; i < mDataList.size(); i++) {
                            boolean check = mDataList.get(i).isCheck();
                            allChecks.add(check);
                        }
                        if (!allChecks.contains(false)) {     //所有都是全选中，通知activity刷新ui
                            mOnRefreshListener.onRfresh(mDataList,true);
                        }
                        if (allChecks.contains(false)) {  //有一个没选中
                            mOnRefreshListener.onRfresh(mDataList,false);
                        }
                        notifyDataSetChanged();
                    }
                });
                break;
            case 1:
                TextView tv_shop_name = holder.getView(R.id.tv_shop_name);
                tv_shop_name.setText(mDataList.get(position).getTitle());
                final ImageView img_shop = holder.getView(R.id.img_shop);
                final boolean check = mDataList.get(position).isCheck();
                if (check) {
                    img_shop.setImageResource(R.drawable.selected);
                } else {
                    img_shop.setImageResource(R.drawable.unchecked);
                }
                img_shop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDataList.get(position).setCheck(!check);
                        Integer goodsId = mDataList.get(position).getGoodsId();
                        for (int i = 0; i < mDataList.size(); i++) {
                            if (mDataList.get(i).getGoodsId() == goodsId) {
                                mDataList.get(i).setCheck(!check);
                            }
                        }

                        List<Boolean> allChecks = new ArrayList<Boolean>(); //所有id的选中状态
                        for (int i = 0; i < mDataList.size(); i++) {
                            boolean check2 = mDataList.get(i).isCheck();
                            allChecks.add(check2);
                        }
                        if (!allChecks.contains(false)) {     //所有都是全选中，通知activity刷新ui
                            mOnRefreshListener.onRfresh(mDataList,true);
                        }
                        if (allChecks.contains(false)) {  //有一个没选中
                            mOnRefreshListener.onRfresh(mDataList,false);
                        }

                        notifyDataSetChanged();
                    }
                });
                // 同样要通知activity


                break;
        }

    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return super.onCreateViewHolder(parent, viewType);
//        View itemView = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);

        View itemView;
        if (viewType == 0) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_goods, parent, false);
        } else {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_title, parent, false);
        }
        return new SuperViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);

        if (mDataList.get(position).getHead() == 0) {
            return 0;  //商品
        } else {
            return 1; //标题布局
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

    /**
     * 刷新选中状态和金额
     */
    public interface onRefreshListener {
        void onRfresh(List<OrderGoodsEntity> datas,boolean b);
    }

    private onRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(onRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }

}
