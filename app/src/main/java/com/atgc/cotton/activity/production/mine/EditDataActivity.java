package com.atgc.cotton.activity.production.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.App;
import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.config.LoginStatus;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.entity.ActionEntity;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

/**
 * Created by Johnny on 2017/7/5.
 * 编辑资料
 */
public class EditDataActivity extends BaseActivity implements View.OnClickListener {
    private int ICON_CHANGE_REQUEST = 101;
    private int NAME_SEX_SIGNATURE_REQUEST = 102;
    private ImageView iv_back;
    private TextView tv_copy;
    private RelativeLayout rl_avatar, rl_nick, rl_sex, rl_id, rl_qr_code, rl_intro;
    private CircleImageView iv_avatar;
    private TextView tv_nick, tv_sex, tv_id, tv_intro;
    private String headUrl;
    private AccountEntity account;
    private ImageLoader imageLoader;
    private boolean isChange = false;
    private boolean isSignatureNull = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        initViews();
    }

    private void initViews() {
        account = App.getInstance().getAccountEntity();
        headUrl = account.getAvatar();
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_copy = (TextView) findViewById(R.id.tv_tag);
        iv_avatar = (CircleImageView) findViewById(R.id.iv_avatar);

        rl_avatar = (RelativeLayout) findViewById(R.id.rl_avatar);
        rl_nick = (RelativeLayout) findViewById(R.id.rl_nick);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        rl_id = (RelativeLayout) findViewById(R.id.rl_id);
        rl_qr_code = (RelativeLayout) findViewById(R.id.rl_qr_code);
        rl_intro = (RelativeLayout) findViewById(R.id.rl_intro);

        tv_nick = (TextView) findViewById(R.id.tv_nick);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_intro = (TextView) findViewById(R.id.tv_intro);

        iv_back.setOnClickListener(this);
        rl_avatar.setOnClickListener(this);
        rl_nick.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_qr_code.setOnClickListener(this);
        rl_intro.setOnClickListener(this);
        tv_copy.setOnClickListener(this);
        bindView();
    }

    private void bindView() {
        if (!TextUtils.isEmpty(headUrl)) {
            imageLoader.displayImage(headUrl, iv_avatar, ImageLoaderUtils.getDisplayImageOptions());
        }
        tv_nick.setText(account.getUserName());
        String sex = account.getSex();
        if (sex.equals("1")) {
            tv_sex.setText("男");
        } else if (sex.equals("2")) {
            tv_sex.setText("女");
        } else {
            sex.equals("");
        }
        tv_id.setText(account.getUserId());
        String Sign=account.getSignature();
        if (!TextUtils.isEmpty(Sign)){
            isSignatureNull=false;
            tv_intro.setText(Sign);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_tag://点击复制用户ID

                break;
            case R.id.rl_avatar:
                Bundle bundle0 = new Bundle();
                bundle0.putString("head", headUrl);
                openActvityForResult(PersonalIconActivity.class, ICON_CHANGE_REQUEST, bundle0);
                break;
            case R.id.rl_nick:
                Bundle bundle = new Bundle();
                bundle.putString("changeType", "name");
                String name = tv_nick.getText().toString();
                bundle.putString("changeValue", name);
                openActvityForResult(PersonalInfoChangeActivity.class, NAME_SEX_SIGNATURE_REQUEST, bundle);
                break;
            case R.id.rl_sex:
                Bundle bundle1 = new Bundle();
                bundle1.putString("changeType", "sex");
                String sex = tv_sex.getText().toString();
                bundle1.putString("changeValue", sex);
                openActvityForResult(PersonalInfoChangeActivity.class, NAME_SEX_SIGNATURE_REQUEST, bundle1);
                break;
            case R.id.rl_qr_code:
                openActivity(QrCodeActivity.class);
                break;
            case R.id.rl_intro:
                Bundle bundle2 = new Bundle();
                bundle2.putString("changeType", "signature");
                String signature = tv_intro.getText().toString();
                bundle2.putString("changeValue", signature);
                bundle2.putBoolean("isNull", isSignatureNull);
                openActvityForResult(PersonalInfoChangeActivity.class, NAME_SEX_SIGNATURE_REQUEST, bundle2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ICON_CHANGE_REQUEST && resultCode == RESULT_OK && data != null) {
            LoginStatus sLoginStatus = LoginStatus.getInstance();
            headUrl = data.getStringExtra("headicon");
            if (!TextUtils.isEmpty(headUrl)) {
                isChange = true;
                sLoginStatus.setAvatar(headUrl);
                imageLoader.displayImage(headUrl, iv_avatar,
                        ImageLoaderUtils.getDisplayImageOptions());
                EventBus.getDefault().post(new ActionEntity(Constants.Action.UPDATE_ACCOUNT_INFORM));
            }
        } else if (requestCode == NAME_SEX_SIGNATURE_REQUEST && resultCode == RESULT_OK && data != null) {
            String changeType = data.getStringExtra("changeType");
            if (!TextUtils.isEmpty(changeType)) {
                LoginStatus sLoginStatus = LoginStatus.getInstance();
                String value = data.getStringExtra("back_value");
                isChange = true;
                switch (changeType) {
                    case "name":
                        tv_nick.setText("" + value);
                        sLoginStatus.setUser_name(value);
                        break;
                    case "sex":
                        tv_sex.setText("" + value);
                        sLoginStatus.setSex(value);
                        break;
                    case "signature":
                        if (!TextUtils.isEmpty(value)) {
                            tv_intro.setText("" + value);
                            isSignatureNull = false;
                            sLoginStatus.setIntro(value);
                        } else {
                            isSignatureNull = true;
                            tv_intro.setText(getString(R.string.not_filled));
                            sLoginStatus.setIntro(getString(R.string.not_filled));
                        }
                        break;
                }
                EventBus.getDefault().post(new ActionEntity(Constants.Action.UPDATE_ACCOUNT_INFORM));
            }
        }
    }
}
