package com.atgc.cotton.activity.goodsDetail;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.activity.goodsDetail.EditAddressActivity;
import com.atgc.cotton.adapter.ChooseAddressAdapter;
import com.atgc.cotton.entity.AddressListEntity;
import com.atgc.cotton.event.RefreshAddress;
import com.atgc.cotton.presenter.ChooseAddressPresenter;
import com.atgc.cotton.presenter.view.IChooseAddressView;


import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 选择地址
 * Created by liw on 2017/7/11.
 */
public class ChooseAddressActivity extends MvpActivity<ChooseAddressPresenter> implements IChooseAddressView, View.OnClickListener {
    private Button btnAdd;
    private RecyclerView rvAddress;
    private ChooseAddressAdapter adapter;
    private String token;
    private List<AddressListEntity.DataBean> datas;
    private int deletePos;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {

        setContentView(R.layout.activity_choose_address);
        initUI();
        initData();
    }

    protected void initUI() {
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        rvAddress = (RecyclerView) findViewById(R.id.rv_address);
        adapter = new ChooseAddressAdapter(this);
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        rvAddress.setAdapter(adapter);

        //删除地址
        adapter.setDeleteListener(new ChooseAddressAdapter.onDeleteListener() {
            @Override
            public void onDel(int position) {
                deletePos = position;
                int addressId = datas.get(position).getAddressId();
                mPresenter.deleteAddress(token, addressId);

            }
        });
    }

    /**
     * 添加地址后通知刷新
     * @param event
     */
    @Subscribe
    public void onEvent(RefreshAddress event) {
        mPresenter.searchAddress(token, 1, 50);
    }


    protected void initData() {
        token = App.getInstance().getToken();
        mPresenter.searchAddress(token, 1, 50);
    }


    @Override
    protected ChooseAddressPresenter createPresenter() {
        return new ChooseAddressPresenter(this);
    }


    /**
     * 查询地址列表成功
     *
     * @param s
     */
    @Override
    public void searchAddreshSuccess(String s) {
        AddressListEntity entity = JSON.parseObject(s, AddressListEntity.class);
        if (entity.getCode() == 0) {
            datas = entity.getData();
            adapter.setDatas(datas);
            adapter.notifyDataSetChanged();
        }


    }

    /**
     * 删除成功
     */
    @Override
    public void deleteSuccess() {
        showToast("删除成功");
        adapter.remove(deletePos);

    }

    /**
     * 删除失败
     */
    @Override
    public void deleteFailue() {
        showToast("删除失败");
    }

    @Override
    public void onClick(View v) {
        openActivity(EditAddressActivity.class);
    }
}
