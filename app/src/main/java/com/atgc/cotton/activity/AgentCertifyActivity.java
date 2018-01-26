package com.atgc.cotton.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.AgentParam;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.AgentSmsRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2018-01-20.
 */
public class AgentCertifyActivity extends BaseActivity {
    private static final String TAG = AgentApplyActivity.class.getSimpleName();
    private static final int DELAY_MILlIS = 1000;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_card_num)
    EditText etCardNum;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_qrCode)
    TextView tvQrCode;
    @BindView(R.id.btn_next)
    Button btnNext;
    private int interval = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (interval != 0) {
                        interval--;
                        tvQrCode.setText("  " + String.valueOf(interval) + "  秒");
                        handler.sendEmptyMessageDelayed(0, DELAY_MILlIS);
                    } else {
                        tvQrCode.setText("发送验证码");
                        tvQrCode.setEnabled(true);
                        handler.removeMessages(0);
                    }
                    break;
            }
        }
    };

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
        showLoadingDialog();
        AgentSmsRequest request = new AgentSmsRequest(TAG, phoneNum);
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                bindData(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg);
            }
        });
    }

    private void bindData(String string) {
        try {
            Log.i("info", "=====json:" + string);
            JSONObject obj = new JSONObject(string);
            interval = obj.getInt("interval");
            Log.i("info", "=====interval:" + interval);
            tvQrCode.setEnabled(false);
            handler.sendEmptyMessageDelayed(0, DELAY_MILlIS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                nextStep();
                break;
        }
    }

    private void nextStep() {
        String name = getText(etName);
        String num = getText(etCardNum);
        String phoneNum = getText(etPhoneNum);
        String code = getText(etCode);

        if (TextUtils.isEmpty(name)) {
            showToast("真实姓名为空！");
            return;
        }

        if (TextUtils.isEmpty(num)) {
            showToast("身份证号码为空！");
            return;
        }

        if (matcher(num, getString(R.string.num))) {
            showToast("身份证号码格式不对！");
            return;
        }

        if (TextUtils.isEmpty(phoneNum)) {
            showToast("手机号为空！");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            showToast("验证码为空！");
            return;
        }
        AgentParam param = new AgentParam();
        param.setTruename(name);
        param.setIdcartno(num);
        param.setMobilephone(phoneNum);
        param.setSmscode(code);

        Bundle bundle = new Bundle();
        bundle.putSerializable("obj", param);
        openActivity(AgentBindActivity.class, bundle);
    }
}
