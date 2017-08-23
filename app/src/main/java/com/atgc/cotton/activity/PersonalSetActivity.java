package com.atgc.cotton.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.App;
import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.entity.ActionEntity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by liw on 2017/7/8.
 */
public class PersonalSetActivity extends BaseActivity {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @Bind(R.id.tv_volum)
    TextView tvVolum;
    @Bind(R.id.rl_cache)
    RelativeLayout rlCache;
//    @Bind(R.id.tv_auth_state)
//    TextView tvAuthState;
//    @Bind(R.id.rl_auth)
//    RelativeLayout rlAuth;
    @Bind(R.id.tv_bind)
    TextView tvBind;
    @Bind(R.id.rl_phone)
    RelativeLayout rlPhone;
    @Bind(R.id.rl_pws)
    RelativeLayout rlPws;
//    @Bind(R.id.rl_wallet)
//    RelativeLayout rlWallet;
    @Bind(R.id.rl_about)
    RelativeLayout rlAbout;
    @Bind(R.id.tv_exit_login)
    TextView tvExitLogin;
    private Handler handler;

    private AccountEntity account;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        App.getInstance().addTempActivity(this);
//        findViewById(R.id.iv_back).setOnClickListener(this);
//        findViewById(R.id.tv_exit_login).setOnClickListener(this);
//        findViewById(R.id.rl_about).setOnClickListener(this);
        handler = new Handler();
        resources = getResources();
        account = App.getInstance().getAccountEntity();
        initView();
    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_back:
//                finish();
//                break;
//            case R.id.tv_exit_login:    //退出登录
////                OkManager.getInstance().doGet(HttpUrl.LOGOUT, new OkManager.Funcl() {
////                    @Override
////                    public void onResponse(String result) {
////                        L.i(result);
////                        BaseResult bean = JSON.parseObject(result, BaseResult.class);
////                        if (bean.getCode() == 0) {
////                            App.getInstance().logout();
////                            handler.postDelayed(new Runnable() {
////                                @Override
////                                public void run() {
////                                    openActivity(MainActivity.class);
////                                    App.getInstance().exit();
////                                }
////                            }, 100);
////                        }else {
////                            showToast(bean.getMessage());
////                        }
////                    }
////
////                    @Override
////                    public void onFailure() {
////
////                    }
////                });
//                App.getInstance().logout();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        App.getInstance().logout();
//                        openActivity(MainActivity.class);
//                        App.getInstance().exit();
//                    }
//                }, 100);
//                break;
//            case R.id.rl_about:
//                break;
//        }
//    }

    private void initView() {
        String mbilePhone = account.getMobilePhone();
        if (TextUtils.isEmpty(mbilePhone)) {
            tvBind.setText("未绑定");
            tvBind.setTextColor(resources.getColor(R.color.text_color_gray_a));
            isBind = false;
        } else {
            tvBind.setText("已绑定");
            tvBind.setTextColor(resources.getColor(R.color.red_fuwa));
            isBind = true;
        }
    }

    private boolean isBind = false;

    @OnClick({R.id.iv_back, R.id.rl_cache, R.id.rl_phone, R.id.rl_pws, R.id.rl_about, R.id.tv_exit_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_cache:
                break;
//            case R.id.rl_auth:
//                break;
            case R.id.rl_phone:
                if (isBind) {
                    openActivity(VerifyPhoneActivity.class);
                } else {
                    openActivity(BindPhoneActivity.class);
                }
                break;
            case R.id.rl_pws:
                openActivity(ChangePwsActivity.class);
                break;
//            case R.id.rl_wallet:
//                openActivity(WalletActivity.class);
//                break;
            case R.id.rl_about:
                openActivity(AboutUsActivity.class);
                break;
            case R.id.tv_exit_login:
                App.getInstance().logout();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        App.getInstance().logout();
                        openActivity(MainActivity.class);
                        App.getInstance().exit();
                    }
                }, 100);
                break;
        }
    }

    @Subscribe
    public void onMessageEvent(ActionEntity event) {
        if (event != null) {
            String action = event.getAction();
            String tag = (String) event.getData();
            if (action.equals(Constants.Action.PHONE_BIND_STATE)) {
                account = App.getInstance().getAccountEntity();
                initView();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
