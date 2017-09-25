package com.atgc.cotton.activity.videoedit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.Constants;
import com.atgc.cotton.R;
import com.atgc.cotton.Session;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.adapter.OnlineMusicAdapter;
import com.atgc.cotton.adapter.ViewPagerFragmentAdapter;
import com.atgc.cotton.entity.ActionEntity;
import com.atgc.cotton.entity.BaseCate;
import com.atgc.cotton.entity.BaseOnline;
import com.atgc.cotton.entity.CateEntity;
import com.atgc.cotton.entity.OnlineVoiceEntity;
import com.atgc.cotton.fragment.MyFragment;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.OnlineMuCateRequest;
import com.atgc.cotton.http.request.SearchMusicRequest;
import com.atgc.cotton.util.SoundUtil;
import com.atgc.cotton.util.SoundUtil2;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.MyTabLayout;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Johnny on 2017-09-05.
 */
public class OnlineMusicActivity extends BaseActivity {
    private static final String TAG = OnlineMusicActivity.class.getSimpleName();
    private RelativeLayout rlSearch, rlTag;
    private TextView tvSearch, tvCancel;
    protected ImageButton clearSearch;
    protected EditText query;
    private InputMethodManager inputMethodManager;

    private LRecyclerView recyclerView;
    protected LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private TextView tv_empty;
    private OnlineMusicAdapter adapter;
    private List<OnlineVoiceEntity> dataList;
    private String keyWords;

    private View cateView;
    private RelativeLayout rlMain;
    private MyTabLayout tabLayout;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private List<CharSequence> listTitle;
    private List<Fragment> listData;
    private ViewPager viewPager;
    private ImageView iv_back;
    private TextView tvSave;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_music);
        EventBus.getDefault().register(this);
        initViews();
    }

    private void initViews() {
        listTitle = new ArrayList<>();
        listData = new ArrayList<>();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        rlSearch = (RelativeLayout) findViewById(R.id.rl_search);
        rlTag = (RelativeLayout) findViewById(R.id.rl_tag);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        tvCancel = (TextView) findViewById(R.id.tv_cancle);
        query = (EditText) findViewById(R.id.query);
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        tvSave = (TextView) findViewById(R.id.tv_save);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundUtil.getInstance().stopPlay();
                finish();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundUtil2.getInstance().stopPlay();
                Intent intent = new Intent();
                intent.putExtra("filePath", filePath);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.hindView(cateView);
                UIUtils.hindView(rlTag);
                UIUtils.showView(rlSearch);
                UIUtils.showView(rlMain);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.hindView(rlSearch);
                UIUtils.showView(rlTag);
                UIUtils.hindView(rlMain);
                UIUtils.showView(cateView);
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        query.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    keyWords = getText(query);
                    requestMusices();
                    return true;
                }
                return false;
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });
        cateView = findViewById(R.id.cate_view);
        rlMain = (RelativeLayout) findViewById(R.id.rl_main);
        tabLayout = (MyTabLayout) findViewById(R.id.tabLayout);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), listData, listTitle);

        viewPager.setAdapter(viewPagerFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        initUi();
        requestMusicCate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SoundUtil.getInstance().stopPlay();
    }

    private void initUi() {
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        recyclerView = (LRecyclerView) findViewById(R.id.list);
//        recyclerView.setEmptyView(tv_empty);
        // recyclerView.setPullRefreshEnabled(false);
        recyclerView.setHeaderViewColor(R.color.material_dark, R.color.material, android.R.color.white);
        recyclerView.setRefreshProgressStyle(ProgressStyle.Pacman); //设置下拉刷新Progress的样式
        recyclerView.setFooterViewHint("拼命加载中", "我是有底线的", "网络不给力啊，点击再试一次吧");
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new OnlineMusicAdapter(context, new itemClickListener(), "MainPager");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        recyclerView.setAdapter(mLRecyclerViewAdapter);
        recyclerView.setLoadMoreEnabled(false);
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
//        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                    }
//                }, 1000);
//            }
//        });
    }

    private class itemClickListener implements OnlineMusicAdapter.OnItemClickListener {
        @Override
        public void ItemClick(OnlineVoiceEntity entity) {
            if (entity != null) {
                List<OnlineVoiceEntity> list = adapter.getDatas();
                for (OnlineVoiceEntity bean : list) {
                    if (entity.getMid().equals(bean.getMid())) {
                        bean.setChecked(true);
                    } else {
                        bean.setChecked(false);
                    }
                }
                adapter.notifyDataSetChanged();
                Session.getInstance().refreshList("MainPager");
                EventBus.getDefault().post(new ActionEntity(Constants.Action.SELECTED_CURRENT_MUSIC, SoundUtil2.getEncodeUrl(entity.getMusicUrl())));
            }
        }
    }

    @Subscribe
    public void onMessageEvent(ActionEntity event) {
        if (event != null) {
            String action = event.getAction();
            String url = (String) event.getData();
            if (action.equals(Constants.Action.SELECTED_CURRENT_MUSIC)) {
                if (!TextUtils.isEmpty(url)) {
                    filePath = url;
                }
            }
        }
    }

    private void requestMusicCate() {
        showLoadingDialog();
        OnlineMuCateRequest request = new OnlineMuCateRequest(TAG);
        request.send(new BaseDataRequest.RequestCallback<BaseCate>() {
            @Override
            public void onSuccess(BaseCate pojo) {
                cancelLoadingDialog();
                initDatas(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
            }
        });
    }

    private void initDatas(BaseCate baseCate) {
        if (baseCate != null) {
            ArrayList<CateEntity> cates = baseCate.getData();
            if (cates != null && cates.size() != 0) {
                for (CateEntity entity : cates) {
                    listTitle.add(entity.getCatName());
                    listData.add(MyFragment.newInstance(entity.getId()));
                }

                //刷新页面
                if (viewPagerFragmentAdapter != null) {
                    viewPagerFragmentAdapter.setListTitle(listTitle);
                    viewPagerFragmentAdapter.setListData(listData);
                    viewPagerFragmentAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void requestMusices() {
        if (TextUtils.isEmpty(keyWords)) {
            showToast("关键字不能为空！", true);
            return;
        }
        SearchMusicRequest request = new SearchMusicRequest(TAG, keyWords);
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
//                if (size == PAGE_NUM) {
//                    page++;
//                    recyclerView.setNoMore(false);
//                } else {
//                    recyclerView.setNoMore(true);
//                }
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

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
