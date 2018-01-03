package com.atgc.cotton.activity.vendingRack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseCompatActivity;
import com.atgc.cotton.adapter.VendingRackHomeAdapter;
import com.atgc.cotton.entity.VendGoodsEntity;
import com.atgc.cotton.presenter.VendRackPresenter;
import com.atgc.cotton.presenter.view.IVendRackView;
import com.atgc.cotton.util.UIUtils;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMARUnity on 2017/6/19.
 */
public class VendingRackHomeActivity extends BaseCompatActivity<VendRackPresenter> implements View.OnClickListener, IVendRackView {

    private Toolbar toolbar;
    private TextView tv_back;
    private Button bt_upload;
    private LRecyclerView rv_content;
    private VendingRackHomeAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private int page = 0, curPos = 0;
    private boolean isAddNew, isOnRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vending_rack_home);
        initView();
    }

    @Override
    protected VendRackPresenter createPresenter() {
        return new VendRackPresenter(this);
    }

    private void initView() {
        bt_upload = (Button) findViewById(R.id.bt_upload);
        tv_back = (TextView) findViewById(R.id.tv_back);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv_content = (LRecyclerView) findViewById(R.id.rv_content);
        tv_back.setOnClickListener(this);
        bt_upload.setOnClickListener(this);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        int sceenW = UIUtils.getScreenWidth(this);
        //bt_upload.getLayoutParams().height = sceenW / 8;
        adapter = new VendingRackHomeAdapter(this);
        List<VendGoodsEntity.Goods> list = new ArrayList<>();
        adapter.setDataList(list);
        adapter.setOnDelListener(new VendingRackHomeAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                curPos = pos;
                VendGoodsEntity.Goods item = (VendGoodsEntity.Goods) adapter.getItem(pos);
                if (item != null) {
                    mPresenter.deleteMyVendGoods(item.getGoodsId());
                }
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
        rv_content.setLoadMoreEnabled(true);
        rv_content.refreshComplete(20);
        rv_content.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isOnRefresh = true;
                        isAddNew = false;
                        rv_content.refreshComplete(20);
                        mPresenter.getMyVendGoods(page, 20);
                    }
                }, 1000);
            }
        });
        rv_content.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isOnRefresh = false;
                        isAddNew = false;
                        mPresenter.getMyVendGoods(page, 20);
                    }
                }, 1000);
            }
        });
        mPresenter.getMyVendGoods(page, 20);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_upload:
                openActvityForResult(VendUploadGoodsActivity.class, 101);
                break;
            case R.id.tv_back:
                finish();
//                showTipsDialog();
                break;
        }
    }

    @Override
    public void getMyVendGoods(List<VendGoodsEntity.Goods> entity) {
        int size = entity.size();
        if (size == 20) {
            rv_content.setNoMore(false);
            page++;
        } else {
            rv_content.setNoMore(true);
        }
        if (!isOnRefresh) {
            adapter.addAll(entity);
            adapter.notifyDataSetChanged();
        } else {
            adapter.setDataList(entity);
        }
    }

    @Override
    public void deleMyGoodsSuccess() {
        showToast("删除成功", false);
        adapter.getDataList().remove(curPos);
        adapter.notifyItemRemoved(curPos);

        if (curPos != (adapter.getDataList().size())) { // 如果移除的是最后一个，忽略
            adapter.notifyItemRangeChanged(curPos, adapter.getDataList().size() - curPos);
        }
        //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
    }

    @Override
    public void onError(String msg) {
        showToast(msg, false);
    }


//    protected void showTipsDialog() {
//        String msg = "你确定退出当前页面么?";
//        CommonDialogUtils.showDialog(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CommonDialogUtils.dismiss();
//            }
//        }, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CommonDialogUtils.dismiss();
//                finish();
//            }
//        }, context, msg);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            isOnRefresh = true;
            page = 0;
            mPresenter.getMyVendGoods(page, 20);
        }
    }
}
