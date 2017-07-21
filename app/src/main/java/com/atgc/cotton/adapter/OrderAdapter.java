package com.atgc.cotton.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.goodsDetail.GoodsDetailActivity;
import com.atgc.cotton.entity.OrderEntity;
import com.atgc.cotton.entity.OrderGoodsEntity;
import com.atgc.cotton.util.UIUtils;

/**
 * Created by GMARUnity on 2017/6/28.
 */
public class OrderAdapter extends BaseRecycleViewAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private int ITEM_HEADER = 0, ITEM_CONTENT = 1, ITEM_FOOTER = 2;
    private String[] orderTypes, orderSellTypes;
    private int sceenW;
    private boolean isBuy = true;

    public OrderAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        orderSellTypes = new String[]{"等待买家付款", "等待卖家发货", "等待买家收货", "订单已取消", "已完成", "未知状态", "等待卖家退款"};
        orderTypes = new String[]{"待付款", "待发货", "待收货", "订单已取消", "已完成", "未知状态"};
        sceenW = UIUtils.getScreenWidth(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        OrderGoodsEntity orderEntity = (OrderGoodsEntity) datas.get(position);
        int type = orderEntity.getContentType();
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEADER) {
            View view = mInflater.inflate(R.layout.item_order_head, parent, false);
            return new MyViewHolderHeader(view);
        } else if (viewType == ITEM_CONTENT) {
            View view = mInflater.inflate(R.layout.item_order_content, parent, false);
            return new MyViewHolderContent(view);
        } else {
            View view = mInflater.inflate(R.layout.item_order_foot, parent, false);
            return new MyViewHolderFooter(view);
        }
    }

    @Override
    public void onBindItemHolder(RecyclerView.ViewHolder holder, int position) {
        final int pos = position;
        final OrderGoodsEntity orderEntity = (OrderGoodsEntity) datas.get(position);
        String orderState = orderEntity.getOrdState();
        int orderType = 0;
        switch (orderState) {
            case "1000":
            case "2000":
                orderType = 0;
                break;
            case "1001":
            case "2001":
                orderType = 1;
                break;
            case "1002":
            case "2002":
                orderType = 2;
                break;
            case "1003":
            case "2003":
                orderType = 3;
                break;
            case "1004":
            case "2004":
                orderType = 4;
                break;
            case "4004":
                orderType = 5;
                break;
            case "2005":
                orderType = 6;
                break;
        }
        if (holder instanceof MyViewHolderHeader) {
            String storeName = orderEntity.getStoreName();
            if (isBuy && orderType != 6) {
                ((MyViewHolderHeader) holder).tv_order_state.setText(orderTypes[orderType]);
            } else {
                ((MyViewHolderHeader) holder).tv_order_state.setText(orderSellTypes[orderType]);
            }
            if (!TextUtils.isEmpty(storeName)) {
                ((MyViewHolderHeader) holder).tv_store_name.setText(storeName);
            }
        } else if (holder instanceof MyViewHolderContent) {
            String goodsContent = orderEntity.getGoodsAttr();
            if (!TextUtils.isEmpty(goodsContent)) {
                ((MyViewHolderContent) holder).tv_goods_content.setText(goodsContent);
            }
            String goodsName = orderEntity.getGoodsName();
            if (!TextUtils.isEmpty(goodsName)) {
                ((MyViewHolderContent) holder).tv_goods_name.setText(goodsName);
            }
            int goodsNum = orderEntity.getBuyNumber();
            ((MyViewHolderContent) holder).tv_goods_num.setText("x" + goodsNum);
            int goodsPrice = orderEntity.getShopPrice();
            ((MyViewHolderContent) holder).tv_goods_price.setText("￥" + goodsPrice);

            //跳转商品页
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof MyViewHolderFooter) {
            if (position == getItemCount() - 1) {
                ((MyViewHolderFooter) holder).v_end_line.setVisibility(View.GONE);
            } else {
                ((MyViewHolderFooter) holder).v_end_line.setVisibility(View.VISIBLE);
            }
            int allNum = orderEntity.getAllNum();
            int allPrice = orderEntity.getOrderAmount();
            ((MyViewHolderFooter) holder).tv_all_price.setText("￥" + allPrice);
            ((MyViewHolderFooter) holder).tv_all_num.setText("共" + allNum + "件：合计");
            ((MyViewHolderFooter) holder).bt_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnBtnListener != null) {
                        mOnBtnListener.onOrdBtn1(orderEntity);
                    }
                }
            });
            ((MyViewHolderFooter) holder).bt_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnBtnListener != null) {
                        mOnBtnListener.onOrdBtn2(orderEntity);
                    }
                }
            });
            ((MyViewHolderFooter) holder).bt_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnBtnListener != null) {
                        mOnBtnListener.onOrdBtn3(orderEntity, pos);
                    }
                }
            });
            //买---1000 待付款：可付款，取消订单  1001 待发货：无  1002 待收货：可确认收货，查看物流  1003 已取消：可删除订单
            // 1004 已完成：无 4004 未知状态：无

            //卖---2000 待买家付款：可确认订单，取消订单  2001 待发货：可备货，发货  2002 待买家收货：无  2003 已取消状态：可删除订单
            // 2004: 已完成：无  2005 待退款：可退款 4004 未知状态：无
            orderEntity.setOrderStatus(orderType);
            switch (orderType) {//"0:待付款", "1:待发货", "2:待收货", "3:待评价"
                case 0:
                    if (isBuy) {
                        ((MyViewHolderFooter) holder).bt_1.setText("付款");
                        ((MyViewHolderFooter) holder).bt_2.setText("取消订单");
                        ((MyViewHolderFooter) holder).bt_1.setVisibility(View.VISIBLE);
                        ((MyViewHolderFooter) holder).bt_2.setVisibility(View.VISIBLE);
                    } else {
                        ((MyViewHolderFooter) holder).bt_1.setVisibility(View.GONE);
                        ((MyViewHolderFooter) holder).bt_2.setVisibility(View.GONE);
                    }
                    ((MyViewHolderFooter) holder).bt_3.setVisibility(View.GONE);
                    break;
                case 1:
                    if (isBuy) {
                        ((MyViewHolderFooter) holder).bt_1.setVisibility(View.GONE);
                    } else {
                        ((MyViewHolderFooter) holder).bt_1.setVisibility(View.VISIBLE);
                        ((MyViewHolderFooter) holder).bt_2.setVisibility(View.VISIBLE);
                        ((MyViewHolderFooter) holder).bt_1.setText("确认发货");
                    }
                    ((MyViewHolderFooter) holder).bt_2.setVisibility(View.GONE);
                    ((MyViewHolderFooter) holder).bt_3.setVisibility(View.GONE);
                    break;
                case 2:
                    if (isBuy) {
                        ((MyViewHolderFooter) holder).bt_1.setText("确认收货");
                        ((MyViewHolderFooter) holder).bt_1.setVisibility(View.VISIBLE);
                        ((MyViewHolderFooter) holder).bt_2.setVisibility(View.VISIBLE);
                        ((MyViewHolderFooter) holder).bt_2.setText("查看物流");
                    } else {
                        ((MyViewHolderFooter) holder).bt_1.setVisibility(View.GONE);
                        ((MyViewHolderFooter) holder).bt_2.setVisibility(View.GONE);
                    }
                    ((MyViewHolderFooter) holder).bt_3.setVisibility(View.GONE);
                    break;
                case 3:
                    ((MyViewHolderFooter) holder).bt_1.setText("删除订单");
                    ((MyViewHolderFooter) holder).bt_1.setVisibility(View.VISIBLE);
                    ((MyViewHolderFooter) holder).bt_2.setVisibility(View.GONE);
                    ((MyViewHolderFooter) holder).bt_3.setVisibility(View.GONE);
                    break;
                case 6:
                    ((MyViewHolderFooter) holder).bt_2.setVisibility(View.GONE);
                    ((MyViewHolderFooter) holder).bt_3.setVisibility(View.GONE);
                    if (!isBuy) {
                        ((MyViewHolderFooter) holder).bt_1.setText("退款");
                        ((MyViewHolderFooter) holder).bt_1.setVisibility(View.VISIBLE);
                    } else {
                        ((MyViewHolderFooter) holder).bt_1.setVisibility(View.GONE);
                    }
                    break;
                default:
                    ((MyViewHolderFooter) holder).bt_1.setVisibility(View.GONE);
                    ((MyViewHolderFooter) holder).bt_2.setVisibility(View.GONE);
                    ((MyViewHolderFooter) holder).bt_3.setVisibility(View.GONE);
                    break;
            }

        }
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    class MyViewHolderContent extends RecyclerView.ViewHolder {
        private ImageView iv_icon;
        private TextView tv_goods_name, tv_goods_content, tv_goods_price, tv_goods_num;

        public MyViewHolderContent(View view) {
            super(view);
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
            tv_goods_content = (TextView) view.findViewById(R.id.tv_goods_content);
            tv_goods_price = (TextView) view.findViewById(R.id.tv_goods_price);
            tv_goods_num = (TextView) view.findViewById(R.id.tv_goods_num);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_icon.getLayoutParams();
            int scw = sceenW / 4;
            layoutParams.width = scw;
            layoutParams.height = scw;
            iv_icon.setLayoutParams(layoutParams);
        }
    }

    class MyViewHolderHeader extends RecyclerView.ViewHolder {
        private TextView tv_store_name, tv_order_state;

        public MyViewHolderHeader(View view) {
            super(view);
            tv_store_name = (TextView) view.findViewById(R.id.tv_store_name);
            tv_order_state = (TextView) view.findViewById(R.id.tv_order_state);
        }
    }

    class MyViewHolderFooter extends RecyclerView.ViewHolder {
        private TextView tv_all_price, tv_all_num;
        private Button bt_1, bt_2, bt_3;
        private View v_end_line;

        public MyViewHolderFooter(View view) {
            super(view);
            v_end_line = view.findViewById(R.id.v_end_line);
            tv_all_price = (TextView) view.findViewById(R.id.tv_all_price);
            tv_all_num = (TextView) view.findViewById(R.id.tv_all_num);
            bt_1 = (Button) view.findViewById(R.id.bt_1);
            bt_2 = (Button) view.findViewById(R.id.bt_2);
            bt_3 = (Button) view.findViewById(R.id.bt_3);
            bt_1.getLayoutParams().height = sceenW / 9;
            bt_2.getLayoutParams().height = sceenW / 9;
            bt_3.getLayoutParams().height = sceenW / 9;
        }
    }

    /**
     * 和Activity通信的接口
     */
    public interface onBtnListener {
        void onOrdBtn1(OrderGoodsEntity orderEntity);

        void onOrdBtn2(OrderGoodsEntity orderEntity);

        void onOrdBtn3(OrderGoodsEntity orderEntity, int pos);
    }

    private onBtnListener mOnBtnListener;

    public void setOnBtnListener(onBtnListener mOnBtnListener) {
        this.mOnBtnListener = mOnBtnListener;
    }

    public void getIsBuy(boolean isBuy) {
        this.isBuy = isBuy;
    }
}
