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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Johnny on 2017-08-21.
 */
public class BindPhoneActivity extends BaseActivity {
    private static final String TAG = BindPhoneActivity.class.getSimpleName();
    private static final int DELAY_MILlIS = 1000;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_option)
    TextView tvOption;
    @Bind(R.id.et_num)
    EditText etNum;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.tv_qrCode)
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
                BindRequest();
                break;
            case R.id.tv_qrCode:
                sendCode();
                break;
        }
    }

    private void BindRequest() {
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

        if (!isMobile(phone)) {
            showToast("手机号格式不对", true);
            return;
        }
        showLoadingDialog();
        BindSMSCodeRequest request = new BindSMSCodeRequest(TAG, phone);
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                BindData(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void BindData(String string) {
        try {
            Log.i("info", "=====json:" + string);
//        String value = string.substring(string.indexOf(":") + 1, string.indexOf("}"));
            JSONObject obj = new JSONObject(string);
            interval = obj.getInt("interval");
            Log.i("info", "=====interval:" + interval);
            tvQrCode.setEnabled(false);
            handler.sendEmptyMessageDelayed(0, DELAY_MILlIS);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("info", "=====JSONException:" + e.getMessage());
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }
}
