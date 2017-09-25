package com.atgc.cotton.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/10.
 */
public class ConversationColumn extends DatabaseColumn {
    public static final String TABLE_NAME = "conversation";
    public static final String CONVERSATION_ID = "conversation_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_AVATAR = "avatar";
    public static final String UNREAD_COUNT = "unread_count";
    public static final String MSG_TIME = "msg_time";
    public static final String MSG_TYPE = "msg_type";
    public static final String MSG_TXT = "msg_txt";
    public static final String USER_ID = "user_id";
    private static final Map<String, String> mColumnMap = new HashMap<String, String>();

    static {
        mColumnMap.put(_ID, "integer primary key autoincrement");
        mColumnMap.put(CONVERSATION_ID, "text");
        mColumnMap.put(USER_NAME, "text");
        mColumnMap.put(USER_AVATAR, "text");
        mColumnMap.put(UNREAD_COUNT, "text");
        mColumnMap.put(MSG_TIME, "text");
        mColumnMap.put(MSG_TYPE, "text");
        mColumnMap.put(MSG_TXT, "text");
        mColumnMap.put(USER_ID, "text");
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Map<String, String> getTableMap() {
        return mColumnMap;
    }
}
