package com.atgc.cotton.activity.shoppingCar;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.adapter.ShoppingCarAdapter;
import com.atgc.cotton.entity.OrderGoodsEntity;
import com.atgc.cotton.entity.ShoopingEntity;
import com.atgc.cotton.presenter.ShoppingCarPresenter;
import com.atgc.cotton.presenter.view.INormalView;
import com.atgc.cotton.util.L;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by liw on 2017/6/19.
 */

public class ShoppingCarActivity extends MvpActivity<ShoppingCarPresenter> implements View.OnClickListener, INormalView {

    private LRecyclerView rcv_content;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private ShoppingCarAdapter adapter;



    private DbUtils mDbUtils;
    private ImageView img_choose; //全选与否
    private boolean check = false; //默认未选中
    private List<OrderGoodsEntity> datas;

    private TextView tv_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_car);
        initUI();
        initData();
    }

    private void initData() {
        mDbUtils = DbUtils.create(this);
        try {
            //数据库里数据，要做处理，加上标题
            List<OrderGoodsEntity> list = mDbUtils.findAll(OrderGoodsEntity.class);


            //编辑后的数据
            datas = new ArrayList<>();

            if (list != null && list.size() > 0) {

                Collections.sort(list, new Comparator<OrderGoodsEntity>() {
                    @Override
                    public int compare(OrderGoodsEntity data1, OrderGoodsEntity data2) {
                        //按照商品id排序，id相同在排在一起
                        return data1.getGoodsId().compareTo(data2.getGoodsId());
                    }
                });

                //存商品id的list
                List<Integer> ids = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    Integer goodsId = list.get(i).getGoodsId();

                    //如果不包含goodsid，就add进去
                    if (!ids.contains(goodsId)) {
                        ids.add(goodsId);
                        String title = list.get(i).getTitle();

                        OrderGoodsEntity orderGoodsEntity = new OrderGoodsEntity();
                        orderGoodsEntity.setHead(1);    //用head来做布局不同的显示
                        orderGoodsEntity.setTitle(title); //显示店铺名布局
                        orderGoodsEntity.setGoodsId(list.get(i).getGoodsId()); //用id来做选中状态
                        datas.add(orderGoodsEntity);
                    }
                    //再把商品data add进去
                    datas.add(list.get(i));
                }

                L.i(datas.toString());

                adapter.setDataList(datas);

            }


        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected ShoppingCarPresenter createPresenter() {
        return new ShoppingCarPresenter(this);
    }

    private void initUI() {
        rcv_content = (LRecyclerView) findViewById(R.id.rcv_content);


        adapter = new ShoppingCarAdapter(this);

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
        adapter.setOnRefreshListener(new ShoppingCarAdapter.onRefreshListener() {
            @Override
            public void onRfresh(List<OrderGoodsEntity> datas,boolean b) {
                if(b){
                    img_choose.setImageResource(R.drawable.selected);
                }else {
                    img_choose.setImageResource(R.drawable.unchecked);
                }
                Double allPrice = 0.0;
                for(OrderGoodsEntity data:datas){
                    if(data.isCheck()){
                        if(data.getHead()==0){
                            int buyNum = data.getBuyNum();
                            Double goodsPrice = data.getGoodsPrice();
                            allPrice = allPrice+(goodsPrice*buyNum);
                        }

                    }
                }
                tv_num.setText(String.valueOf(allPrice));


            }
        });
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rcv_content.setHeaderViewColor(R.color.material_dark, R.color.material, android.R.color.white);
//        rcv_content.setRefreshProgressStyle(ProgressStyle.Pacman); //设置下拉刷新Progress的样式
        rcv_content.setPullRefreshEnabled(false);
        rcv_content.setFooterViewHint("拼命加载中", "我是有底线的", "网络不给力啊，点击再试一次吧");
        //View empty = View.inflate(this, R.layout.item_vend_rack_empty, null);
        rcv_content.setEmptyView(findViewById(R.id.empty_view));
        rcv_content.setAdapter(mLRecyclerViewAdapter);
        rcv_content.setLayoutManager(new LinearLayoutManager(this));

        img_choose = (ImageView) findViewById(R.id.img_choose);
        img_choose.setOnClickListener(this);
        tv_num = (TextView) findViewById(R.id.tv_num);
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(0, R.anim.activity_out);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_choose:
                if (!check) {
                    img_choose.setImageResource(R.drawable.selected);
                    check = true;
                } else {
                    img_choose.setImageResource(R.drawable.unchecked);
                    check = false;
                }
                for(OrderGoodsEntity data:datas){
                    data.setCheck(check);
                }

                if(!check){
                    tv_num.setText(null);

                }else{

                    Double allPrice = 0.0;
                    for (OrderGoodsEntity data : datas) {
                        if(data.getHead()==0){
                            if (data.isCheck()) {
                                int buyNum = data.getBuyNum();
                                Double goodsPrice = data.getGoodsPrice();
                                allPrice = allPrice + (goodsPrice * buyNum);
                            }
                        }

                    }
                    tv_num.setText(String.valueOf(allPrice));
                }


                adapter.setDataList(datas);
                adapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    public void getDataSuccess(String s) {

    }

    @Override
    public void getDataFail() {

    }
}
