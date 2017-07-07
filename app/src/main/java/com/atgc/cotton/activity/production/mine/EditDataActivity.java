package com.atgc.cotton.activity.production.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.widget.CircleImageView;

/**
 * Created by Johnny on 2017/7/5.
 * 编辑资料
 */
public class EditDataActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_save, tv_copy;
    private RelativeLayout rl_avatar, rl_nick, rl_sex, rl_id, rl_qr_code, rl_intro;
    private CircleImageView iv_avatar;
    private TextView tv_nick, tv_sex, tv_id, tv_intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_copy = (TextView) findViewById(R.id.tv_tag);
        iv_avatar = (CircleImageView) findViewById(R.id.iv_avatar);

        rl_avatar = (RelativeLayout) findViewById(R.id.rl_nick);
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
        tv_save.setOnClickListener(this);
        rl_avatar.setOnClickListener(this);
        rl_nick.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_qr_code.setOnClickListener(this);
        rl_intro.setOnClickListener(this);
        tv_copy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:

                break;
            case R.id.tv_tag://点击复制用户ID

                break;
            case R.id.rl_avatar:

                break;
            case R.id.rl_nick:

                break;
            case R.id.rl_sex:

                break;
            case R.id.rl_qr_code:

                break;
            case R.id.rl_intro:

                break;
        }
    }
}
