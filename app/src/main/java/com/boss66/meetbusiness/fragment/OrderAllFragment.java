package com.boss66.meetbusiness.fragment;

import android.support.v7.widget.RecyclerView;

import com.boss66.meetbusiness.adapter.OrderAdapter;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

/**
 * Created by GMARUnity on 2017/6/28.
 */
public class OrderAllFragment extends BaseOrderFragment {

    @Override
    protected void setAdapterAndDecor(LRecyclerView list) {
        setHasOptionsMenu(true);
        OrderAdapter orderAdapter = new OrderAdapter(getActivity());
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(orderAdapter);
        list.setAdapter(mLRecyclerViewAdapter);
    }
}
