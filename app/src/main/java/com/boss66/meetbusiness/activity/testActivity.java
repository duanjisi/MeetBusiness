package com.boss66.meetbusiness.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.boss66.meetbusiness.App;
import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.activity.base.BaseActivity;
import com.boss66.meetbusiness.entity.AccountEntity;
import com.boss66.meetbusiness.http.BaseDataRequest;
import com.boss66.meetbusiness.http.request.LoginRequest;
import com.boss66.meetbusiness.http.request.ModifyNickRequest;
import com.boss66.meetbusiness.http.request.PraiseRequest;
import com.boss66.meetbusiness.http.request.UnPraiseRequest;
import com.boss66.meetbusiness.http.request.UserInfosRequest;

/**
 * Created by Johnny on 2017/6/23.
 */
public class testActivity extends BaseActivity {
    private static final String TAG = testActivity.class.getSimpleName();
    private Button btnGet, btnPost, btnPut, btnDelete, btnLogin, btnPraise, btnUnpraise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btnGet = (Button) findViewById(R.id.tv_get);
        btnPost = (Button) findViewById(R.id.tv_post);
        btnPut = (Button) findViewById(R.id.tv_put);
        btnDelete = (Button) findViewById(R.id.tv_delete);
        btnLogin = (Button) findViewById(R.id.tv_get);
        btnPraise = (Button) findViewById(R.id.tv_praise);
        btnUnpraise = (Button) findViewById(R.id.tv_unpraise);

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestGet();
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPost();
            }
        });
        btnPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPut();
            }
        });

        btnPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPraise();
            }
        });

        btnUnpraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestUnpraise();
            }
        });
    }


    private void requestGet() {
        showLoadingDialog();
        UserInfosRequest request = new UserInfosRequest(TAG, "100000078");
        request.send(new BaseDataRequest.RequestCallback<AccountEntity>() {
            @Override
            public void onSuccess(AccountEntity pojo) {
                cancelLoadingDialog();
                showToast(pojo.toString(), true);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void requestPost() {
        showLoadingDialog();
        LoginRequest request = new LoginRequest(TAG, "13662541671", "123456");
        request.send(new BaseDataRequest.RequestCallback<AccountEntity>() {
            @Override
            public void onSuccess(AccountEntity pojo) {
                cancelLoadingDialog();
                App.getInstance().initUser(pojo);
                showToast(pojo.toString(), true);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void requestPut() {
        showLoadingDialog();
        ModifyNickRequest request = new ModifyNickRequest(TAG, "段sdfjskdl");
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                showToast(pojo.toString(), true);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void requestPraise() {
        showLoadingDialog();
        PraiseRequest request = new PraiseRequest(TAG, "34");
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                showToast("点赞成功!", true);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void requestUnpraise() {
        showLoadingDialog();
        UnPraiseRequest request = new UnPraiseRequest(TAG, "34");
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                cancelLoadingDialog();
                showToast("取消点赞成功!", true);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }
}
