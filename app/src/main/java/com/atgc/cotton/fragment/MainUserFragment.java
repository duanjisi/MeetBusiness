package com.atgc.cotton.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.production.other.OtherProActivity;
import com.atgc.cotton.adapter.MainUserAdapter;
import com.atgc.cotton.entity.BaseSUser;
import com.atgc.cotton.entity.SUserEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.SearchUserRequest;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017-10-23.
 */
public class MainUserFragment extends BaseFragment implements AbsListView.OnScrollListener {
    private static final String TAG = MainUserFragment.class.getSimpleName();
    private ListView listView;
    private MainUserAdapter adapter;
    private boolean isEnd = false;
    private int page = 1;
    private int pageNum = 10;
    private String keyWords = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new MainUserAdapter(getContext());
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(new ItemClickListener());
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SUserEntity entity = (SUserEntity) adapterView.getItemAtPosition(i);
            if (entity != null) {
                Intent intent = new Intent(getContext(), OtherProActivity.class);
                intent.putExtra("userid", entity.getUserId());
                startActivity(intent);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 当不滚动时
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                //加载更多功能的代码
                requestMoreData();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    public void refresh(String keywords) {
        page = 1;
        if (!TextUtils.isEmpty(keywords)) {
            this.keyWords = keywords;
            SearchUserRequest request = new SearchUserRequest(TAG, keywords, "" + page, "" + pageNum);
            request.send(new BaseDataRequest.RequestCallback<BaseSUser>() {
                @Override
                public void onSuccess(BaseSUser pojo) {
                    initData(pojo);
                }

                @Override
                public void onFailure(String msg) {
                    cancelLoadingDialog();
                }
            });
        }
    }

    private void initData(BaseSUser baseSUser) {
        if (baseSUser != null) {
            ArrayList<SUserEntity> list = baseSUser.getData();
            int size = list.size();
            if (list != null && list.size() != 0) {
                if (size != pageNum) {
                    isEnd = true;
                    showToast("数据加载完成!", true);
                }
                adapter.initData(list);
            }
        }
    }


    private void requestMoreData() {
        if (!TextUtils.isEmpty(keyWords)) {
            if (isEnd) {
                return;
            }
            page++;
            SearchUserRequest request = new SearchUserRequest(TAG, keyWords, "" + page, "" + pageNum);
            request.send(new BaseDataRequest.RequestCallback<BaseSUser>() {
                @Override
                public void onSuccess(BaseSUser pojo) {
                    addData(pojo);
                }

                @Override
                public void onFailure(String msg) {
                    cancelLoadingDialog();
                }
            });
        }
    }

    private void addData(BaseSUser baseSUser) {
        if (baseSUser != null) {
            ArrayList<SUserEntity> list = baseSUser.getData();
            int size = list.size();
            if (list != null && list.size() != 0) {
                if (size != pageNum) {
                    isEnd = true;
                    showToast("数据加载完成!", true);
                }
                adapter.addData(list);
            }
        }
    }
}
