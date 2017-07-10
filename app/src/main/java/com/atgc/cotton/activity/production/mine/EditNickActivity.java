package com.atgc.cotton.activity.production.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;

/**
 * Created by Johnny on 2017/7/5.
 * 编辑昵称
 */
public class EditNickActivity extends BaseActivity {
    private ImageView iv_back, iv_delete;
    private TextView tv_save;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nick);
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        tv_save = (TextView) findViewById(R.id.tv_save);
        editText = (EditText) findViewById(R.id.et_content);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
