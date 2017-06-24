package com.boss66.meetbusiness.activity.vendingRack;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.adapter.VendingRackHomeAdapter;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMARUnity on 2017/6/19.
 */
public class VendingRackHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView tv_back;
    private Button bt_upload;
    private LRecyclerView rv_content;
    private VendingRackHomeAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vending_rack_home);
        initView();
    }

    private void initView() {
        bt_upload = (Button) findViewById(R.id.bt_upload);
        tv_back = (TextView) findViewById(R.id.tv_back);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv_content = (LRecyclerView) findViewById(R.id.rv_content);
        tv_back.setOnClickListener(this);
        bt_upload.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = new VendingRackHomeAdapter(this);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add("库存：" + i);
        }
        adapter.setDataList(list);
        adapter.setOnDelListener(new VendingRackHomeAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                Toast.makeText(VendingRackHomeActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                adapter.getDataList().remove(pos);
                adapter.notifyItemRemoved(pos);

                if (pos != (adapter.getDataList().size())) { // 如果移除的是最后一个，忽略
                    adapter.notifyItemRangeChanged(pos, adapter.getDataList().size() - pos);
                }
                //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
            }
        });
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rv_content.setHeaderViewColor(R.color.material_dark, R.color.material, android.R.color.white);
        rv_content.setRefreshProgressStyle(ProgressStyle.Pacman); //设置下拉刷新Progress的样式
        rv_content.setFooterViewHint("拼命加载中", "我是有底线的", "网络不给力啊，点击再试一次吧");
        //View empty = View.inflate(this, R.layout.item_vend_rack_empty, null);
        rv_content.setEmptyView(findViewById(R.id.empty_view));
        rv_content.setAdapter(mLRecyclerViewAdapter);
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.dp_066)
                .setPadding(R.dimen.dp_4)
                .setColorResource(R.color.text_color_gray_c)
                .build();
        rv_content.addItemDecoration(divider);
//        rv_content.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_upload:
                openActivity(VendUploadGoodsActivity.class, null);
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    public void openActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
