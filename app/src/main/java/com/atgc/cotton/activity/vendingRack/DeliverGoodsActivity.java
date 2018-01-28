package com.atgc.cotton.activity.vendingRack;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.DeliverGoodsRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2017/8/11.
 */
public class DeliverGoodsActivity extends BaseActivity {
    private static final String TAG = DeliverGoodsActivity.class.getSimpleName();
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_deliver)
    TextView tvDeliver;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_num)
    EditText etNum;

    private int orderid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_goods);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderid = bundle.getInt("orderid", 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.img_back, R.id.tv_deliver})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_deliver:
                deliverGoodsRequest();
                break;
        }
    }


    private void deliverGoodsRequest() {
        String expressname = getText(etName);
        String expressnum = getText(etNum);
        if (TextUtils.isEmpty(expressname)) {
            showToast("快递名称为空!", true);
            return;
        }
        if (TextUtils.isEmpty(expressname)) {
            showToast("快递单号为空!", true);
            return;
        }
        showLoadingDialog();
        DeliverGoodsRequest request = new DeliverGoodsRequest(TAG, "" + orderid, expressname, expressnum);
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                showToast("发货成功!", true);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }
}
