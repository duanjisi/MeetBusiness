package com.atgc.cotton.activity.im;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.adapter.MemberAdapter;
import com.atgc.cotton.entity.BaseMember;
import com.atgc.cotton.entity.MemberEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.MemberFocusRequest;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Johnny on 2017-08-22.
 * 联系人
 */
public class ContactsActivity extends BaseActivity implements AbsListView.OnScrollListener {
    private static final String TAG = ContactsActivity.class.getSimpleName();
    private String keyWords;
    private InputMethodManager inputMethodManager;
    private int page = 1;
    private int pageNum = 10;
    private MemberAdapter adapter;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.et_keywords)
    EditText etKeywords;
    @BindView(R.id.search_clear)
    ImageButton clearSearch;
    @BindView(R.id.listview)
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        title.setText("sdfsd发的啥地方" + getEmojiStringByUnicode(0x1F60A) + "第三方sdfsdf");
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(ChatActivity.class);
            }
        });
        hideSoftKeyboard();
        etKeywords.addTextChangedListener(new TextWatcher() {
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
        etKeywords.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    keyWords = getText(etKeywords);
                    requestSearch();
                    return true;
                }
                return false;
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etKeywords.getText().clear();
                hideSoftKeyboard();
            }
        });
        adapter = new MemberAdapter(context);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new ItemClickListener());
        listview.setOnScrollListener(this);
        request();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 当不滚动时
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                //加载更多功能的代码
                requestMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            MemberEntity entity = (MemberEntity) adapterView.getItemAtPosition(i);
            if (entity != null) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("toUid", entity.getUserId());
                intent.putExtra("title", entity.getUserName());
                intent.putExtra("toAvatar", entity.getAvatar());
                startActivity(intent);
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

    private void request() {
        BaseDataRequest request = new MemberFocusRequest(TAG, "" + page, "" + pageNum);
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

    private void requestMore() {
        page++;
        BaseDataRequest request = new MemberFocusRequest(TAG, "" + page, "" + pageNum);
        showLoadingDialog();
        request.send(new BaseDataRequest.RequestCallback<BaseMember>() {
            @Override
            public void onSuccess(BaseMember pojo) {
                cancelLoadingDialog();
                addDatas(pojo);
            }

            @Override
            public void onFailure(String msg) {
                cancelLoadingDialog();
                showToast(msg, true);
            }
        });
    }

    private void addDatas(BaseMember baseMember) {
        if (baseMember != null) {
            ArrayList<MemberEntity> list = baseMember.getData();
            if (list != null && list.size() != 0) {
                adapter.addData(list);
            }
        }
    }

    private void requestSearch() {
        if (TextUtils.isEmpty(keyWords)) {
            showToast("关键字为空!", true);
            return;
        }
        BaseDataRequest request = new MemberFocusRequest(TAG, keyWords);
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


    private String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
