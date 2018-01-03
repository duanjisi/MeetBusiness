package com.atgc.cotton.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.goodsDetail.GoodsDetailActivity;
import com.atgc.cotton.activity.vendingRack.VendingRackHomeActivity;
import com.atgc.cotton.adapter.base.ListBaseAdapter;
import com.atgc.cotton.adapter.base.SuperViewHolder;
import com.atgc.cotton.entity.VendGoodsEntity;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.GlideRoundTransform;
import com.atgc.cotton.widget.SwipeMenuView;
import com.bumptech.glide.Glide;

/**
 * Created by GMARUnity on 2017/6/19.
 */
public class VendingRackHomeAdapter extends ListBaseAdapter<VendGoodsEntity.Goods> {

    private Context context;
    private int sceenW;

    public VendingRackHomeAdapter(Context context) {
        super(context);
        this.context = context;
        sceenW = UIUtils.getScreenWidth(context) / 4;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_vend_rack_home;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        View contentView = holder.getView(R.id.rl_content);
        ImageView iv_delete = holder.getView(R.id.iv_delete);
        ImageView iv_icon = holder.getView(R.id.iv_icon);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_icon.getLayoutParams();
        params.width = sceenW;
        params.height = sceenW;
        iv_icon.setLayoutParams(params);
        TextView tv_num = holder.getView(R.id.tv_num);
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_price = holder.getView(R.id.tv_price);

        VendGoodsEntity.Goods item = (VendGoodsEntity.Goods) getItem(position);
        if (item != null) {
            Glide.with(context).load(item.getGoodsImg()).error(R.drawable.zf_default_message_image).
                    transform(new GlideRoundTransform(context, 10)).into(iv_icon);
            tv_num.setText("库存：" + item.getGoodsNumber());
            tv_title.setText("" + item.getGoodsName());
            tv_content.setText("" + item.getGoodsAttr());
            tv_price.setText("￥" + item.getShopPrice() + "/" + item.getUnits());
        }
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
                VendGoodsEntity.Goods item = (VendGoodsEntity.Goods) getItem(position);
//                Intent intent = new Intent(context, VendUploadGoodsActivity.class);
//                intent.putExtra("goodsId", item.getGoodsId());
//                ((VendingRackHomeActivity) context).startActivityForResult(intent, 101);
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("goodId", item.getGoodsId());
                ((VendingRackHomeActivity) context).startActivity(intent);
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
