package com.atgc.cotton;

import java.util.Observable;

/**
 * Created by Johnny on 2017-09-09.
 */
public class Session extends Observable {
    private static Session session = null;
    public static final int ACTION_REFRESH_LIST = 1011;
    public static final int ACTION_SEND_IM_MESSAGE = 1015;//发送信息
    public static final int ACTION_STOP_CHAT_SERVICE = 1016;//发送信息
    public static final int ACTION_RECEIVER_IM_MESSAGE = 1017;//接受IM信息
    public static final int ACTION_REFRSH_CONVERSATION_PAGE = 1018;

    public Session() {

    }

    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    public void refreshList(String cateid) {
        SessionInfo sin = new SessionInfo();
        sin.setAction(ACTION_REFRESH_LIST);
        sin.setData(cateid);
        this.setChanged();
        this.notifyObservers(sin);
    }

    public void refreshConversationPager() {
        SessionInfo sin = new SessionInfo();
        sin.setAction(ACTION_REFRSH_CONVERSATION_PAGE);
        this.setChanged();
        this.notifyObservers(sin);
    }
}
