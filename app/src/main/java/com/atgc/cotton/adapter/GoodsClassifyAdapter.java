package com.atgc.cotton.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atgc.cotton.R;
import com.atgc.cotton.entity.VendGoodsAttrEntity;
import com.atgc.cotton.util.L;
import com.atgc.cotton.widget.flowlayout.FlowLayout;
import com.atgc.cotton.widget.flowlayout.TagAdapter;
import com.atgc.cotton.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liw on 2017/7/13.
 */

public class GoodsClassifyAdapter extends BaseRecycleViewAdapter {
    private Context context;


    private Map<String, String> map = new HashMap<>();   //存id 和 属性


    private Map<String, Set<Integer>> map2 = new HashMap<>(); //存属性类别名：如 颜色  和选中的位置，是否被选中。

    public Map<String, String> getMap() {
        return map;
    }

    public Map<String, Set<Integer>> getMap2() {
        return map2;
    }

    public GoodsClassifyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindItemHolder(RecyclerView.ViewHolder holder, int position) {
        final GoodsClassifyHolder holder1 = (GoodsClassifyHolder) holder;
        VendGoodsAttrEntity item = (VendGoodsAttrEntity) datas.get(position);

        final String attrName = item.getAttrName();
        holder1.tv_content.setText(attrName);
        String attrValue = item.getAttrValue();
        final String[] tags = attrValue.split("@#");
        final int attrId = item.getAttrId();   //属性id

        map.put(attrName, tags[0]);     //一开始存

        Set<Integer> set = new HashSet<>();
        set.add(0);
//        L.i("set :" + set.toString());

        map2.put(attrName, set);

        TagAdapter<String> adapter = new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tv, holder1.ll_content, false);
                tv.setText(s);
                return tv;
            }
        };
        adapter.setSelectedList(0);
        holder1.ll_content.setAdapter(adapter);
        //tag点击时候的回调
        holder1.ll_content.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                Toast.makeText(context, tags[position], Toast.LENGTH_SHORT).show();

                L.i("选中的属性："+tags[position]+"id: "+attrId);
                map.put(attrName, tags[position]);

                return true;

            }
        });

        //TODO 判断选中状态。 如果有没选择的不让购买
        holder1.ll_content.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {

//                getActivity().setTitle("choose:" + selectPosSet.toString());
                L.i("是否选中:"+selectPosSet.toString()+"id: "+attrId);

                map2.put(attrName, selectPosSet);

            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods_classify, parent, false);
        return new GoodsClassifyHolder(view);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class GoodsClassifyHolder extends RecyclerView.ViewHolder {
        private TextView tv_content;
        private TagFlowLayout ll_content;

        public GoodsClassifyHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            ll_content = (TagFlowLayout) itemView.findViewById(R.id.ll_content);
        }

    }
}
