package com.atgc.cotton.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.ActionEntity;
import com.atgc.cotton.entity.AgentParam;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

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
    private AgentParam param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_bind_card);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
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

    @Subscribe
    public void onMessageEvent(ActionEntity event) {
        if (event != null) {
            String action = event.getAction();
            String tag = (String) event.getData();
            if (action.equals(Constants.Action.AGENT_ACTIVITY_CLOSE)) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
