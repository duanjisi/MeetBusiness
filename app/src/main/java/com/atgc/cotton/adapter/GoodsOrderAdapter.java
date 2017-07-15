package com.atgc.cotton.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.entity.OrderGoodsEntity;
import com.bumptech.glide.Glide;

/**
 * Created by liw on 2017/7/15.
 */

public class GoodsOrderAdapter extends BaseRecycleViewAdapter {
    private Context context;

    public GoodsOrderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindItemHolder(RecyclerView.ViewHolder holder, int position) {
        GoodsOrderHolder holder1 = (GoodsOrderHolder) holder;
        OrderGoodsEntity item = (OrderGoodsEntity) datas.get(position);

        holder1.tv_title.setText(item.getTitle());

        holder1.tv_goods_name.setText(item.getGoodsName());
        holder1.tv_goods_content.setText(item.getType());
        holder1.tv_goods_num.setText("x"+item.getBuyNum());
        holder1.tv_goods_price.setText("¥"+item.getGoodsPrice());

        holder1.tv_all_price.setText("¥"+item.getGoodsPrice()+"");
        holder1.tv_all_num.setText("共"+item.getBuyNum()+"件:  小计");

        Glide.with(context).load(item.getImgUrl()).into(holder1.iv_icon);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order,parent,false);

        return new GoodsOrderHolder(view);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class GoodsOrderHolder extends RecyclerView.ViewHolder{
        private TextView tv_title;
        private TextView tv_goods_name;
        private TextView tv_goods_content;
        private TextView tv_goods_price;
        private TextView tv_goods_num;
        private TextView tv_all_price;
        private TextView tv_all_num;

        private ImageView iv_icon;


        public GoodsOrderHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_goods_name = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tv_goods_content = (TextView) itemView.findViewById(R.id.tv_goods_content);
            tv_goods_price = (TextView) itemView.findViewById(R.id.tv_goods_price);
            tv_goods_num = (TextView) itemView.findViewById(R.id.tv_goods_num);
            tv_all_price = (TextView) itemView.findViewById(R.id.tv_all_price);
            tv_all_num = (TextView) itemView.findViewById(R.id.tv_all_num);

            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }

    }
}
