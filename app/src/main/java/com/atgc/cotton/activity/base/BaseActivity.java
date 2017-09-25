package com.atgc.cotton.activity.base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.LoginActivity;
import com.atgc.cotton.util.CommonDialogUtils;
import com.atgc.cotton.widget.DialogFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;


public class BaseActivity extends FragmentActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    // 进度条
    private ProgressDialog mProgressDialog;
    private LocalBroadcastReceiver mLocalBroadcastReceiver;
    public Context context;
    // 首先在您的Activity中添加如下成员变量
    final public UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        App.getInstance().addTempActivity(this);
        context = this;
    }

    protected void addQQQZonePlatform() {
        String appId = getString(R.string.qq_app_id);
        String appKey = getString(R.string.qq_app_key);
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
                appId, appKey);
        qqSsoHandler.setTargetUrl("http://www.umeng.com");
        qqSsoHandler.addToSocialSDK();
//        // 添加QZone平台
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
//                LoginActivity.this, appId, appKey);
//        qZoneSsoHandler.addToSocialSDK();
    }

    protected void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = getString(R.string.weixin_app_id);
        String appSecret = getString(R.string.weixin_app_secret);

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, appId,
                appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this,
                appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void onReceiveAction(Context context, Intent intent) {
    }

    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onReceiveAction(context, intent);
        }
    }

    public void openActivity(Class<?> clazz) {
        openActivity(clazz, null);
    }

    public void openActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void openActvityForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    public void openActvityForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    protected void setTextView(TextView tv, String str) {
        if (!TextUtils.isEmpty(str)) {
            tv.setText(str);
        }
    }

    /**
     * 显示加载进度条
     *
     * @return: void
     */
    protected void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog();
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        } else {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }

    protected void showComposingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = createComposeDialog();
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        } else {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog dialog = DialogFactory.createProgressDialog(this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setIndeterminate(true);
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.setCancelable(false);
        return dialog;
    }

    private ProgressDialog createComposeDialog() {
        ProgressDialog dialog = DialogFactory.createProgressDialog(this);
        dialog.setMessage(getString(R.string.compose));
        dialog.setIndeterminate(true);
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.setCancelable(false);
        return dialog;
    }

    /**
     * 取消加载进度条
     *
     * @return: void
     */
    protected void cancelLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void showTipsDialog() {
        String msg = "应用未登录,你确定登录么?";
        CommonDialogUtils.showDialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialogUtils.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(LoginActivity.class);
                CommonDialogUtils.dismiss();
                finish();
            }
        }, context, msg);
    }

    /**
     * @param resId  资源ID
     * @param length true为长时间，false为短时间
     * @return: void
     */
    protected void showToast(int resId, boolean length) {
        Toast.makeText(this, resId, length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    /**
     * @param msg    内容
     * @param length true为长时间，false为短时间
     * @return: void
     */
    protected void showToast(String msg, boolean length) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(this, msg, length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    protected String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        AppManager.getInstance().remove(this);
    }

    /**
     * @param msg 内容
     * @return: void
     */
    protected void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
