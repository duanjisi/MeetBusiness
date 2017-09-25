package com.atgc.cotton.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.atgc.cotton.R;
import com.atgc.cotton.adapter.ConversationAdapter;
import com.atgc.cotton.entity.BaseConversation;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017-08-22.
 * 私信
 */
public class ImMsgFragment extends BaseFragment {
    private RelativeLayout rl_no_data;
    private ListView listView;
    private ConversationAdapter adapter;
    private PopupWindow popupWindow;
    private Handler handler = new Handler();
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_im_msg, null);
            initViews(view);
        }
        return view;
    }

    private void initViews(View view) {
        rl_no_data = (RelativeLayout) view.findViewById(R.id.rl_no_datas);
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new ConversationAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ItemClickListner());
        listView.setOnItemLongClickListener(new ItemLongClickListener());
        initDatas();
    }

    private void initDatas() {
        ArrayList<BaseConversation> list = new ArrayList<>();
        BaseConversation conversation1 = new BaseConversation("张三", "http://img4.imgtn.bdimg.com/it/u=3676518267,2718469511&fm=27&gp=0.jpg", "你好吗？");
        BaseConversation conversation2 = new BaseConversation("李四", "http://img4.imgtn.bdimg.com/it/u=4087330161,2917575671&fm=27&gp=0.jpg", "到哪里了？");
        BaseConversation conversation3 = new BaseConversation("王五", "http://img5.imgtn.bdimg.com/it/u=464687734,2663254471&fm=27&gp=0.jpg", "过得潇洒啊");
        BaseConversation conversation4 = new BaseConversation("赵六", "http://img1.imgtn.bdimg.com/it/u=1005258935,19647458&fm=27&gp=0.jpg", "得了吧");
        BaseConversation conversation5 = new BaseConversation("麻子", "http://img2.imgtn.bdimg.com/it/u=2081844754,3710086262&fm=27&gp=0.jpg", "好美哦");

        list.add(conversation1);
        list.add(conversation2);
        list.add(conversation3);
        list.add(conversation4);
        list.add(conversation5);

        adapter.initData(list);
    }


    private class ItemClickListner implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            BaseConversation entity = (BaseConversation) adapterView.getItemAtPosition(i);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null && view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
