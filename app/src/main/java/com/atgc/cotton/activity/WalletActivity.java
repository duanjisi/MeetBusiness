package com.atgc.cotton.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2017-08-21.
 */
public class WalletActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_acconut)
    EditText etAcconut;
    @Bind(R.id.et_amount)
    EditText etAmount;
    @Bind(R.id.tv_take_out)
    TextView tvTakeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_take_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_take_out:
                break;
        }
    }
}
