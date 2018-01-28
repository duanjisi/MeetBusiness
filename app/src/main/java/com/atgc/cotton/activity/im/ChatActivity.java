package com.atgc.cotton.activity.im;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.Session;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.adapter.MessageAdapter;
import com.atgc.cotton.db.MessageDB;
import com.atgc.cotton.db.dao.ConversationHelper;
import com.atgc.cotton.entity.AccountEntity;
import com.atgc.cotton.entity.BaseConversation;
import com.atgc.cotton.entity.MessageItem;
import com.atgc.cotton.entity.Motion;
import com.atgc.cotton.service.ChatService;
import com.atgc.cotton.util.Emoji;
import com.atgc.cotton.util.PrefKey;
import com.atgc.cotton.util.PreferenceUtils;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.InputDetector;
import com.atgc.cotton.widget.MoreChatPop;
import com.atgc.cotton.xlistview.MsgListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Johnny on 2017-08-23.
 */
public class ChatActivity extends BaseActivity implements
        MoreChatPop.OnItemdListener,
        MsgListView.IXListViewListener,
        ChatService.receiveMessageCallback {
    public static final int NEW_MESSAGE = 0x001;// 收到消息
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_more)
    ImageView ivMore;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_write)
    TextView tvWrite;
    @Bind(R.id.msg_listView)
    MsgListView mMsgListView;
    private MoreChatPop moreChatPop;

    public static String defaulgUserName = "在飞";
    public static String defaulgIcon = "4";
    public static int defaultCount = 0;
    private App mApplication;
    private MessageDB mMsgDB;// 保存消息的数据库
    private static MessageAdapter adapter;// 发送消息展示的adapter
    private AccountEntity account;
    public static int MSGPAGERNUM;
    private String mMsgId;
    private String userid, toUid, title, toAvatar;//toUid;单聊（个人用户id）,群聊(群id)
    private PopupWindow popupWindow;
    /**
     * 接收到数据，用来更新listView
     */
    private Handler handler = new Handler() {
        // 接收到消息
        public void handleMessage(android.os.Message msg) {
            if (msg.what == NEW_MESSAGE) {
                MessageItem item = (MessageItem) msg.obj;
                adapter.upDateMsg(item);// 更新界面
                scrollToBottomListItem();
            }
        }
    };
    private InputDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_chat);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        // 消息
        mApplication = App.getInstance();
        account = mApplication.getAccountEntity();
        mMsgDB = mApplication.getMessageDB();// 发送数据库
        moreChatPop = new MoreChatPop(context);
        moreChatPop.setOnItemSelectedListener(this);
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        String token = account.getToken();
//        new SocketHandler("token=" + token, this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userid = account.getUserId();
            toUid = bundle.getString("toUid", "");
            title = bundle.getString("title", "");
            toAvatar = bundle.getString("toAvatar", "");
            mMsgId = userid + "_" + toUid;
        }
        tvTitle.setText(title);
        adapter = new MessageAdapter(context, initMsgData());
        mMsgListView = (MsgListView) findViewById(R.id.msg_listView);
        // 触摸ListView隐藏表情和输入法
//        mMsgListView.setOnTouchListener(this);
        mMsgListView.setPullLoadEnable(false);
        mMsgListView.setXListViewListener(this);
        mMsgListView.setAdapter(adapter);
        mMsgListView.setSelection(adapter.getCount() - 1);
    }

    /**
     * 加载消息历史，从数据库中读出
     */
    private List<MessageItem> initMsgData() {
        List<MessageItem> list = mMsgDB
                .getMsg(mMsgId, MSGPAGERNUM);
        List<MessageItem> msgList = new ArrayList<MessageItem>();// 消息对象数组
        if (list.size() > 0) {
            for (MessageItem entity : list) {
                if (entity.getName().equals("")) {
                    entity.setName(defaulgUserName);
                }
                if (entity.getHeadImg() < 0) {
                    entity.setHeadImg(defaultCount);
                }
                msgList.add(entity);
            }
        }
        return msgList;
    }

    private void sendTxtMessage(String msg) {//发文字
        // 发送消息
        MessageItem item = new MessageItem(
                MessageItem.MESSAGE_TYPE_TXT, "nick",
                System.currentTimeMillis(), msg, 0,
                false, 0, 0, "" + System.currentTimeMillis(),
                account.getUserName(), account.getUserId(), account.getAvatar());
        MessageItem data = mMsgDB.saveMsg(mMsgId, item);// 消息保存数据库
        adapter.upDateMsg(data);
        mMsgListView.setSelection(adapter.getCount() - 1);
        sendImMessage(msg);
        saveConversation(title, toAvatar, toUid, msg);
    }

    private void sendImMessage(String msg) {
        String json = getJsonText(msg);
        if (TextUtils.isEmpty(json)) {
            return;
        }
//        new SocketHandler(json, this);
        Motion motion = Motion.getInstance();
        motion.setAction(Session.ACTION_SEND_IM_MESSAGE);
        motion.setData(json);
        EventBus.getDefault().post(motion);
    }

    private String getJsonText(String msg) {
        JSONObject object = new JSONObject();
        try {
            object.put("target_type", "user");
            object.put("target", toUid);
            object.put("from", account.getUserId());

            JSONObject obj1 = new JSONObject();
            obj1.put("type", "txt");
            obj1.put("content", msg);
            object.put("msg", obj1);

            JSONObject obj2 = new JSONObject();
            obj2.put("sendername", account.getUserName());
            obj2.put("senderavatar", account.getAvatar());
            obj2.put("receivername", title);
            obj2.put("receiveravatar", toAvatar);
            object.put("ext", obj2);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveConversation(String name, String avatar, String userid, String content) {
        BaseConversation sation = new BaseConversation();
        sation.setUser_name(name);
        sation.setAvatar(avatar);
        sation.setConversation_id(userid);
        sation.setNewest_msg_time("" + System.currentTimeMillis());
        ConversationHelper.getInstance().save(sation);
        String msg = "我：" + content;
        String noticeKey = PrefKey.NEWS_NOTICE_KEY + "/" + userid;
        PreferenceUtils.putString(this, noticeKey, msg);
    }

    @OnClick({R.id.iv_back, R.id.iv_more, R.id.tv_write})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
                if (!isFinishing()) {
                    if (moreChatPop.isShowing()) {
                        moreChatPop.dismiss();
                    } else {
                        moreChatPop.show(getWindow().getDecorView());
                    }
                }
                break;
            case R.id.tv_write:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    showPopChat(context, getWindow().getDecorView());
                }
                break;
        }
    }

    @Override
    public void onItemClick(int type) {
        switch (type) {
            case 1:
//                showToast("清空聊天记录", true);
                mMsgDB.clearMsgDatas(mMsgId);
                MSGPAGERNUM = 0;
                List<MessageItem> msgList = initMsgData();
                int position = adapter.getCount();
                adapter.setmMsgList(msgList);
                mMsgListView.stopRefresh();
                mMsgListView.setSelection(adapter.getCount() - position - 1);
                break;
            case 2:
                showToast("加入黑名单", true);
                break;
        }
    }

    /**
     * @Description 滑动到列表底部
     */
    private void scrollToBottomListItem() {
        // todo eric, why use the last one index + 2 can real scroll to the
        // bottom?
        if (mMsgListView != null) {
            mMsgListView.setSelection(adapter.getCount() - 1);
        }
    }

    @Override
    public void onRefresh() {
        MSGPAGERNUM++;
        List<MessageItem> msgList = initMsgData();
        int position = adapter.getCount();
        adapter.setmMsgList(msgList);
        mMsgListView.stopRefresh();
        mMsgListView.setSelection(adapter.getCount() - position - 1);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatService.callbacks.add(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChatService.callbacks.remove(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getInstance().refreshConversationPager();
    }

    @Override
    public void onMessageReceive(MessageItem item, String fromUid) {
        if (item != null) {
            if (fromUid.equals(toUid)) {//接受到的uiserid 与该聊天的对象id一样
                android.os.Message msg = new android.os.Message();
                msg.what = NEW_MESSAGE;
                msg.obj = item;
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    public void onNotify(final String title, final String content) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                showToast(title, true);
            }
        });
    }

    @Override
    public void onNetChange(boolean isNetConnected) {

    }

//    private void initDetector() {
//        mDetector = InputDetector.with(this)
//                .setEmotionView(viewFace)
//                .BindToContent(findViewById(R.id.fl_content))
//                .BindToEditText(mEtMsg)
//                .BindToEmotionButton(ibFace)
//                .setMoreView(ll_other)
//                .BindMoreButton(mBtnAffix)
//                .build();
//    }


//    @Override
//    public void onBackPressed() {
//        if (!mDetector.interceptBackPress()) {
//            Log.i("info", "=================onBackPressed()");
//            super.onBackPressed();
//            if (!isFinishing()) {
//                finish();
//            }
//        }
//    }


    private InputMethodManager mInputMethodManager;

    private void showPopChat(final Context context, View parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.popwindow_item_im_chat, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style);

        int[] location = new int[2];
        parent.getLocationOnScreen(location);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getDrawableFromRes(R.drawable.bg_popwindow));
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        LinearLayout topArea = (LinearLayout) view.findViewById(R.id.top_area);
        final ImageView iv_chat_face = (ImageView) view.findViewById(R.id.iv_chat_face);
        final GridView gridView = (GridView) view.findViewById(R.id.grid);
//        mDetector = InputDetector.with(this)
//                .setEmotionView(gridView)
//                .BindToContent(findViewById(R.id.fl_content))
//                .BindToEditText(editText)
//                .BindToEmotionButton(iv_chat_face)
//                .build();
        int softHeight = PreferenceUtils.getInt(context, PrefKey.SORFT_HEIGHT_KEY, 0);
        if (softHeight == 0) {
            softHeight = UIUtils.getScreenHeight(context) / 2 - 50;
        }
        gridView.getLayoutParams().height = softHeight;
        EmojiAdapter adapter = new EmojiAdapter(context);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int emo = (int) adapterView.getItemAtPosition(i);
                String txt = getText(editText);
                String string = getEmojiStringByUnicode(emo);
                if (!TextUtils.isEmpty(string)) {
                    String str = txt + string;
                    editText.setText(str);
                    editText.setSelection(str.length());
                }
            }
        });

        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            imm.showSoftInput(editText, 0);
        } else {
            imm.showSoftInput((View) editText.getWindowToken(),
                    InputMethodManager.SHOW_FORCED);
        }
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gridView.setVisibility(View.GONE);
                mInputMethodManager.hideSoftInputFromWindow(
                        editText.getWindowToken(), 0);
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        topArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        iv_chat_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gridView.getVisibility() != View.VISIBLE) {
                    mInputMethodManager.hideSoftInputFromWindow(
                            editText.getWindowToken(), 0);
                    try {
                        Thread.sleep(80);// 解决此时会黑一下屏幕的问题
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    gridView.setVisibility(View.VISIBLE);
                }
            }
        });

        view.findViewById(R.id.btn_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String msg = editText.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    showToast("内容不能为空!", true);
                    return;
                }
                sendTxtMessage(msg);
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private Drawable getDrawableFromRes(int resId) {
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, resId);
        return new BitmapDrawable(bmp);
    }

    private class EmojiAdapter extends BaseAdapter {
        private Context context;
        private float mImageHeight;
        private int[] emos;

        public EmojiAdapter(Context context) {
            this.context = context;
            mImageHeight = (UIUtils.getScreenWidth(context) - UIUtils.dip2px(context, 80)) / 8;
            emos = Emoji.emos;
        }

        @Override
        public int getCount() {
            return emos.length;
        }

        @Override
        public Object getItem(int i) {
            return emos[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView imageView = new TextView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
            imageView.getLayoutParams().width = (int) mImageHeight;
            imageView.getLayoutParams().height = (int) mImageHeight;
            imageView.setText(getEmojiStringByUnicode(emos[i]));
            return imageView;
        }
    }

    private String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

}
