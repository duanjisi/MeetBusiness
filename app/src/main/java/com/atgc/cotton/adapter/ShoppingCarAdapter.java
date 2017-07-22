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
import com.atgc.cotton.entity.OrderGoods;
import com.atgc.cotton.widget.SwipeMenuView;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liw on 2017/6/27.
 */

public class ShoppingCarAdapter extends ListBaseAdapter<OrderGoods> {

    private DbUtils mDbUtils;

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

                            String userId = mDataList.get(position).getUserId();//店铺id
                            int _id = mDataList.get(position).get_id();
                            boolean checksss = mDataList.get(position).isChecksss();
                            int headpos = -1;
                            List<Integer> goodIds = new ArrayList<>();
                            mDataList.remove(position); //先删除掉这个data
                            notifyItemRemoved(position);

                            try {
                                if (mDbUtils != null) {

                                    mDbUtils.delete(OrderGoods.class, WhereBuilder.b("_id", "=", _id));
                                }
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            //遍历同一个店铺里的商品，看是否还有,如果没有就删除title
                            for (int i = 0; i < mDataList.size(); i++) {
                                String id = mDataList.get(i).getUserId();
                                if (userId.equals(id)) { //同一个店铺
                                    if (mDataList.get(i).getHead() == 0) {
                                        Integer goodsId = mDataList.get(i).getGoodsId();
                                        goodIds.add(goodsId);
                                    } else {
                                        headpos = i;
                                    }
                                }
                            }
                            if (goodIds.size() == 0) {
                                mDataList.remove(headpos);
                                notifyItemRemoved(headpos);
                            }
                            //刷新选中金额，和全选状态
                            List<Boolean> allChecks = new ArrayList<>(); //所有商店商品id的选中状态,用来刷新activity的全选图标

                            if (mDataList.size() == 0) {     //如果没有商品了，也要去刷新全选状态
                                mOnRefreshListener.onRfresh(mDataList, false);
                                return;
                            }

                            for (int i = 0; i < mDataList.size(); i++) {       //所有商店所有商品的check状态     这个是为了刷新activity的Ui
                                boolean check = mDataList.get(i).isChecksss();
                                allChecks.add(check);
                            }

                            if (!allChecks.contains(false)) {     //所有都是全选中，通知activity刷新ui
                                mOnRefreshListener.onRfresh(mDataList, true);
                            }
                            if (allChecks.contains(false)) {  //有一个没选中
                                mOnRefreshListener.onRfresh(mDataList, false);
                            }


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

                OrderGoods entity = mDataList.get(position);
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

                final boolean check1 = mDataList.get(position).isChecksss();
                if (check1) {
                    img_choose.setImageResource(R.drawable.selected);
                } else {
                    img_choose.setImageResource(R.drawable.unchecked);
                }

                img_choose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Boolean> checks = new ArrayList<>();   //当前商店的商品id的选中状态
                        List<Boolean> allChecks = new ArrayList<>(); //所有商店商品id的选中状态,用来刷新activity的全选图标
                        mDataList.get(position).setChecksss(!check1); //改变当前的选中状态
                        String userId = mDataList.get(position).getUserId();
                        //检查其他的统一id是否选中
//
                        int shopPos = -1;
                        for (int i = 0; i < mDataList.size(); i++) {
                            String id = mDataList.get(i).getUserId();
                            if (id.equals(userId)) {         //拿到同一个店铺的数据
                                Integer head = mDataList.get(i).getHead();
                                if (head == 0) {     //拿到商品的check
                                    boolean check = mDataList.get(i).isChecksss();
                                    checks.add(check);     //包id相同商品的选中状态存list，然后看是否contains false或者true
                                } else {
                                    shopPos = i;
                                }

                            }

                        }


                        if (!checks.contains(false)) {   //全选中     这两个判断，刷新adapter的Ui
                            mDataList.get(shopPos).setChecksss(true);
                        }
                        if (checks.contains(false)) { //没有全选中
                            mDataList.get(shopPos).setChecksss(false);
                        }

                        for (int i = 0; i < mDataList.size(); i++) {       //所有商店所有商品的check状态     这个是为了刷新activity的Ui
                            boolean check = mDataList.get(i).isChecksss();
                            allChecks.add(check);
                        }
                        if (!allChecks.contains(false)) {     //所有都是全选中，通知activity刷新ui
                            mOnRefreshListener.onRfresh(mDataList, true);
                        }
                        if (allChecks.contains(false)) {  //有一个没选中
                            mOnRefreshListener.onRfresh(mDataList, false);
                        }
                        notifyDataSetChanged();
                    }
                });
                break;
            case 1:
                TextView tv_shop_name = holder.getView(R.id.tv_shop_name);
                tv_shop_name.setText(mDataList.get(position).getTitle());
                final ImageView img_shop = holder.getView(R.id.img_shop);
                final boolean check = mDataList.get(position).isChecksss();
                if (check) {
                    img_shop.setImageResource(R.drawable.selected);
                } else {
                    img_shop.setImageResource(R.drawable.unchecked);
                }
                img_shop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDataList.get(position).setChecksss(!check);
                        String userId = mDataList.get(position).getUserId();
                        for (int i = 0; i < mDataList.size(); i++) {
                            if (userId.equals(mDataList.get(i).getUserId())) {
                                mDataList.get(i).setChecksss(!check);      //改变所有item，包括商店名
                            }
                        }

                        List<Boolean> allChecks = new ArrayList<Boolean>(); //所有id的选中状态
                        for (int i = 0; i < mDataList.size(); i++) {
                            boolean check2 = mDataList.get(i).isChecksss();
                            allChecks.add(check2);
                        }
                        if (!allChecks.contains(false)) {     //所有都是全选中，通知activity刷新ui
                            mOnRefreshListener.onRfresh(mDataList, true);
                        }
                        if (allChecks.contains(false)) {  //有一个没选中
                            mOnRefreshListener.onRfresh(mDataList, false);
                        }

                        notifyDataSetChanged();
                    }
                });

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
        void onRfresh(List<OrderGoods> datas, boolean b);
    }

    private onRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(onRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }

    public void setDb(DbUtils mDbUtils) {
        this.mDbUtils = mDbUtils;
    }

    public void chooseRefresh(boolean check) {
        for (OrderGoods data : mDataList) {
            data.setChecksss(check);
        }
        notifyDataSetChanged();

    }
}
