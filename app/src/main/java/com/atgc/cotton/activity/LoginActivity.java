package com.atgc.cotton.activity;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.presenter.LoginPresenter;
import com.atgc.cotton.presenter.view.INormalView;
import com.atgc.cotton.util.PrefKey;
import com.atgc.cotton.util.PreferenceUtils;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.InputDetector;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录
 * Created by liw on 2017/7/6.
 */

public class LoginActivity extends MvpActivity<LoginPresenter> implements INormalView {

    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.img_logo)
    ImageView im_logo;
    @Bind(R.id.et_acconut)
    EditText et_account;
    @Bind(R.id.et_pw)
    EditText et_pw;
    @Bind(R.id.tv_Reset)
    TextView tv_reset;
    @Bind(R.id.btn_login)
    Button btn_login;
    @Bind(R.id.tv_register)
    TextView tv_register;
    @Bind(R.id.img_qq)
    ImageView imgQq;
    @Bind(R.id.img_wx)
    ImageView imgWx;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;
    @Bind(R.id.tv_bottom)
    TextView tvBottom;
    @Bind(R.id.rl_root)
    RelativeLayout rootLayout;

    private InputDetector detector;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this, context);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sizeHeight();
        initUI();
        initData();
//        detector.with(this).bindToET(et_account).build();
    }

    private int maxHeight = 0;

    private void sizeHeight() {
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                Rect r = new Rect();
                rootLayout.getWindowVisibleDisplayFrame(r);
                //r.top 是状态栏高度
                int screenHeight = rootLayout.getRootView().getHeight();
                int softHeight = screenHeight - (r.bottom - r.top);
                if (softHeight >= maxHeight) {
                    maxHeight = softHeight;
                }
                Log.i("info", "Size: " + softHeight);
                //boolean visible = heightDiff > screenHeight / 3;
            }
        });
    }

    /**
     * 登录成功回调
     *
     * @param s
     */
    @Override
    public void getDataSuccess(String s) {
//        showToast(s);
        PreferenceUtils.putInt(context, PrefKey.SORFT_HEIGHT_KEY, maxHeight);
        finish();
        openActivity(HomePagerActivity.class);
    }

    /**
     * 登录失败
     */
    @Override
    public void getDataFail() {
        showToast("登录失败,请重试");
    }

    protected void initUI() {
        TextPaint paint = tv_register.getPaint();
        if (paint != null) {
            paint.setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            paint.setAntiAlias(true);//抗锯齿
        }
    }

    protected void initData() {
        addQQQZonePlatform();
        addWXPlatform();
    }

    @OnClick({R.id.img_back, R.id.et_acconut, R.id.et_pw, R.id.tv_Reset, R.id.btn_login, R.id.tv_register, R.id.img_qq, R.id.img_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.et_acconut:
                break;
            case R.id.et_pw:
                break;
            case R.id.tv_Reset:     //重置密码
                openActivity(ResetPswActivity.class);
                break;
            case R.id.btn_login:
                login();

                break;
            case R.id.tv_register:
                openActivity(RegisterActivity.class);
                break;
            case R.id.img_qq:
                ThirdLogin(SHARE_MEDIA.QQ, "qq");
                break;
            case R.id.img_wx:
                ThirdLogin(SHARE_MEDIA.WEIXIN, "wx");
                break;
        }
    }

    /**
     * 手机号登录
     */
    private void login() {
        String phone = et_account.getText().toString();
        String psw = et_pw.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            showToast("登录帐号不能为空!");
            return;
        }
        if (!UIUtils.isMobile(phone)) {
            showToast("手机号格式不正确!");
            return;

        }
        if (TextUtils.isEmpty(psw)) {
            showToast("登录密码不能为空!");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("mobilephone", phone);
        map.put("password", psw);
        mPresenter.login(map);
    }


    /**
     * 第三方登录
     *
     * @param platform
     * @param type
     */
    private void ThirdLogin(SHARE_MEDIA platform, final String type) {
        mController.doOauthVerify(LoginActivity.this, platform,
                new SocializeListeners.UMAuthListener() {

                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        Toast.makeText(LoginActivity.this, "授权开始",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SocializeException e,
                                        SHARE_MEDIA platform) {
                        showToast(e.getMessage(), true);
                        Toast.makeText(LoginActivity.this, "授权失败",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                        // 获取uid
//                        Log.i("info", "Bundle:" + printBundle(value));
                        String uid = value.getString("uid");
                        String openid = value.getString("openid");
                        String access_token = value.getString("access_token");
//                        String unionid = value.getString("unionid");
                        if (!TextUtils.isEmpty(uid)
                                && !TextUtils.isEmpty(openid)
                                && !TextUtils.isEmpty(access_token)) {
                            // uid不为空，获取用户信息
                            getUserInfo(platform, openid, access_token, type);
                        } else {
                            Toast.makeText(LoginActivity.this, "授权失败...",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(LoginActivity.this, "授权取消",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * 获取用户信息
     *
     * @param platform
     * @param openid
     * @param access_token
     * @param type
     */
    private void getUserInfo(SHARE_MEDIA platform
            , final String openid
            , final String access_token
            , final String type) {
        mController.getPlatformInfo(LoginActivity.this, platform,
                new SocializeListeners.UMDataListener() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (info != null) {
                            String thusername = "";
                            String avatar = "";
                            String unionid = "";
                            if (info.containsKey("nickname")) {
                                thusername = (String) info.get("nickname");
                            } else if (info.containsKey("screen_name")) {
                                thusername = (String) info.get("screen_name");
                            }
                            if (info.containsKey("profile_image_url")) {
                                avatar = (String) info.get("profile_image_url");
                            } else if (info.containsKey("headimgurl")) {
                                avatar = (String) info.get("headimgurl");
                            }
                            if (info.containsKey("unionid")) {
                                unionid = (String) info.get("unionid");
                            }
                            Log.i("info", "===info:" + info.toString());
                            Log.i("info", "===thusername:" + thusername + "\n" + "avatar:" + avatar);
                            if (thusername != null && !thusername.equals("") && !avatar.equals("")) {
//                                ThirdLoginPathform(type, access_token, avatar, openid, unionid, thusername);
                                //调用自己的接口登录
                                if (type.equals("wx")) {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("wxunionid", unionid);
                                    map.put("wxnickname", thusername);
                                    map.put("wxavatar", avatar);

                                    mPresenter.wxLogin(map);

                                } else {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("accesstoken", access_token);
                                    map.put("qqnickname", thusername);
                                    map.put("qqavatar", avatar);

                                    mPresenter.qqLogin(map);
                                }
                            }
                        }
                    }
                });
    }
}
