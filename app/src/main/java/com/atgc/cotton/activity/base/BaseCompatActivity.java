package com.atgc.cotton.activity.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.atgc.cotton.R;
import com.atgc.cotton.presenter.BasePresenter;
import com.atgc.cotton.widget.DialogFactory;

/**
 * Created by GMARUnity on 2017/7/10.
 * AppCompatActivity
 */
public abstract class BaseCompatActivity<P extends BasePresenter> extends BaseActivity {
    // 进度条
    private ProgressDialog mProgressDialog;
    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    public void showLoading() {
        showLoadingDialog();
    }

    public void hideLoading() {
        cancelLoadingDialog();
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

    private ProgressDialog createProgressDialog() {
        ProgressDialog dialog = DialogFactory.createProgressDialog(this);
        dialog.setMessage(getString(R.string.loading));
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

    /**
     * @param str
     * @param length true为长时间，false为短时间
     * @return: void
     */
    protected void showToast(String str, boolean length) {
        Toast.makeText(this, str, length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }
}
