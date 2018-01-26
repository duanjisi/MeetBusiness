package com.atgc.cotton.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.ChangePwsRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2017-08-21.
 * 修改密码
 */
public class ChangePwsActivity extends BaseActivity {
    private static final String TAG = ChangePwsActivity.class.getSimpleName();
    private static final int DELAY_MILlIS = 1000;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_option)
    TextView tvOption;
    @BindView(R.id.et_older_pws)
    EditText etOlderPws;
    @BindView(R.id.et_new_pws)
    EditText etNewPws;
    @BindView(R.id.et_confirm_pws)
    EditText etConfirmPws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pws);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_option})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_option:
                changePwsRequest();
                break;
        }
    }

    private void changePwsRequest() {
        String older = getText(etOlderPws);
        String news = getText(etNewPws);
        String confirm = getText(etConfirmPws);
        if (TextUtils.isEmpty(older)) {
            showToast("原始密码不能为空!", true);
            return;
        }
        if (TextUtils.isEmpty(news)) {
            showToast("新密码不能为空!", true);
            return;
        }
        if (TextUtils.isEmpty(confirm)) {
            showToast("确认密码不能为空!", true);
            return;
        }
        if (!news.equals(confirm)) {
            showToast("确认密码与新密码不一致！", true);
            return;
        }
        showLoadingDialog();
        ChangePwsRequest request = new ChangePwsRequest(TAG, older, news);
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                showToast("修改登录密码成功!", true);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }
}
