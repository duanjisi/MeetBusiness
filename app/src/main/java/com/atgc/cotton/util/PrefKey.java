package com.atgc.cotton.util;


import com.atgc.cotton.App;

/**
 * Created by Johnny on 2017/1/17.
 */
public class PrefKey {
    private static final String MAIN_KEY = "im.boss66.com." + App.getInstance().getUid() + ".";
    public static final String AVOID_DISTURB = MAIN_KEY + ".avoid.dsiturb.";
    public static final String SHOW_NICK_NAME = MAIN_KEY + ".show.nick.name.";
    public static final String SHOW_GROUP_MEMBER_NICK = MAIN_KEY + ".grp.member.nick.name.";
    public static final String UN_READ_NEWS_KEY = MAIN_KEY + ".unread.news";
    public static final String NEWS_NOTICE_KEY = MAIN_KEY + ".news.notice";
    public static final String EMOJI_DOWNLOAD_KEY = MAIN_KEY + ".emoji.download";
    public static final String CHAT_GROUP_INFORMS_FIRST = MAIN_KEY + ".chat.group.informs.setfirst";
    public static final String EMO_IS_LOADING = MAIN_KEY + ".emo.isloading";
    public static final String EMO_SYSTEM_INIT = MAIN_KEY + "init.system.emos";
    public static final String NOTIFY_NEWS_TAG = MAIN_KEY + "notify.news.tag";
    public static final String SORFT_HEIGHT_KEY = "com.atgc.cotton.softheight";
}
