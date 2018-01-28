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

import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.ActionEntity;
import com.atgc.cotton.entity.AgentParam;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.AgentSmsRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Johnny on 2018-01-20.
 */
public class AgentCertifyActivity extends BaseActivity {
    private static final String TAG = AgentApplyActivity.class.getSimpleName();
    private static final int DELAY_MILlIS = 1000;
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
        EventBus.getDefault().register(this);
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

    private void requestQrCode() {
        String phoneNum = getText(etPhoneNum);
        if (TextUtils.isEmpty(phoneNum)) {
            showToast("手机号为空", true);
            return;
        }
        if (!isMobile(phoneNum)) {
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
            interval = obj.getInt("Interval");
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

//        if (matcher(num, getString(R.string.num))) {
//            showToast("身份证号码格式不对！");
//            return;
//        }

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
        param.setIdcardno(num);
        param.setMobilephone(phoneNum);
        param.setSmscode(code);

        Bundle bundle = new Bundle();
        bundle.putSerializable("obj", param);
        openActivity(AgentBindActivity.class, bundle);
    }
}
