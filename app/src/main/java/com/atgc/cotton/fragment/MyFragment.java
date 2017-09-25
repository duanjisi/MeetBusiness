package com.atgc.cotton.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.atgc.cotton.App;
import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.Session;
import com.atgc.cotton.adapter.OnlineMusicAdapter;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.entity.ActionEntity;
import com.atgc.cotton.entity.BaseOnline;
import com.atgc.cotton.entity.OnlineVoiceEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.OnlineMusicRequest;
import com.atgc.cotton.util.SoundUtil2;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MyFragment extends BaseFragment {
    private static final String TAG = MyFragment.class.getSimpleName();
    private static final int PAGE_NUM = 8;
    private int page = 1;
    private AccountEntity account;
    private Button btnRefresh;
    private String cateid;
    private View view;
    private LRecyclerView recyclerView;
    protected LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private TextView tv_empty;
    private OnlineMusicAdapter adapter;
    private List<OnlineVoiceEntity> dataList;
    private String filePath;

    public static MyFragment newInstance(String title) {
        MyFragment fragment = new MyFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("cateid", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这里我只是简单的用num区别标签，其实具体应用中可以使用真实的fragment对象来作为叶片
        cateid = getArguments() != null ? getArguments().getString("cateid") : "";
    }

    /**
     * 为Fragment加载布局时调用
     **/
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_pager_list, null);
            initViews(view);
        }
        return view;
    }

    private void initViews(View view) {
        account = App.getInstance().getAccountEntity();
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        recyclerView = (LRecyclerView) view.findViewById(R.id.list);
        recyclerView.setEmptyView(tv_empty);
        // recyclerView.setPullRefreshEnabled(false);
        recyclerView.setHeaderViewColor(R.color.material_dark, R.color.material, android.R.color.white);
        recyclerView.setRefreshProgressStyle(ProgressStyle.Pacman); //设置下拉刷新Progress的样式
        recyclerView.setFooterViewHint("拼命加载中", "我是有底线的", "网络不给力啊，点击再试一次吧");
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new OnlineMusicAdapter(getActivity(), new itemClickListener(), cateid);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        recyclerView.setAdapter(mLRecyclerViewAdapter);
        recyclerView.setLoadMoreEnabled(true);
        recyclerView.refreshComplete(20);
        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.refreshComplete(20);
                        requestMusices();
                    }
                }, 1000);
            }
        });
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestMore();
                    }
                }, 1000);
            }
        });
        requestMusices();
    }

    private class itemClickListener implements OnlineMusicAdapter.OnItemClickListener {
        @Override
        public void ItemClick(OnlineVoiceEntity entity) {
            if (entity != null) {
//                SoundUtil2.getInstance().playRecorder(getContext(), entity.getMusicUrl(), new SoundUtil2.CompletionListener() {
//                    @Override
//                    public void onCompletion() {
//                        List<OnlineVoiceEntity> list = adapter.getDatas();
//                        for (OnlineVoiceEntity bean : list) {
//                            bean.setChecked(false);
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//                });
                List<OnlineVoiceEntity> list = adapter.getDatas();
                for (OnlineVoiceEntity bean : list) {
                    if (entity.getMid().equals(bean.getMid())) {
                        bean.setChecked(true);
                    } else {
                        bean.setChecked(false);
                    }
                }
                adapter.notifyDataSetChanged();
                Session.getInstance().refreshList(cateid);
                EventBus.getDefault().post(new ActionEntity(Constants.Action.SELECTED_CURRENT_MUSIC, SoundUtil2.getEncodeUrl(entity.getMusicUrl())));
            }
        }
    }

    private void requestMusices() {
        if (TextUtils.isEmpty(cateid)) {
            return;
        }
        page = 1;
        OnlineMusicRequest request = new OnlineMusicRequest(TAG, cateid, "" + page, "" + PAGE_NUM);
        request.send(new BaseDataRequest.RequestCallback<BaseOnline>() {
            @Override
            public void onSuccess(BaseOnline pojo) {
                initDatas(pojo);
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    private void initDatas(BaseOnline baseOnline) {
        if (baseOnline != null) {
            ArrayList<OnlineVoiceEntity> list = baseOnline.getData();
            int size = list.size();
            if (list != null && size != 0) {
                if (dataList == null) {
                    dataList = new ArrayList<>();
                }
                if (size == PAGE_NUM) {
                    page++;
                    recyclerView.setNoMore(false);
                } else {
                    recyclerView.setNoMore(true);
                }
                if (dataList.size() > 0) {
                    dataList.clear();
                }
                dataList.addAll(list);
                adapter.setDataList(dataList);
            } else {
                recyclerView.setNoMore(true);
            }
        }
    }

    private void requestMore() {
        if (TextUtils.isEmpty(cateid)) {
            return;
        }
        OnlineMusicRequest request = new OnlineMusicRequest(TAG, cateid, "" + page, "" + PAGE_NUM);
        request.send(new BaseDataRequest.RequestCallback<BaseOnline>() {
            @Override
            public void onSuccess(BaseOnline pojo) {
                bindDatas(pojo);
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    private void bindDatas(BaseOnline baseOnline) {
        if (baseOnline != null) {
            ArrayList<OnlineVoiceEntity> list = baseOnline.getData();
            int size = list.size();
            if (list != null && size != 0) {
                if (dataList == null) {
                    dataList = new ArrayList<>();
                }
                if (size == PAGE_NUM) {
                    recyclerView.setNoMore(false);
                    page++;
                } else {
                    recyclerView.setNoMore(true);
                }
                dataList.addAll(list);
                adapter.addAll(dataList);
            } else {
                recyclerView.setNoMore(true);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) view.getParent()).removeView(view);
    }
}