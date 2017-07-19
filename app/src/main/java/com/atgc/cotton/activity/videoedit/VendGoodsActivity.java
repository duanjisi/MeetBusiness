package com.atgc.cotton.activity.videoedit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.adapter.VendGoodAdapter;
import com.atgc.cotton.entity.VendGoodsEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.VendGoodsRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 2017/7/18.
 * 售货架列表
 */
public class VendGoodsActivity extends BaseActivity implements AbsListView.OnScrollListener {
    private static final String TAG = VendGoodsActivity.class.getSimpleName();
    private static final int PAGE_SIZE = 10;
    private int page = 1;
    private ImageView iv_back;
    private TextView tv_option;
    private ListView listView;
    private VendGoodAdapter adapter;
    private boolean isEnd = false;
    private String[] strings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vend_goods);
        initViews();
    }

    private void initViews() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String str = bundle.getString("goodids", "");
            if (!TextUtils.isEmpty(str)) {
                strings = str.split(",");
            }
        }
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_option = (TextView) findViewById(R.id.tv_option);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("list", getSelectedList());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.listview);
        adapter = new VendGoodAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new itemClickListener());
        listView.setOnScrollListener(this);
        requestDatas();
    }

    private ArrayList<VendGoodsEntity.Goods> getSelectedList() {
        ArrayList<VendGoodsEntity.Goods> list = new ArrayList<>();
        ArrayList<VendGoodsEntity.Goods> goodses = (ArrayList<VendGoodsEntity.Goods>) adapter.getData();
        for (int i = 0; i < goodses.size(); i++) {
            VendGoodsEntity.Goods good = goodses.get(i);
            if (good.isSelected()) {
                list.add(good);
            }
        }
        return list;
    }


    private ArrayList<VendGoodsEntity.Goods> changList(List<VendGoodsEntity.Goods> goods) {
        if (strings != null && strings.length != 0) {
            for (int i = 0; i < strings.length; i++) {
                String str = strings[i];
                for (int j = 0; j < goods.size(); j++) {
                    VendGoodsEntity.Goods good = goods.get(j);
                    if (str.equals(good.getGoodsId())) {
                        good.setSelected(true);
                    }
                }
            }
        }
        return (ArrayList<VendGoodsEntity.Goods>) goods;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 当不滚动时
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                //加载更多功能的代码
                requestMoreDatas();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    private class itemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            VendGoodsEntity.Goods good = (VendGoodsEntity.Goods) adapterView.getItemAtPosition(i);
            if (good != null) {
                boolean selected = good.isSelected();
                good.setSelected(!selected);
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void requestDatas() {
        showLoadingDialog();
        VendGoodsRequest request = new VendGoodsRequest(TAG, "" + page, "" + PAGE_SIZE);
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                initDatas(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void initDatas(String json) {
        VendGoodsEntity entity = JSON.parseObject(json, VendGoodsEntity.class);
        if (entity.getCode() == 0 && entity != null && entity.getData() != null) {
            ArrayList<VendGoodsEntity.Goods> goodses = changList(entity.getData());
            int size = goodses.size();
            if (size != PAGE_SIZE) {
                isEnd = true;
                showToast("数据加载完成!", true);
            }
            adapter.initData(goodses);
        } else {
            showToast(entity.getMessage(), true);
        }
    }

    private void requestMoreDatas() {
        if (isEnd) {
            return;
        }
        page++;
        VendGoodsRequest request = new VendGoodsRequest(TAG, "" + page, "" + PAGE_SIZE);
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                bindDatas(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void bindDatas(String json) {
        VendGoodsEntity entity = JSON.parseObject(json, VendGoodsEntity.class);
        if (entity.getCode() == 0 && entity != null && entity.getData() != null) {
            ArrayList<VendGoodsEntity.Goods> goodses = changList(entity.getData());
            int size = goodses.size();
            if (size != PAGE_SIZE) {
                isEnd = true;
                showToast("数据加载完成!", true);
            }
            adapter.addData(goodses);
        } else {
            showToast(entity.getMessage(), true);
        }
    }
}
