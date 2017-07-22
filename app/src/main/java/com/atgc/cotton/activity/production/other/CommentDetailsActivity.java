package com.atgc.cotton.activity.production.other;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.adapter.CommentAdapter;
import com.atgc.cotton.entity.BaseComment;
import com.atgc.cotton.entity.Comment;
import com.atgc.cotton.entity.VideoEntity;
import com.atgc.cotton.http.BaseDataRequest;
import com.atgc.cotton.http.request.ComOrReplyRequest;
import com.atgc.cotton.http.request.CommentsRequest;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/14.
 * 评论详情
 */
public class CommentDetailsActivity extends BaseActivity implements AbsListView.OnScrollListener {
    private static final String TAG = CommentDetailsActivity.class.getSimpleName();
    private static final int PAGE_SIZE = 10;
    private int page = 1;
    private boolean isEnd = false;
    private ImageView iv_back;
    private ListView listView;
    private EditText editText;
    private Button btn_comment;
    private CommentAdapter adapter;
    private VideoEntity videoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_details);
        initViews();
    }

    private void initViews() {
        videoEntity = (VideoEntity) getIntent().getExtras().getSerializable("obj");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.editText);
        btn_comment = (Button) findViewById(R.id.btn_comment);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCommentStr();
            }
        });
        adapter = new CommentAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        requestComment();
    }

    private void requestComment() {
        if (videoEntity == null) {
            return;
        }
        page = 1;
        CommentsRequest request = new CommentsRequest(TAG, videoEntity.getId(), "" + page, "" + PAGE_SIZE);
        request.send(new BaseDataRequest.RequestCallback<BaseComment>() {
            @Override
            public void onSuccess(BaseComment pojo) {
                initList(pojo);
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    private void initList(BaseComment baseComment) {
        if (baseComment != null) {
            ArrayList<Comment> comments = baseComment.getData();
            int size = comments.size();
            if (comments != null && size != 0) {
                if (size != PAGE_SIZE) {
                    isEnd = true;
                    showToast("数据加载完成!", true);
                }
                adapter.initData(comments);
            }
        }
    }

    private void requestMore() {
        if (videoEntity == null) {
            return;
        }
        if (isEnd) {
            return;
        }
        page++;
        CommentsRequest request = new CommentsRequest(TAG, videoEntity.getId(), "" + page, "" + PAGE_SIZE);
        request.send(new BaseDataRequest.RequestCallback<BaseComment>() {
            @Override
            public void onSuccess(BaseComment pojo) {
                bindDatas(pojo);
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    private void bindDatas(BaseComment baseComment) {
        if (baseComment != null) {
            ArrayList<Comment> comments = baseComment.getData();
            int size = comments.size();
            if (comments != null && size != 0) {
                if (size != PAGE_SIZE) {
                    isEnd = true;
                    showToast("数据加载完成!", true);
                }
                adapter.addData(comments);
            }
        }
    }

    private void requestCommentStr() {
        if (!App.getInstance().isLogin()) {
            showTipsDialog();
            return;
        }
        String content = getText(editText);
        if (TextUtils.isEmpty(content)) {
            showToast("评论内容不能为空!", true);
            return;
        }
        if (videoEntity == null) {
            return;
        }
        ComOrReplyRequest request = new ComOrReplyRequest(TAG, videoEntity.getId(), "0", content, videoEntity.getUserId());
        request.send(new BaseDataRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String pojo) {
                showToast("评论成功!", true);
                editText.setText("");
                requestComment();
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg, true);
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 当不滚动时
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                //加载更多功能的代码
//                showToast("滑动到底部了", true);
                requestMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
}
