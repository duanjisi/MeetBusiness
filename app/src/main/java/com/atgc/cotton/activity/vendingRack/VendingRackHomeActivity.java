package com.atgc.cotton.activity.vendingRack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.adapter.VendingRackHomeAdapter;
import com.atgc.cotton.util.ToastUtil;
import com.atgc.cotton.widget.DialogFactory;
import com.atgc.cotton.R;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.entity.VendGoodsEntity;
import com.atgc.cotton.http.HttpUrl;
import com.atgc.cotton.util.NetworkUtil;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMARUnity on 2017/6/19.
 */
public class VendingRackHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = VendingRackHomeActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView tv_back;
    private Button bt_upload;
    private LRecyclerView rv_content;
    private VendingRackHomeAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<VendGoodsEntity.Goods> list;
    private int page = 0;
    private boolean isOnRefresh = false, isAddNew = false;
    // 进度条
    private ProgressDialog mProgressDialog;

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
        list = new ArrayList<>();
        adapter.setDataList(list);
        //adapter.getList(list);
        adapter.setOnDelListener(new VendingRackHomeAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                deleteGoods(pos);
            }
        });
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rv_content.setHeaderViewColor(R.color.material_dark, R.color.material, android.R.color.white);
        rv_content.setRefreshProgressStyle(ProgressStyle.Pacman); //设置下拉刷新Progress的样式
        rv_content.setFooterViewHint("拼命加载中", "我是有底线的", "网络不给力啊，点击再试一次吧");
        //View empty = View.inflate(this, R.layout.item_vend_rack_empty, null);
        rv_content.setEmptyView(findViewById(R.id.empty_view));
        rv_content.setAdapter(mLRecyclerViewAdapter);

        rv_content.setLoadMoreEnabled(true);
        rv_content.refreshComplete(20);
        rv_content.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(VendingRackHomeActivity.this, "刷新完成");
                        isOnRefresh = true;
                        isAddNew = false;
                        rv_content.refreshComplete(20);
                        getMyGoods();
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
                        getMyGoods();
                    }
                }, 1000);
            }
        });

        rv_content.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.dp_066)
                .setPadding(R.dimen.dp_4)
                .setColorResource(R.color.text_color_gray_c)
                .build();
        rv_content.addItemDecoration(divider);
        getMyGoods();
//        rv_content.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_upload:
                openActvityForResult(VendUploadGoodsActivity.class, 101);
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    public void openActvityForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    private void getMyGoods() {
        showLoadingDialog();
        if (!NetworkUtil.networkAvailable(this)) {
            ToastUtil.show(VendingRackHomeActivity.this, "网络异常，请检查网络", Toast.LENGTH_SHORT);
            return;
        }
        String url = HttpUrl.VEND_GET_MY_GOODS;
        HttpUtils httpUtils = new HttpUtils(60 * 1000);//实例化RequestParams对象
        //设置当前请求的缓存时间
        httpUtils.configCurrentHttpCacheExpiry(0 * 1000);
        //设置默认请求的缓存时间
        httpUtils.configDefaultHttpCacheExpiry(0);
        com.lidroid.xutils.http.RequestParams params = new com.lidroid.xutils.http.RequestParams();
        String access_token = App.getInstance().getToken();
        params.addHeader("Authorization", access_token);
        if (isOnRefresh || isAddNew) {
            page = 0;
        }
        url = url + "page=" + page + "&size=20";
        httpUtils.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                cancelLoadingDialog();
                String result = responseInfo.result;
                if (result != null) {
                    BaseResult dta = BaseResult.parse(result);
                    if (dta != null) {
                        if (dta.getCode() == 0 && dta.getStatus() == 200) {
                            VendGoodsEntity vendGoodsEntity = JSON.parseObject(result, VendGoodsEntity.class);
                            if (vendGoodsEntity != null) {
                                int code = vendGoodsEntity.getCode();
                                String msg = vendGoodsEntity.getMessage();
                                if (code == 0) {
                                    List<VendGoodsEntity.Goods> datas = vendGoodsEntity.getData();
                                    if (datas != null) {
                                        showData(datas);
                                    }
                                } else {
                                    ToastUtil.show(VendingRackHomeActivity.this, msg, Toast.LENGTH_SHORT);
                                }
                            } else {
                                rv_content.setNoMore(false);
                                ToastUtil.showShort(VendingRackHomeActivity.this, "没有更多数据了");
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                cancelLoadingDialog();
                ToastUtil.show(VendingRackHomeActivity.this, "服务器异常，请稍后再试", Toast.LENGTH_SHORT);
            }
        });
    }

    private void showData(List<VendGoodsEntity.Goods> datas) {
        int size = datas.size();
        if (size == 20) {
            rv_content.setNoMore(false);
            page++;
        } else {
            rv_content.setNoMore(true);
        }

        if (!isOnRefresh) {
            adapter.addAll(datas);
            adapter.notifyDataSetChanged();
        } else if (isOnRefresh || isAddNew) {
            adapter.setDataList(datas);
        }
    }

    private void deleteGoods(final int pos) {
        showLoadingDialog();
        if (!NetworkUtil.networkAvailable(this)) {
            ToastUtil.show(VendingRackHomeActivity.this, "网络异常，请检查网络", Toast.LENGTH_SHORT);
            return;
        }
        VendGoodsEntity.Goods item = (VendGoodsEntity.Goods) adapter.getDataList().get(pos);
        String goodsId = item.getGoodsId();
        String url = HttpUrl.VEND_CHANGE_GOODS + goodsId;
        HttpUtils httpUtils = new HttpUtils(60 * 1000);//实例化RequestParams对象
        com.lidroid.xutils.http.RequestParams params = new com.lidroid.xutils.http.RequestParams();
        String access_token = App.getInstance().getToken();
        params.addHeader("Authorization", access_token);
        httpUtils.send(HttpRequest.HttpMethod.DELETE, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                cancelLoadingDialog();
                String result = responseInfo.result;
                if (result != null) {
                    BaseResult dta = JSON.parseObject(result, BaseResult.class);
                    if (dta != null) {
                        if (dta.getCode() == 0 && dta.getStatus() == 200) {
                            ToastUtil.showShort(VendingRackHomeActivity.this, dta.getMessage());
                            adapter.getDataList().remove(pos);
                            adapter.notifyItemRemoved(pos);
                            if (pos != (adapter.getDataList().size())) { // 如果移除的是最后一个，忽略
                                adapter.notifyItemRangeChanged(pos, adapter.getDataList().size() - pos);
                            }
                            //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                        } else {
                            ToastUtil.showShort(VendingRackHomeActivity.this, "删除失败");
                        }
                    } else {
                        ToastUtil.showShort(VendingRackHomeActivity.this, "删除失败");
                    }
                } else {
                    ToastUtil.showShort(VendingRackHomeActivity.this, "删除失败");
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                cancelLoadingDialog();
                ToastUtil.show(VendingRackHomeActivity.this, "服务器异常，请稍后再试", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            isOnRefresh = true;
            isAddNew = true;
            rv_content.refreshComplete(20);
            getMyGoods();
        }
    }

    /**
     * 显示加载进度条
     *
     * @return: void
     */
    protected void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog();
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        } else {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog dialog = DialogFactory.createProgressDialog(this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setIndeterminate(true);
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.setCancelable(false);
        return dialog;
    }

    protected void cancelLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
