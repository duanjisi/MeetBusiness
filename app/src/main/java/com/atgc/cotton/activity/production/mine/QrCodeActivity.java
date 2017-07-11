package com.atgc.cotton.activity.production.mine;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.MakeQRCodeUtil;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Johnny on 2017/7/5.
 * 我的二维码
 */
public class QrCodeActivity extends BaseActivity {
    private ImageView iv_back, iv_share, iv_qr_code;
    private CircleImageView avatar;
    private TextView tv_name, tv_id;
    private ImageLoader imageLoader;
    private int screenW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        initViews();
    }

    private void initViews() {
        imageLoader = ImageLoaderUtils.createImageLoader(context);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        avatar = (CircleImageView) findViewById(R.id.iv_head);
        iv_qr_code = (ImageView) findViewById(R.id.iv_qcode);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_id = (TextView) findViewById(R.id.tv_id);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        screenW = UIUtils.getScreenWidth(QrCodeActivity.this) * 3 / 5;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) iv_qr_code.getLayoutParams();
        linearParams.height = screenW;
        linearParams.width = screenW;
        iv_qr_code.setLayoutParams(linearParams);

        AccountEntity sAccount = App.getInstance().getAccountEntity();
        String user_id = sAccount.getUserId();
        String url = "https://api.66boss.com/web/download?uid=" + user_id;
        MakeQRCodeUtil.createQRImage(url, screenW, screenW, iv_qr_code);
        String headicon = sAccount.getAvatar();
        imageLoader.displayImage(headicon, avatar,
                ImageLoaderUtils.getDisplayImageOptions());
        String user_name = sAccount.getUserName();
        tv_name.setText("" + user_name);
        String sex = sAccount.getSex();
        Drawable nav_up = null;
        if (sex.equals("0")) {
            nav_up = getResources().getDrawable(R.drawable.works_man);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        } else {
            nav_up = getResources().getDrawable(R.drawable.works_lady);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        }
        tv_name.setCompoundDrawables(null, null, nav_up, null);
        tv_id.setText(sAccount.getUserId());
    }
}
