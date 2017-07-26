package com.atgc.cotton.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.entity.GoodsEvaluateEntity;
import com.atgc.cotton.entity.PhotoInfo;
import com.atgc.cotton.entity.VendGoodsEntity;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.MultiImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liw on 2017/7/26.
 */

public class GoodsEvaluateAdapter extends BaseRecycleViewAdapter {
    private Context context;
    private final int screenWidth;

    public GoodsEvaluateAdapter(Context context) {
        this.context = context;
        screenWidth = UIUtils.getScreenWidth(context);
    }

    @Override
    public void onBindItemHolder(RecyclerView.ViewHolder holder, int position) {
        GoodsEvaluateHolder holder1 = (GoodsEvaluateHolder) holder;
        GoodsEvaluateEntity.DataBean data = (GoodsEvaluateEntity.DataBean) datas.get(position);
        holder1.tv_name.setText(data.getUserName());
        holder1.tv_content.setText(data.getContent());
        Glide.with(context).load(data.getAvatar()).into(holder1.img_head);
        List<String> pics = data.getPics();
        List<PhotoInfo> photos = new ArrayList<>();
        for(String s:pics){
            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.file_thumb =s;
            photoInfo.type=0;
            photos.add(photoInfo);
        }
        holder1.multiImagView.setSceenW(screenWidth);
        holder1.multiImagView.setList(photos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods_evaluate, parent, false);
        return new GoodsEvaluateHolder(view);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class GoodsEvaluateHolder extends RecyclerView.ViewHolder {
        ImageView img_head;
        TextView tv_name;
        TextView tv_content;
        MultiImageView multiImagView;

        public GoodsEvaluateHolder(View itemView) {
            super(itemView);
            img_head = (ImageView) itemView.findViewById(R.id.img_head);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            multiImagView = (MultiImageView) itemView.findViewById(R.id.multiImagView);
        }
    }
}
