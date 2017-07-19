package com.atgc.cotton.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.entity.OrderGoods;
import com.atgc.cotton.util.MoneyUtil;
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
        switch (holder.getItemViewType()) {

            case 0:
                GoodsOrderHolder holder1 = (GoodsOrderHolder) holder;

                OrderGoods item = (OrderGoods) datas.get(position);


                Double goodsPrice = item.getGoodsPrice();
                int buyNum = item.getBuyNum();

                holder1.tv_goods_name.setText(item.getGoodsName());
                holder1.tv_goods_content.setText(item.getType());
                holder1.tv_goods_num.setText("x" + buyNum);
                holder1.tv_goods_price.setText("¥" + goodsPrice);

                String allPrice = MoneyUtil.moneyMul(goodsPrice + "", buyNum + "");

                holder1.tv_all_price.setText("¥" + allPrice);
                holder1.tv_all_num.setText("共" + item.getBuyNum() + "件:  小计");

                Glide.with(context).load(item.getImgUrl()).into(holder1.iv_icon);
                break;
            case 1:
                ShopNameHolder holder2 = (ShopNameHolder) holder;
                OrderGoods item2 = (OrderGoods) datas.get(position);
                holder2.tv_title.setText(item2.getTitle());

                break;
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
                return new GoodsOrderHolder(view);
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.item_order_title, parent, false);
                return new ShopNameHolder(view);
        }
        return null;


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class GoodsOrderHolder extends RecyclerView.ViewHolder {
        private TextView tv_goods_name;
        private TextView tv_goods_content;
        private TextView tv_goods_price;
        private TextView tv_goods_num;
        private TextView tv_all_price;
        private TextView tv_all_num;

        private ImageView iv_icon;


        public GoodsOrderHolder(View itemView) {
            super(itemView);
            tv_goods_name = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tv_goods_content = (TextView) itemView.findViewById(R.id.tv_goods_content);
            tv_goods_price = (TextView) itemView.findViewById(R.id.tv_goods_price);
            tv_goods_num = (TextView) itemView.findViewById(R.id.tv_goods_num);
            tv_all_price = (TextView) itemView.findViewById(R.id.tv_all_price);
            tv_all_num = (TextView) itemView.findViewById(R.id.tv_all_num);

            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }

    }

    public class ShopNameHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;

        public ShopNameHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        OrderGoods data = (OrderGoods) datas.get(position);
        if (data.getHead() == 0) {
            return 0;  //商品
        } else {
            return 1; //标题布局
        }
    }

}
