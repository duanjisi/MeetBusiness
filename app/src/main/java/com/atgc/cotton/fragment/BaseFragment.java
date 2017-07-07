package com.atgc.cotton.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;
import com.atgc.cotton.R;
import com.atgc.cotton.widget.DialogFactory;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

public class BaseFragment extends Fragment {
    public boolean mIsFirstin = true;

    private ProgressDialog mProgressDialog;
    private LocalBroadcastReceiver mLocalBroadcastReceiver;
    // 首先在您的Activity中添加如下成员变量
    final public UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocalBroadcastReceiver = new LocalBroadcastReceiver();
    }

    protected void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog();
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        } else {
            Activity activity = getActivity();
            if (!mProgressDialog.isShowing()
                    && activity != null
                    && !activity.isFinishing()) {
                mProgressDialog.show();
            }
        }
    }


    private ProgressDialog createProgressDialog() {
        ProgressDialog dialog = DialogFactory.createProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.loading));
        dialog.setIndeterminate(true);
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.setCancelable(false);
        return dialog;
    }


    protected void cancelLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void showToast(int resId, boolean length) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), resId, length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        }
    }

    protected void showToast(String msg, boolean length) {
        if (getActivity() != null && !TextUtils.isEmpty(msg)) {
            Toast.makeText(getActivity(), msg, length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 注册本地广播器需要接收的Action
     *
     * @param action
     */
    protected void registerAction(String action) {
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mLocalBroadcastReceiver, new IntentFilter(action));
    }

    /**
     * 当接收到本地广播的时候调用的方法
     *
     * @param context
     * @param intent
     */
    protected void onReceiveAction(Context context, Intent intent) {
    }

    /**
     * 本地广播接收器
     */
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
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void openActvityForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivityForResult(intent, requestCode);
    }

    public void openActvityForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }
}
