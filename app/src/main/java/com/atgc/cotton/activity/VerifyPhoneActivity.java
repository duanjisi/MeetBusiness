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

import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.PhoneUnBindRequest;
import com.atgc.cotton.http.request.PhoneVerifyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2017-08-21.
 */
public class VerifyPhoneActivity extends BaseActivity {
    private static final String TAG = VerifyPhoneActivity.class.getSimpleName();
    private static final int DELAY_MILlIS = 1000;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_option)
    TextView tvOption;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_qrCode)
    TextView tvQrCode;
    private AccountEntity account;
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
        setContentView(R.layout.activity_verify_phone);
        ButterKnife.bind(this);
        initDatas();
    }

    private void initDatas() {
        account = App.getInstance().getAccountEntity();
        String mbilePhone = account.getMobilePhone();
        if (!TextUtils.isEmpty(mbilePhone)) {
            tvNum.setText(mbilePhone);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_option, R.id.tv_qrCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_option:
                verifyRequest();
                break;
            case R.id.tv_qrCode:
                sendCode();
                break;
        }
    }

    private void sendCode() {
        showLoadingDialog();
        PhoneUnBindRequest request = new PhoneUnBindRequest(TAG);
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
            interval = obj.getInt("Interval");
            Log.i("info", "=====Interval:" + interval);
            tvQrCode.setEnabled(false);
            handler.sendEmptyMessageDelayed(0, DELAY_MILlIS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void verifyRequest() {
        String code = getText(etCode);
        if (TextUtils.isEmpty(code)) {
            showToast("验证码为空!", true);
            return;
        }
        showLoadingDialog();
        PhoneVerifyRequest request = new PhoneVerifyRequest(TAG, code);
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                initData(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void initData(String string) {
        try {
            Log.i("info", "=====json:" + string);
            JSONObject obj = new JSONObject(string);
            boolean isOk = obj.getBoolean("IsRight");
            if (isOk) {
                showToast("验证成功！", true);
                openActivity(ChangePhoneActivity.class);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
