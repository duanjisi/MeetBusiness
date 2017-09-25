package com.atgc.cotton.socket;

import android.text.TextUtils;
import android.util.Log;

import com.atgc.cotton.App;
import com.atgc.cotton.Session;
import com.atgc.cotton.entity.Motion;
import com.atgc.cotton.util.NetworkUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import de.greenrobot.event.EventBus;

/**
 * Created by Johnny on 2017-09-20.
 */
public class WebSocketExampleClient extends WebSocketClient {


    public WebSocketExampleClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d("websocket", "open");
    }

    @Override
    public void onMessage(String message) {
        Log.d("websocket", "onMessage:" + message);
        if (!TextUtils.isEmpty(message) && !message.equals("ok")) {
            Motion motion = Motion.getInstance();
            motion.setAction(Session.ACTION_RECEIVER_IM_MESSAGE);
            motion.setData(message);
            EventBus.getDefault().post(motion);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d("websocket", "onClose");
        if (NetworkUtil.networkAvailable(App.getInstance())) {//网络可用
            SocketHandler.getInstance().sendMessage("token=" + App.getInstance().getToken());//重新登录
        }
    }

    @Override
    public void onError(Exception ex) {
        Log.d("websocket", "onError");
    }
}
