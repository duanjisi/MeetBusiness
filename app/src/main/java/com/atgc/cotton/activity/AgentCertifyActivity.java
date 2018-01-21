package com.atgc.cotton.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.AgentSmsRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2018-01-20.
 */
public class AgentCertifyActivity extends BaseActivity {
    private static final String TAG = AgentApplyActivity.class.getSimpleName();

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_card_num)
    EditText etCardNum;
    @Bind(R.id.et_phone_num)
    EditText etPhoneNum;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.tv_qrCode)
    TextView tvQrCode;
    @Bind(R.id.btn_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_certify);
        ButterKnife.bind(this);
    }

    private void requestQrCode() {

        String phoneNum = getText(etPhoneNum);
        if (TextUtils.isEmpty(phoneNum)) {
            showToast("手机号为空", true);
            return;
        }
        if (!matcher(phoneNum, getString(R.string.phone))) {
            showToast("手机号格式不对", true);
            return;
        }
        AgentSmsRequest request = new AgentSmsRequest(TAG, phoneNum);
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {

            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });
    }


    private boolean matcher(String str, String regEx) {
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    @OnClick({R.id.iv_back, R.id.tv_qrCode, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_qrCode:
                requestQrCode();
                break;
            case R.id.btn_next:
                break;
        }
    }
}
