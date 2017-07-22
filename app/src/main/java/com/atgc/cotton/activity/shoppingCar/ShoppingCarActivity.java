package com.atgc.cotton.activity.shoppingCar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.activity.goodsDetail.WriteOrderActivity;
import com.atgc.cotton.adapter.ShoppingCarAdapter;
import com.atgc.cotton.entity.OrderGoods;
import com.atgc.cotton.entity.OrderGoodsListEntity;
import com.atgc.cotton.entity.ShoopingEntity;
import com.atgc.cotton.event.RefreshShoopingCar;
import com.atgc.cotton.presenter.ShoppingCarPresenter;
import com.atgc.cotton.presenter.view.INormalView;
import com.atgc.cotton.util.L;
import com.atgc.cotton.util.MoneyUtil;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


/**
 * 购物车
 * Created by liw on 2017/6/19.
 */

public class ShoppingCarActivity extends MvpActivity<ShoppingCarPresenter> implements View.OnClickListener, INormalView {

    private LRecyclerView rcv_content;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private ShoppingCarAdapter adapter;


    private DbUtils mDbUtils;
    private ImageView img_choose; //全选与否
    private boolean check = false; //默认未选中
    private List<OrderGoods> datas; //编辑后的数据

    private TextView tv_num;
    private TextView tv_delete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_shopping_car);
        initUI();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEvent(RefreshShoopingCar event){
        initData();
        tv_num.setText("0");

    }

    private void initData() {

        try {
            //数据库里数据，要做处理，加上标题
            List<OrderGoods> list = mDbUtils.findAll(OrderGoods.class);


            datas = new ArrayList<>();

            if (list != null && list.size() > 0) {

                Collections.sort(list, new Comparator<OrderGoods>() {
                    @Override
                    public int compare(OrderGoods data1, OrderGoods data2) {
                        //按照商品id排序，id相同在排在一起
                        return data1.getUserId().compareTo(data2.getUserId());
                    }
                });

                //存id的list
                List<String> ids = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    String goodsId = list.get(i).getUserId();

                    //如果不包含商品id，就add进去
                    if (!ids.contains(goodsId)) {
                        ids.add(goodsId);
                        String title = list.get(i).getTitle();

                        OrderGoods orderGoodsEntity = new OrderGoods();
                        orderGoodsEntity.setHead(1);    //用head来做布局不同的显示
                        orderGoodsEntity.setTitle(title); //显示店铺名布局
                        orderGoodsEntity.setGoodsId(list.get(i).getGoodsId()); //用id来做选中状态
                        orderGoodsEntity.setUserId(list.get(i).getUserId());
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
        mDbUtils = DbUtils.create(this);
        rcv_content = (LRecyclerView) findViewById(R.id.rcv_content);


        adapter = new ShoppingCarAdapter(this);
        adapter.setDb(mDbUtils);
        adapter.setOnDelListener(new ShoppingCarAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
//                Toast.makeText(ShoppingCarActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
//                adapter.getDataList().remove(pos);
//                adapter.notifyItemRemoved(pos);

                if (pos != (adapter.getDataList().size())) {
                    adapter.notifyItemRangeChanged(pos, adapter.getDataList().size() - pos);
                }
                //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
            }
        });
        adapter.setOnRefreshListener(new ShoppingCarAdapter.onRefreshListener() {
            @Override
            public void onRfresh(List<OrderGoods> mDataList, boolean b) {
                check = b;
                datas = mDataList;   //数据可以传过来，但是不能再setdatas给adapter了。
                if (check) {
                    img_choose.setImageResource(R.drawable.selected);
                } else {
                    img_choose.setImageResource(R.drawable.unchecked);
                }
                String allPrice = "0";

                for (OrderGoods data : mDataList) {
                    if (data.isChecksss()) {
                        if (data.getHead() == 0) {
                            int buyNum = data.getBuyNum();
                            Double goodsPrice = data.getGoodsPrice();
//                            allPrice = allPrice+(goodsPrice*buyNum);

                            String moneyMul = MoneyUtil.moneyMul(goodsPrice + "", buyNum + "");
                            allPrice = MoneyUtil.moneyAdd(allPrice, moneyMul);
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
        tv_delete = (TextView) findViewById(R.id.tv_delete);

        tv_delete.setOnClickListener(this);
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


                if (!check) {
                    tv_num.setText("0");

                } else {

                    String allPrice = "0";
                    for (OrderGoods data : datas) {
                        if (data.getHead() == 0) {
                                int buyNum = data.getBuyNum();
                                Double goodsPrice = data.getGoodsPrice();
//                                allPrice = allPrice + (goodsPrice * buyNum);
                                String moneyMul = MoneyUtil.moneyMul(goodsPrice + "", buyNum + "");
                                allPrice = MoneyUtil.moneyAdd(allPrice, moneyMul);
                        }

                    }
                    tv_num.setText(String.valueOf(allPrice));
                }


                adapter.chooseRefresh(check);

                break;
            case R.id.tv_delete:
                OrderGoodsListEntity entity = new OrderGoodsListEntity();
                List<OrderGoods> newDatas = new ArrayList<>();


                //把选中的数据取出来，只要商品，店铺后面再加。
                for (int i = 0; i < datas.size(); i++) {

                    int head = datas.get(i).getHead();
                    if (head == 0) {
                        boolean checksss = datas.get(i).isChecksss();
                        if (checksss) {
                            newDatas.add(datas.get(i));
                        }
                    }

                }

                if (newDatas.size() == 0) {
                    showToast("您还没有选择宝贝哦");
                    return;
                }
                //加上店铺

                //存id的list
                List<String> ids = new ArrayList<>();
                List<OrderGoods> addShopList = new ArrayList<>();
                for (int i = 0; i < newDatas.size(); i++) {
                    String goodsId = newDatas.get(i).getUserId();

                    //如果不包含商品id，就add进去
                    if (!ids.contains(goodsId)) {
                        ids.add(goodsId);
                        String title = newDatas.get(i).getTitle();

                        OrderGoods orderGoodsEntity = new OrderGoods();
                        orderGoodsEntity.setHead(1);    //用head来做布局不同的显示
                        orderGoodsEntity.setTitle(title); //显示店铺名布局
                        orderGoodsEntity.setGoodsId(newDatas.get(i).getGoodsId()); //用id来做选中状态
                        orderGoodsEntity.setUserId(newDatas.get(i).getUserId());
                        addShopList.add(orderGoodsEntity);
                    }
                    //再把商品data add进去
                    addShopList.add(newDatas.get(i));
                }


                entity.setData(addShopList);

                String goodsJson = JSON.toJSONString(entity);
                //订单页
                Intent intent = new Intent(context, WriteOrderActivity.class);
                intent.putExtra("goodsJson", goodsJson);
                startActivity(intent);


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
