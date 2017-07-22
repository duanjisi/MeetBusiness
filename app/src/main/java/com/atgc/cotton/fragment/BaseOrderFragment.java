package com.atgc.cotton.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

/**
 * Created by GMARUnity on 2017/6/28.
 */
public abstract class BaseOrderFragment extends Fragment {
    private LRecyclerView recyclerView;
    protected LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private TextView tv_empty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_base_order, container, false);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        recyclerView = (LRecyclerView) view.findViewById(R.id.list);
        recyclerView.setEmptyView(tv_empty);
        // recyclerView.setPullRefreshEnabled(false);
        recyclerView.setHeaderViewColor(R.color.material_dark, R.color.material, android.R.color.white);
        recyclerView.setRefreshProgressStyle(ProgressStyle.Pacman); //设置下拉刷新Progress的样式
        recyclerView.setFooterViewHint("拼命加载中", "我是有底线的", "网络不给力啊，点击再试一次吧");
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        final DividerDecoration divider = new DividerDecoration.Builder(this.getActivity())
//                .setHeight(R.dimen.default_divider_height)
//                .setPadding(R.dimen.default_divider_padding)
//                .setColorResource(R.color.text_color_gray_b)
//                .build();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        //mList.addItemDecoration(divider);

        setAdapterAndDecor(recyclerView);
    }

    protected abstract void setAdapterAndDecor(LRecyclerView list);

    public void openActivity(Class<?> clazz) {
        openActivity(clazz, null);
    }

    public void openActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void openActvityForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivityForResult(intent, requestCode);
    }

    public void openActvityForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }
}
