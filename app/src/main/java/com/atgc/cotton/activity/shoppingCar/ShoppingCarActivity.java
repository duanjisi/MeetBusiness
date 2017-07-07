package com.atgc.cotton.activity.shoppingCar;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.atgc.cotton.entity.ShoopingEntity;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.adapter.ShoppingCarAdapter;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liw on 2017/6/19.
 */

public class ShoppingCarActivity extends BaseActivity implements View.OnClickListener {

    private LRecyclerView rcv_content;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private ShoppingCarAdapter adapter;

    private List<ShoopingEntity> datas;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_car);
        initUI();
    }

    private void initUI() {
        rcv_content = (LRecyclerView) findViewById(R.id.rcv_content);


        adapter = new ShoppingCarAdapter(this);
        datas =new ArrayList<>();
        ShoopingEntity data1 = new ShoopingEntity();
        data1.setType(1);
        ShoopingEntity data2 = new ShoopingEntity();
        data2.setType(2);
        ShoopingEntity data3 = new ShoopingEntity();
        data3.setType(1);

        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
        adapter.setDataList(datas);
        adapter.setOnDelListener(new ShoppingCarAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                Toast.makeText(ShoppingCarActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                adapter.getDataList().remove(pos);
                adapter.notifyItemRemoved(pos);

                if (pos != (adapter.getDataList().size())) {
                    adapter.notifyItemRangeChanged(pos, adapter.getDataList().size() - pos);
                }
                //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
            }
        });
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rcv_content.setHeaderViewColor(R.color.material_dark, R.color.material, android.R.color.white);
        rcv_content.setRefreshProgressStyle(ProgressStyle.Pacman); //设置下拉刷新Progress的样式
        rcv_content.setFooterViewHint("拼命加载中", "我是有底线的", "网络不给力啊，点击再试一次吧");
        //View empty = View.inflate(this, R.layout.item_vend_rack_empty, null);
        rcv_content.setEmptyView(findViewById(R.id.empty_view));
        rcv_content.setAdapter(mLRecyclerViewAdapter);
        rcv_content.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(0, R.anim.activity_out);
    }


    @Override
    public void onClick(View v) {
        finish();
    }
}
