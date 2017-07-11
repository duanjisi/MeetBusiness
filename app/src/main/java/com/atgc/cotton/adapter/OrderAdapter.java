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
import com.atgc.cotton.activity.GoodsDetailActivity;
import com.atgc.cotton.entity.OrderEntity;
import com.atgc.cotton.util.UIUtils;

/**
 * Created by GMARUnity on 2017/6/28.
 */
public class OrderAdapter extends BaseRecycleViewAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private int ITEM_HEADER = 0, ITEM_CONTENT = 1, ITEM_FOOTER = 2;
    private String[] orderTypes;
    private int sceenW;

    public OrderAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        orderTypes = new String[]{"待付款", "待发货", "待发货", "待评价"};
        sceenW = UIUtils.getScreenWidth(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        OrderEntity orderEntity = (OrderEntity) datas.get(position);
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
        final OrderEntity orderEntity = (OrderEntity) datas.get(position);
        int orderType = orderEntity.getOrderType();
        if (holder instanceof MyViewHolderHeader) {
            String storeName = orderEntity.getShopName();
            ((MyViewHolderHeader) holder).tv_order_state.setText(orderTypes[orderType]);
            if (!TextUtils.isEmpty(storeName)) {
                ((MyViewHolderHeader) holder).tv_store_name.setText(storeName);
            }
        } else if (holder instanceof MyViewHolderContent) {
            String goodsContent = orderEntity.getGoodsContent();
            if (!TextUtils.isEmpty(goodsContent)) {
                ((MyViewHolderContent) holder).tv_goods_content.setText(goodsContent);
            }
            String goodsName = orderEntity.getGoodsName();
            if (!TextUtils.isEmpty(goodsName)) {
                ((MyViewHolderContent) holder).tv_goods_name.setText(goodsName);
            }
            String goodsNum = orderEntity.getGoodsNum();
            if (!TextUtils.isEmpty(goodsNum)) {
                ((MyViewHolderContent) holder).tv_goods_num.setText("x" + goodsNum);
            }
            String goodsPrice = orderEntity.getGoodsPrice();
            if (!TextUtils.isEmpty(goodsPrice)) {
                ((MyViewHolderContent) holder).tv_goods_price.setText("￥" + goodsPrice);
            }

            //跳转商品页
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof MyViewHolderFooter) {
            int allNum = orderEntity.getAllNum();
            String allPrice = orderEntity.getAllPrice();
            if (!TextUtils.isEmpty(allPrice)) {
                ((MyViewHolderFooter) holder).tv_all_price.setText(allPrice);
            }
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
                        mOnBtnListener.onOrdBtn3(orderEntity,pos);
                    }
                }
            });
            switch (orderType) {//"0:待付款", "1:待发货", "2:待收货", "3:待评价"
                case 0:
                    ((MyViewHolderFooter) holder).bt_1.setText("付款");
                    ((MyViewHolderFooter) holder).bt_2.setText("取消订单");
                    ((MyViewHolderFooter) holder).bt_3.setVisibility(View.GONE);
                    break;
                case 1:
                    ((MyViewHolderFooter) holder).bt_1.setVisibility(View.GONE);
                    ((MyViewHolderFooter) holder).bt_2.setVisibility(View.GONE);
                    ((MyViewHolderFooter) holder).bt_3.setVisibility(View.GONE);
                    break;
                case 2:
                    ((MyViewHolderFooter) holder).bt_1.setText("确认收货");
                    ((MyViewHolderFooter) holder).bt_2.setText("查看物流");
                    ((MyViewHolderFooter) holder).bt_3.setVisibility(View.GONE);
                    break;
                case 3:
                    ((MyViewHolderFooter) holder).bt_1.setText("评价");
                    ((MyViewHolderFooter) holder).bt_2.setText("查看物流");
                    ((MyViewHolderFooter) holder).bt_3.setText("删除订单");
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

        public MyViewHolderFooter(View view) {
            super(view);
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
        void onOrdBtn1(OrderEntity orderEntity);

        void onOrdBtn2(OrderEntity orderEntity);

        void onOrdBtn3(OrderEntity orderEntity,int pos);
    }

    private onBtnListener mOnBtnListener;

    public void setOnBtnListener(onBtnListener mOnBtnListener) {
        this.mOnBtnListener = mOnBtnListener;
    }
}
