package com.atgc.cotton.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.ActionEntity;
import com.atgc.cotton.entity.AgentCheck;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.HttpUrl;
import com.atgc.cotton.http.request.AgentCheckRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Johnny on 2018-01-20.
 */
public class AgentApplyActivity extends BaseActivity {
    private static final String TAG = AgentApplyActivity.class.getSimpleName();

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_auth_state)
    TextView tvAuthState;
    @Bind(R.id.tv_apply_msg)
    TextView tvApplyMsg;
    @Bind(R.id.wb)
    WebView wb;
    @Bind(R.id.btn_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_agent);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        requestCheck();
    }

    @Subscribe
    public void onMessageEvent(ActionEntity event) {
        if (event != null) {
            String action = event.getAction();
            if (action.equals(Constants.Action.AGENT_ACTIVITY_CLOSE)) {
                requestCheck();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void requestCheck() {
        showLoadingDialog();
        AgentCheckRequest request = new AgentCheckRequest(TAG);
        request.send(new BaseDataRequest.RequestCallback<AgentCheck>() {
            @Override
            public void onSuccess(AgentCheck pojo) {
                cancelLoadingDialog();
                BindData(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg);
            }
        });
    }

    private void BindData(AgentCheck agent) {
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
                    tvAuthState.setText("你的申请资料已提交!");
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
