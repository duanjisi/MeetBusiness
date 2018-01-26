package com.atgc.cotton.activity.im;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.atgc.cotton.R;
import com.atgc.cotton.Session;
import com.atgc.cotton.SessionInfo;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.adapter.ConversationAdapter;
import com.atgc.cotton.db.dao.ConversationHelper;
import com.atgc.cotton.entity.BaseConversation;
import com.atgc.cotton.util.PrefKey;
import com.atgc.cotton.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2017-09-21.
 */
public class PrivateLetterActivity extends BaseActivity implements Observer {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_msg)
    ImageView ivMsg;
    @BindView(R.id.listView)
    ListView listView;
    private ConversationAdapter adapter;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_letter);
        Session.getInstance().addObserver(this);
        ButterKnife.bind(this);
        initViews();
        initDatas();
    }

    private void initViews() {
        adapter = new ConversationAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ItemClickListner());
        listView.setOnItemLongClickListener(new ItemLongClickListener());
    }

    private void initDatas() {
        ArrayList<BaseConversation> list = (ArrayList<BaseConversation>) ConversationHelper.getInstance().query();
        if (list != null && list.size() != 0) {
            adapter.initData(list);
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_msg:
                openActivity(ContactsActivity.class);
                break;
        }
    }

    private class ItemClickListner implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            BaseConversation entity = (BaseConversation) adapterView.getItemAtPosition(i);
            if (entity != null) {
                String key = PrefKey.UN_READ_NEWS_KEY + "/" + entity.getConversation_id();
                PreferenceUtils.putInt(context, key, 0);

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("toUid", entity.getConversation_id());
                intent.putExtra("title", entity.getUser_name());
                intent.putExtra("toAvatar", entity.getAvatar());
                startActivity(intent);
            }
        }
    }

    private class ItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            BaseConversation entity = (BaseConversation) adapterView.getItemAtPosition(i);
            if (entity != null) {

            }
            return true;
        }
    }

    private void freshPager() {
        ArrayList<BaseConversation> list = (ArrayList<BaseConversation>) ConversationHelper.getInstance().query();
        Log.i("info", "list:" + list + "list.size()" + "" + list.size());
        if (list != null && list.size() != 0) {
            adapter.initData(list);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        SessionInfo sin = (SessionInfo) o;
        if (sin.getAction() == Session.ACTION_REFRSH_CONVERSATION_PAGE) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    freshPager();
                }
            });
        }
    }
}
