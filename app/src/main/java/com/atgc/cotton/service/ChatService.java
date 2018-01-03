package com.atgc.cotton.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.atgc.cotton.App;
import com.atgc.cotton.Session;
import com.atgc.cotton.db.MessageDB;
import com.atgc.cotton.entity.MessageItem;
import com.atgc.cotton.entity.Motion;
import com.atgc.cotton.socket.SocketHandler;
import com.atgc.cotton.util.PrefKey;
import com.atgc.cotton.util.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Johnny on 2017-09-16.
 */
public class ChatService extends Service {
    public static ArrayList<receiveMessageCallback> callbacks = new ArrayList<>();
    private String token;
    private SocketHandler socketHandler;
    private MessageDB mMsgDB;// 保存消息的数据库
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    handler.removeMessages(0);
                    break;
                case 100:
                    login();
                    break;
            }
        }
    };
    private String userid;

    @Override
    public void onCreate() {
        super.onCreate();
        socketHandler = SocketHandler.getInstance();
        mMsgDB = App.getInstance().getMessageDB();// 发送数据库
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        userid = App.getInstance().getUid();
        login();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }


    public static void stopChatService(Context context) {
        Intent iService = new Intent(context, ChatService.class);
        context.stopService(iService);
    }

    public static void startChatService(Context context) {
        if (App.getInstance().isLogin()) {
            Intent iService = new Intent(context, ChatService.class);
            context.startService(iService);
        }
    }

    @Subscribe
    public void onMessageEvent(Motion event) {
        if (event != null) {
            int action = event.getAction();
            switch (action) {
                case Session.ACTION_SEND_IM_MESSAGE:
                    String msg = (String) event.getData();
                    if (msg != null && !msg.equals("")) {
                        sendMessage(msg);
                    }
                    break;
                case Session.ACTION_STOP_CHAT_SERVICE:
                    stopChatService(this);
                    break;
                case Session.ACTION_RECEIVER_IM_MESSAGE:
                    String message = (String) event.getData();
                    if (message != null && !message.equals("")) {
                        messageHandle(message);
                    }
                    break;
            }
        }
    }

    public void sendMessage(String msg) {
        socketHandler.sendMessage(msg);
    }

    private void login() {
        if (!TextUtils.isEmpty(token)) {
            socketHandler.sendMessage("token=" + token);
        }
    }

    private void messageHandle(String str) {
        try {
            JSONObject object = new JSONObject(str);
            String receiver_id = object.getString("target");
            String from_id = object.getString("from");
            int timestamp = object.getInt("timestamp");
            JSONObject obj1 = object.getJSONObject("msg");
            JSONObject obj2 = object.getJSONObject("ext");
            String msg = "";
            if (obj1 != null) {
                msg = obj1.getString("content");
            }
            String sender = "";
            String sender_avatar = "";
            if (obj2 != null) {
                sender = obj2.getString("sendername");
                sender_avatar = obj2.getString("senderavatar");
            }
            MessageItem item = new MessageItem(MessageItem.MESSAGE_TYPE_TXT,
                    sender, 0,
                    msg, 0, true, 0,
                    0, timestamp + "000",
                    sender, from_id,
                    sender_avatar);
            MessageItem data = mMsgDB.saveMsg(userid + "_" + from_id, item);// 保存数据库
            for (int i = 0; i < callbacks.size(); i++)
                ((receiveMessageCallback) callbacks.get(i)).onMessageReceive(data, from_id);

            String conversation = sender + ":" + msg;
            String noticeKey = PrefKey.NEWS_NOTICE_KEY + "/" + from_id;
            PreferenceUtils.putString(this, noticeKey, msg);
            String key = PrefKey.UN_READ_NEWS_KEY + "/" + from_id;
            int num = PreferenceUtils.getInt(this, key, 0);
            num++;
            PreferenceUtils.putInt(this, key, num);
            Session.getInstance().refreshConversationPager();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface receiveMessageCallback {
        void onMessageReceive(MessageItem item, String fromUid);

        void onNotify(String title, String content);

        void onNetChange(boolean isNetConnected);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
