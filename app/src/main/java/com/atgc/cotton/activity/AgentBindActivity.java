package com.atgc.cotton.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2018-01-20.
 * 绑定银行卡
 */
public class AgentBindActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.et_bank_account)
    EditText etBankAccount;
    @Bind(R.id.et_bank_num)
    EditText etBankNum;
    @Bind(R.id.et_bank_name)
    EditText etBankName;
    @Bind(R.id.btn_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_bind_card);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.iv_back, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.btn_next:
                break;
        }
    }
}
