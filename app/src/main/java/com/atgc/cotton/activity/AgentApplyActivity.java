package com.atgc.cotton.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.AgentCheck;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;
import com.atgc.cotton.http.request.AgentCheckRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2018-01-20.
 */
public class AgentApplyActivity extends BaseActivity {
    private static final String TAG = AgentApplyActivity.class.getSimpleName();

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_auth_state)
    TextView tvAuthState;
    @BindView(R.id.tv_apply_msg)
    TextView tvApplyMsg;
    @BindView(R.id.wb)
    WebView wb;
    @BindView(R.id.btn_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_agent);
        ButterKnife.bind(this);
        requestCheck();
    }

    private void requestCheck() {
        showLoadingDialog();
        AgentCheckRequest request = new AgentCheckRequest(TAG);
        request.send(new BaseDataRequest.RequestCallback<AgentCheck>() {
            @Override
            public void onSuccess(AgentCheck pojo) {
                cancelLoadingDialog();
                BindViewData(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg);
            }
        });
    }

    private void BindViewData(AgentCheck agent) {
        if (agent != null) {
            int type = agent.getType();
            btnNext.setTag(type);
            switch (type) {
                case -1://未申请过
                    showView(wb);
                    hindeView(tvApplyMsg);
                    hindeView(tvAuthState);
                    btnNext.setText("同意并进行实名认证");
                    wb.loadUrl(HttpUrl.AGENT_APPLY_AGREEMENT);
                    break;
                case 0://审核中
                    hindeView(tvApplyMsg);
                    showView(tvAuthState);
                    hindeView(wb);
                    btnNext.setText("确定");
                    break;
                case 1://审核通过
                    showView(tvApplyMsg);
                    hindeView(tvAuthState);
                    hindeView(wb);
                    tvApplyMsg.setText(getString(R.string.check_sucess));
                    btnNext.setText("选择商品");
                    break;
                case 2://审核不通过
                    showView(tvApplyMsg);
                    hindeView(tvAuthState);
                    hindeView(wb);
                    tvApplyMsg.setText(getString(R.string.check_fail));
                    btnNext.setText("重新申请");
                    break;
                case 3://违规关闭代理权

                    break;
            }
        }
    }

    private void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void hindeView(View view) {
        view.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_back, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_next:
                doSomeThinngs(view);
                break;
        }
    }


    private void doSomeThinngs(View view) {
        int type = (int) view.getTag();
        switch (type) {
            case -1://未申请过,  同意并进行实名认证
                openActivity(AgentCertifyActivity.class);
                break;
            case 0://审核中
                finish();
                break;
            case 1://审核通过
                openActivity(AgentGoodsActivity.class);
                break;
            case 2://审核不通过
                showView(tvApplyMsg);
                hindeView(tvAuthState);
                hindeView(wb);
                tvApplyMsg.setText(getString(R.string.check_fail));
                btnNext.setText("重新申请");
                break;
            case 3://违规关闭代理权

                break;
        }
    }
}
