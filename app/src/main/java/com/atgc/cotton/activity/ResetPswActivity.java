package com.atgc.cotton.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.presenter.ResetPwsPresenter;
import com.atgc.cotton.presenter.view.IResetView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 重置密码
 * Created by liw on 2017/7/19.
 */
public class ResetPswActivity extends MvpActivity<ResetPwsPresenter> implements IResetView {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.et_acconut)
    EditText etAcconut;
    @BindView(R.id.et_pw)
    EditText etPw;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_psw);
        ButterKnife.bind(this);
    }

    @Override
    protected ResetPwsPresenter createPresenter() {
        return new ResetPwsPresenter(this);
    }


    @OnClick({R.id.img_back, R.id.btn_code, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_code:    //验证码
                Map<String, String> map = new HashMap<>();
                String phone = etAcconut.getText().toString();
                map.put("mobilephone", phone);
                mPresenter.sendRestCode(map);

                break;
            case R.id.btn_register:     //找回密码
                Map<String, String> map2 = new HashMap<>();
                String phone2 = etAcconut.getText().toString();
                String code = etCode.getText().toString();
                String psw = etPw.getText().toString();
                map2.put("mobilephone", phone2);
                map2.put("verifycode", code);
                map2.put("newpsw", psw);
                mPresenter.resetPsw(map2);
                break;
        }
    }

    /**
     * 获取验证码
     *
     * @param s
     */
    @Override
    public void getCodeSucess(String s) {
        BaseResult result = JSON.parseObject(s, BaseResult.class);
        if (result != null) {
            showToast(result.getMessage());
        }
    }

    /**
     * 重置密码
     *
     * @param s
     */
    @Override
    public void resetPswSucess(String s) {

        BaseResult result = JSON.parseObject(s, BaseResult.class);
        if (result != null) {
            showToast(result.getMessage());
            int code = result.getCode();
            if(code==0){
                finish();
            }
        }
    }

    /**
     * 网络不好的提示
     * @param msg
     */
    @Override
    public void onError(String msg) {

        showToast(msg);
    }
}
