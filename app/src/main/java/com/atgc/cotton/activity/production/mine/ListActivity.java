package com.atgc.cotton.activity.production.mine;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.activity.production.other.OtherProActivity;
import com.atgc.cotton.adapter.MemberAdapter;
import com.atgc.cotton.entity.BaseMember;
import com.atgc.cotton.entity.MemberEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.MemberFansRequest;
import com.atgc.cotton.http.request.MemberFocusRequest;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2017-08-26.
 */
public class ListActivity extends BaseActivity {
    private static final String TAG = ListActivity.class.getSimpleName();
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.listview)
    ListView listview;
    private int page = 1;
    private int pageNum = 10;
    private MemberAdapter adapter;
    private int type;//1:关注，2:粉丝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        type = getIntent().getExtras().getInt("type", 0);
        if (type == 1) {
            tvTitle.setText("关注列表");
        } else if (type == 2) {
            tvTitle.setText("粉丝列表");
        }
        adapter = new MemberAdapter(context);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new ItemClickListener());
        request();
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            MemberEntity entity = (MemberEntity) adapterView.getItemAtPosition(i);
            if (entity != null) {
                Intent intent = new Intent(context, OtherProActivity.class);
                intent.putExtra("member", entity);
                startActivity(intent);
            }
        }
    }

    private void request() {
        BaseDataRequest request = null;
        if (type == 1) {
            request = new MemberFocusRequest(TAG, "" + page, "" + pageNum);
        } else {
            request = new MemberFansRequest(TAG, "" + page, "" + pageNum);
        }
        showLoadingDialog();
        request.send(new BaseDataRequest.RequestCallback<BaseMember>() {
            @Override
            public void onSuccess(BaseMember pojo) {
                cancelLoadingDialog();
                initDatas(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void initDatas(BaseMember baseMember) {
        if (baseMember != null) {
            ArrayList<MemberEntity> list = baseMember.getData();
            if (list != null && list.size() != 0) {
                adapter.initData(list);
            }
        }
    }
}
