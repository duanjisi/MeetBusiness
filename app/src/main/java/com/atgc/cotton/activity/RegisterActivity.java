package com.atgc.cotton.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.LoginRequest;
import com.atgc.cotton.http.request.RegisterRequst;
import com.atgc.cotton.presenter.RegisterPresenter;
import com.atgc.cotton.presenter.view.IBaseView;
import com.atgc.cotton.presenter.view.IRegisterView;
import com.atgc.cotton.retrofit.ApiStores;
import com.atgc.cotton.util.L;
import com.atgc.cotton.util.OkManager;
import com.atgc.cotton.util.ToastUtil;
import com.atgc.cotton.util.UIUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

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



    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_back, R.id.img_head, R.id.btn_code,  R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_head:    //头像
                break;
            case R.id.btn_code:     //验证码
                showToast("验证码");
                break;

            case R.id.btn_register:  //注册            验证码还没加上，还有性别，昵称，接口也没
                register();
                break;
        }
    }

    private void register() {

        String phone = etAcconut.getText().toString();
        String psw = etPw.getText().toString();

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
        if(psw.length()<6||psw.length()>16){
            showToast("密码长度不符合要求");
            return;
        }

        map = new HashMap<>();
        map.put("mobilephone",phone);
        map.put("password",psw);
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

    //获取验证码成功后的UI操作
    @Override
    public void getCodeSucceed(String s) {
        BaseResult result = JSON.parseObject(s, BaseResult.class);
        if(result.getCode()==0){
            showToast("注册成功");
            finish();
        }
    }

    //登录成功后的UI操作
    @Override
    public void loginSucceed() {

    }
}
