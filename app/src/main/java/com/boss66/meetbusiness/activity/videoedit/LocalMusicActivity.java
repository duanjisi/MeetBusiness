package com.boss66.meetbusiness.activity.videoedit;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.activity.base.BaseActivity;
import com.boss66.meetbusiness.adapter.LocalMusicAdapter;
import com.boss66.meetbusiness.entity.LocalVoiceEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMARUnity on 2017/6/30.
 */
public class LocalMusicActivity extends BaseActivity {

    private TextView tv_back, tv_title, tv_ok;
    private RecyclerView rv_content;
    private LocalMusicAdapter adapter;
    private List<LocalVoiceEntity> list;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("filePath", filePath);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("选择本地视频");
        rv_content = (RecyclerView) findViewById(R.id.rv_content);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置布局管理器
        rv_content.setLayoutManager(layoutManager);

        rv_content.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= list.size()) {
                    return;
                }

                LocalVoiceEntity vEntty = list.get(position);
                if (vEntty != null) {
                    filePath = vEntty.filePath;
//                    Log.i("filePath:", filePath);
//                    Intent intent = new Intent();
//                    intent.putExtra("filePath", filePath);
//                    setResult(RESULT_OK, intent);
//                    finish();
//                    SharedPreferences sp = getSharedPreferences("voice", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putString("path", filePath);
//                    editor.commit();
                }
            }

            @Override
            public void onLongClick(View view, int posotion) {

            }
        }));
        adapter = new LocalMusicAdapter(this);
        adapter.setDatas(list);
        rv_content.setAdapter(adapter);
        new SearchThead().start();
    }

    private void getVideoFile() {
        ContentResolver mContentResolver = getContentResolver();
        Cursor cursor = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // ID:MediaStore.Audio.Media._ID
                int id = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media._ID));

                // title：MediaStore.Audio.Media.TITLE
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                // path：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                String mineType = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                // duration：MediaStore.Audio.Media.DURATION
                int duration = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                // 大小：MediaStore.Audio.Media.SIZE
                int size = (int) cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                if (size > 0) {
                    LocalVoiceEntity entty = new LocalVoiceEntity();
                    entty.ID = id;
                    entty.title = title;
                    entty.filePath = url;
                    entty.duration = duration;
                    entty.size = size;
                    list.add(entty);
                }
            } while (cursor.moveToNext());

        }
        if (cursor != null) {
            cursor.close();
            mHandler.sendEmptyMessage(1);
            cursor = null;
        }
    }

    class SearchThead extends Thread {
        @Override
        public void run() {
            getVideoFile();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && list != null) {
                LocalVoiceEntity vEntty = list.get(0);
                if (vEntty != null) {
                    filePath = vEntty.filePath;
                }
                adapter.notifyDataSetChanged();
            }
        }
    };

    private static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private View childView;
        private RecyclerView touchView;

        public RecyclerItemClickListener(Context context, final OnItemClickListener mListener) {
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent ev) {
                    if (childView != null && mListener != null) {
                        mListener.onItemClick(childView, touchView.getChildPosition(childView));
                    }
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent ev) {
                    if (childView != null && mListener != null) {
                        mListener.onLongClick(childView, touchView.getChildPosition(childView));
                    }
                }
            });
        }

        GestureDetector mGestureDetector;

        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            mGestureDetector.onTouchEvent(motionEvent);
            childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            touchView = recyclerView;
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onLongClick(View view, int posotion);
        }
    }

}
