package com.atgc.cotton.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.AgentParam;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2018-01-20.
 * 绑定银行卡
 */
public class AgentBindActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_bank_account)
    EditText etBankAccount;
    @BindView(R.id.et_bank_num)
    EditText etBankNum;
    @BindView(R.id.et_bank_name)
    EditText etBankName;
    @BindView(R.id.btn_next)
    Button btnNext;
    private AgentParam param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_bind_card);
        ButterKnife.bind(this);
        param = (AgentParam) getIntent().getExtras().getSerializable("obj");
    }


    @OnClick({R.id.iv_back, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_next:
                nextStep();
                break;
        }
    }

    private void nextStep() {
        String user = getText(etBankAccount);
        String bankNum = getText(etBankNum);
        String bank = getText(etBankName);
        if (TextUtils.isEmpty(user)) {
            showToast("持卡人为空！");
            return;
        }
        if (TextUtils.isEmpty(bankNum)) {
            showToast("银行卡号为空！");
            return;
        }
        if (TextUtils.isEmpty(bank)) {
            showToast("银行为空！");
            return;
        }
        if (param != null) {
            param.setAccount_user(user);
            param.setAccount(bankNum);
            param.setBank(bank);
            Bundle bundle = new Bundle();
            bundle.putSerializable("obj", param);
            openActivity(AgentCertificateActivity.class, bundle);
        }
    }
}
