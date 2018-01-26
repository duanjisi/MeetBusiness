package com.atgc.cotton.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.config.LoginStatus;
import com.atgc.cotton.entity.ActionEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.BindPhoneRequest;
import com.atgc.cotton.http.request.BindSMSCodeRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Johnny on 2017-08-21.
 */
public class BindPhoneActivity extends BaseActivity {
    private static final String TAG = BindPhoneActivity.class.getSimpleName();
    private static final int DELAY_MILlIS = 1000;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_option)
    TextView tvOption;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_qrCode)
    TextView tvQrCode;

    private int interval = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (interval != 0) {
                        interval--;
                        tvQrCode.setText(String.valueOf(interval) + "秒");
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
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_option, R.id.tv_qrCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_option:
                BindViewRequest();
                break;
            case R.id.tv_qrCode:
                sendCode();
                break;
        }
    }

    private void BindViewRequest() {
        final String phone = getText(etNum);
        String qrCode = getText(etCode);
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号不能为空!", true);
            return;
        }

        if (TextUtils.isEmpty(qrCode)) {
            showToast("手机号不能为空!", true);
            return;
        }
        showLoadingDialog();
        BindPhoneRequest request = new BindPhoneRequest(TAG, phone, qrCode);
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                showToast("绑定手机成功!", true);
                LoginStatus sLoginStatus = LoginStatus.getInstance();
                sLoginStatus.setPhone(phone);
                EventBus.getDefault().post(new ActionEntity(Constants.Action.PHONE_BIND_STATE));
                finish();
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void sendCode() {
        String phone = getText(etNum);
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号不能为空!", true);
            return;
        }
        showLoadingDialog();
        BindSMSCodeRequest request = new BindSMSCodeRequest(TAG, phone);
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                BindViewData(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void BindViewData(String string) {
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

}
