package com.atgc.cotton.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.adapter.base.ABaseAdapter;
import com.atgc.cotton.entity.VendGoodsEntity;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.widget.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Johnny on 2017/7/18.
 */
public class VendGoodAdapter extends ABaseAdapter<VendGoodsEntity.Goods> {
    private ImageLoader imageLoader;
    private itemRemoveCallBack itemRemoveCallBack;

    public VendGoodAdapter(Context context, itemRemoveCallBack callBack) {
        super(context);
        this.itemRemoveCallBack = callBack;
        imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    public VendGoodAdapter(Context context) {
        super(context);
        imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    @Override
    protected View setConvertView(int position, final VendGoodsEntity.Goods entity, View convertView) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_vend_goods, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (entity != null) {
            if (itemRemoveCallBack != null) {
                holder.iv_check.setImageResource(R.drawable.delete_item);
            } else {
                if (entity.isSelected()) {
                    holder.iv_check.setImageResource(R.drawable.selected);
                } else {
                    holder.iv_check.setImageResource(R.drawable.unchecked);
                }
            }
            holder.describe.setText(entity.getGoodsName());
            holder.content.setText(entity.getGoodsAttr());
            holder.price.setText("￥ " + entity.getShopPrice());
            holder.volum.setText("库存:" + entity.getGoodsNumber());
            String imageUrl = entity.getGoodsImg();
            if (!TextUtils.isEmpty(imageUrl)) {
                imageLoader.displayImage(imageUrl, holder.Riv, ImageLoaderUtils.getDisplayImageOptions());
            }

            holder.iv_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemRemoveCallBack != null) {
                        itemRemoveCallBack.onRemoveItem(entity);
                    }
                }
            });
        }
        return convertView;
    }

    public interface itemRemoveCallBack {
        void onRemoveItem(VendGoodsEntity.Goods good);
    }


    private class ViewHolder {
        ImageView iv_check;
        RoundImageView Riv;
        TextView describe;
        TextView content;
        TextView price;
        TextView volum;

        public ViewHolder(View view) {
            this.iv_check = (ImageView) view.findViewById(R.id.iv_tag);
            this.Riv = (RoundImageView) view.findViewById(R.id.iv_good_bg);
            this.describe = (TextView) view.findViewById(R.id.tv_good_describe);
            this.content = (TextView) view.findViewById(R.id.tv_good_content);
            this.price = (TextView) view.findViewById(R.id.tv_price);
            this.volum = (TextView) view.findViewById(R.id.tv_volum);
        }
    }
}
