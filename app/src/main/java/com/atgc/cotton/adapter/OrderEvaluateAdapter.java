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
import com.atgc.cotton.activity.vendingRack.MyOrderActivity;
import com.atgc.cotton.activity.vendingRack.OrderEvaluateActivity;
import com.atgc.cotton.entity.OrderEntity;
import com.atgc.cotton.entity.OrderGoodsEntity;
import com.atgc.cotton.util.UIUtils;

/**
 * Created by GMARUnity on 2017/7/15.
 * 待评价
 */
public class OrderEvaluateAdapter extends BaseRecycleViewAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private int sceenW;

    public OrderEvaluateAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        sceenW = UIUtils.getScreenWidth(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_order_evaluate, parent, false);
        return new MyViewHolderContent(view);
    }

    @Override
    public void onBindItemHolder(RecyclerView.ViewHolder holder, final int position) {
        OrderGoodsEntity orderEntity = (OrderGoodsEntity) datas.get(position);
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
        ((MyViewHolderContent) holder).bt_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderGoodsEntity orderEntity = (OrderGoodsEntity) datas.get(position);
                Intent intent = new Intent(mContext, OrderEvaluateActivity.class);
                intent.putExtra("data", orderEntity);
                ((MyOrderActivity)mContext).startActivityForResult(intent,101);
                //mContext.sta(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    class MyViewHolderContent extends RecyclerView.ViewHolder {
        private Button bt_evaluate;
        private ImageView iv_icon;
        private TextView tv_goods_name, tv_goods_content, tv_goods_price, tv_goods_num;

        public MyViewHolderContent(View view) {
            super(view);
            bt_evaluate = (Button) view.findViewById(R.id.bt_evaluate);
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
}
