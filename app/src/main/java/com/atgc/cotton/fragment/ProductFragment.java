package com.atgc.cotton.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.atgc.cotton.R;

import java.util.ArrayList;

import com.atgc.cotton.adapter.ProductAdapter;
import com.atgc.cotton.entity.ProEntity;

/**
 * Created by Johnny on 2017/7/6.
 */
public class ProductFragment extends BaseFragment {

    private GridView gridView;
    private ProductAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        gridView = (GridView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        adapter = new ProductAdapter(getActivity());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new itemClickListener());
        initDatas();
    }

    private void initDatas() {
        ArrayList<ProEntity> pros = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ProEntity entity = new ProEntity();
            entity.setAvatar("http://img0.imgtn.bdimg.com/it/u=388444222,2315692232&fm=23&gp=0.jpg");
            pros.add(entity);
        }
        adapter.initData(pros);
    }

    private class itemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ProEntity entity = (ProEntity) adapterView.getItemAtPosition(i);
        }
    }
}
