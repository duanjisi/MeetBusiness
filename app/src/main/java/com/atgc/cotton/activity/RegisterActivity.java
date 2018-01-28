package com.atgc.cotton.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.presenter.RegisterPresenter;
import com.atgc.cotton.presenter.view.IRegisterView;
import com.atgc.cotton.util.UIUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 * Created by liw on 2017/7/7.
 */
public class RegisterActivity extends MvpActivity<RegisterPresenter> implements IRegisterView {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.img_head)
    ImageView imgHead;
    @Bind(R.id.et_acconut)
    EditText etAcconut;
    @Bind(R.id.et_pw)
    EditText etPw;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.btn_code)
    Button btnCode;
    @Bind(R.id.rl_code)
    RelativeLayout rlCode;

    @Bind(R.id.btn_register)
    Button btnRegister;
    private HashMap<String, String> map;

    private int count = 60;
    private TimeCount time;


    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        time = new TimeCount(60000, 1000);
    }

    @OnClick({R.id.img_back, R.id.img_head, R.id.btn_code, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_head:    //头像
                break;
            case R.id.btn_code:     //验证码
                time.start();
                Map<String, String> map = new HashMap<>();
                String phone = etAcconut.getText().toString();
                map.put("mobilephone", phone);
                mPresenter.sendCode(map);
                break;

            case R.id.btn_register:  //注册            验证码还没加上，还有性别，昵称，接口也没
                register();
                break;
        }
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnCode.setClickable(false);
            btnCode.setText("(" + millisUntilFinished / 1000 + ") 秒后可重新发送");
        }

        @Override
        public void onFinish() {
            btnCode.setText("重新获取验证码");
            btnCode.setClickable(true);

        }
    }


    private void register() {

        String phone = etAcconut.getText().toString();
        String psw = etPw.getText().toString();
        String code = etCode.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号不能为空!");
            return;
        }
        if (!UIUtils.isMobile(phone)) {
            showToast("手机号格式不正确!");
            return;

        }
        if (TextUtils.isEmpty(psw)) {
            showToast("请输入密码!");
            return;
        }
        if (psw.length() < 6 || psw.length() > 16) {
            showToast("密码长度不符合要求");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            showToast("验证码不能为空!", true);
            return;
        }

        map = new HashMap<>();
        map.put("mobilephone", phone);
        map.put("password", psw);
        map.put("verifycode", code);
        mPresenter.register(map);
        //我的ok请求框架
//        OkManager.getInstance().doPost(ApiStores.API_SERVER_URL+"public/register", map, new OkManager.Funcl() {
//            @Override
//            public void onResponse(String result) {
//                L.i(result);
//
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });

    }

    //获取验证码接口通过后的UI操作
    @Override
    public void getCodeSucceed(String s) {
        BaseResult result = JSON.parseObject(s, BaseResult.class);
        if (result.getCode() == 0) {
            showToast("已发送");
        } else if (result.getCode() == 1) {
            showToast(result.getMessage());
        }
    }

    //登录成功后的UI操作
    @Override
    public void loginSucceed(String s) {
        BaseResult result = JSON.parseObject(s, BaseResult.class);
        if (result.getCode() == 0) {
            showToast("注册成功");
            finish();
        } else if (result.getCode() == 1) {
            showToast(result.getMessage());

        }
    }

    @Override
    public void applyFailure(String s) {
        showToast(s);
    }
}
