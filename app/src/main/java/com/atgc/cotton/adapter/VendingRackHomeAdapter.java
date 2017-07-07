package com.atgc.cotton.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.adapter.base.ListBaseAdapter;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.GlideRoundTransform;
import com.atgc.cotton.R;
import com.atgc.cotton.adapter.base.SuperViewHolder;
import com.atgc.cotton.entity.VendGoodsEntity;
import com.atgc.cotton.widget.SwipeMenuView;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by GMARUnity on 2017/6/19.
 */
public class VendingRackHomeAdapter extends ListBaseAdapter {

    private Context context;
    private int sceenW;
    private List<VendGoodsEntity> list;

    public VendingRackHomeAdapter(Context context) {
        super(context);
        this.context = context;
        sceenW = UIUtils.getScreenWidth(context);
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
        ImageView iv_icon = holder.getView(R.id.iv_icon);
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_price = holder.getView(R.id.tv_price);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_icon.getLayoutParams();
        layoutParams.width = (int) (sceenW / 3 * 0.8);
        layoutParams.height = (int) (sceenW / 3 * 0.8);
        iv_icon.setLayoutParams(layoutParams);
        Log.i("onBindItemHolder:", "" + position);
        VendGoodsEntity.Goods entity = (VendGoodsEntity.Goods) mDataList.get(position);
        if (entity != null) {
            String name = entity.getGoodsName();
            String img = entity.getGoodsImg();
            String price = entity.getShopPrice();
            String attr = entity.getGoodsAttr();
            String num = entity.getGoodsNumber();
            tv_title.setText("" + name);
            tv_num.setText("库存：" + num);
            tv_content.setText("" + attr);
            tv_price.setText("￥" + price);
            Glide.with(context).load(img).
                    error(R.drawable.zf_default_message_image).transform(new GlideRoundTransform(context, 10)).into(iv_icon);
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

    public void getList(List<VendGoodsEntity> list) {
        this.list = list;
    }
}
