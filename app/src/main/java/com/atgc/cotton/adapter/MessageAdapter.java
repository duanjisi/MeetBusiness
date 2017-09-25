package com.atgc.cotton.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.production.other.OtherProActivity;
import com.atgc.cotton.entity.MessageItem;
import com.atgc.cotton.util.ImageLoaderUtils;
import com.atgc.cotton.util.TimeUtil;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author pangzf
 * @desc发送消息的adapter
 * @blog:http://blog.csdn.net/pangzaifei/article/details/43023625
 * @github:https://github.com/pangzaifei/zfIMDemo
 * @qq:1660380990
 * @email:pzfpang451@163.com
 */
@SuppressLint("NewApi")
public class MessageAdapter extends BaseAdapter {
    private static final String TAG = MessageAdapter.class.getSimpleName();
    public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
    public static final int MESSAGE_TYPE_INVALID = -1;

    public static final int MESSAGE_TYPE_MINE_EMOTION = 0x00;
    public static final int MESSAGE_TYPE_MINE_IMAGE = 0x01;
    public static final int MESSAGE_TYPE_MINE_VIDEO = 0x02;
    public static final int MESSAGE_TYPE_MINE_TXT = 0;
    public static final int MESSAGE_TYPE_MINE_AUDIO = 0x08;

    public static final int MESSAGE_TYPE_OTHER_EMOTION = 0x03;
    public static final int MESSAGE_TYPE_OTHER_IMAGE = 0x04;
    public static final int MESSAGE_TYPE_OTHER_VIDEO = 0x05;
    public static final int MESSAGE_TYPE_OTHER_TXT = 1;
    public static final int MESSAGE_TYPE_OTHER_AUDIO = 0x09;

    public static final int MESSAGE_TYPE_TIME_TITLE = 0x07;
    public static final int MESSAGE_TYPE_HISTORY_DIVIDER = 0x08;
    private static final int VIEW_TYPE_COUNT = 2;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<MessageItem> mMsgList;
    private long mPreDate;
    private Resources resources;
    private ImageLoader imageLoader;
    private int widthScreen;
    private int widthMin;
    private Handler mHandler = new Handler();
    private int mImageHeight;
    //    private ChatDialog chatDialog;
    private String userid;

//    public MessageAdapter(Context context, List<MessageItem> msgList) {
//        this.mContext = context;
//        userid = App.getInstance().getUid();
//        widthScreen = UIUtils.getScreenWidth(context) / 2;
//        widthMin = UIUtils.getScreenWidth(context) / 3;
//        mImageHeight = (UIUtils.getScreenWidth(context) - UIUtils.dip2px(context, 60)) / 3;
//        resources = context.getResources();
//        mMsgList = msgList;
//        mInflater = LayoutInflater.from(context);
//        imageLoader = ImageLoaderUtils.createImageLoader(context);
////        mSpUtil = PushApplication.getInstance().getSpUtil();
////        mSoundUtil = SoundUtil.getInstance();
//    }

    public MessageAdapter(Context context, List<MessageItem> msgList) {
        this.mContext = context;
        userid = App.getInstance().getUid();
        widthScreen = UIUtils.getScreenWidth(context) / 2;
        widthMin = UIUtils.getScreenWidth(context) / 4;
        mImageHeight = (UIUtils.getScreenWidth(context) - UIUtils.dip2px(context, 60)) / 3;
        resources = context.getResources();
        mMsgList = msgList;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageLoaderUtils.createImageLoader(context);
//        mSpUtil = PushApplication.getInstance().getSpUtil();
//        mSoundUtil = SoundUtil.getInstance();
    }


    public void removeHeadMsg() {
        if (mMsgList.size() - 10 > 10) {
            for (int i = 0; i < 10; i++) {
                mMsgList.remove(i);
            }
            notifyDataSetChanged();
        }
    }

    public void removeItem(MessageItem item) {
        Iterator iterator = mMsgList.iterator();
        while (iterator.hasNext()) {
            MessageItem mode = (MessageItem) iterator.next();
            if (mode.getMsgId().equals(item.getMsgId())) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();
        if (mMsgList.size() != 0) {
            MessageItem lastItem = mMsgList.get(mMsgList.size() - 1);
            if (lastItem != null) {
//                refreshConversation(lastItem);
            }
        }
    }

//    private void refreshConversation(MessageItem messageItem) {
//        String noticeKey = PrefKey.NEWS_NOTICE_KEY + "/" + toUid;
//        String sender = "";
//        if (messageItem.getUserid().equals(userid)) {
//            sender = "我";
//        } else {
//            sender = messageItem.getNick();
//        }
//        String msg = "";
//        if (!isGroupChat) {
//            switch (messageItem.getMsgType()) {
//                case MessageItem.MESSAGE_TYPE_EMOTION:
//                    msg = "[动画表情]";
//                    break;
//                case MessageItem.MESSAGE_TYPE_IMG:
//                    msg = "[图片]";
//                    break;
//                case MessageItem.MESSAGE_TYPE_VIDEO:
//                    msg = "[视频]";
//                    break;
//                case MessageItem.MESSAGE_TYPE_AUDIO:
//                    msg = "[声音]";
//                    break;
//                case MessageItem.MESSAGE_TYPE_TXT:
//                    msg = sender + ":" + messageItem.getMessage();
//                    break;
//            }
//        } else {
//            switch (messageItem.getMsgType()) {
//                case MessageItem.MESSAGE_TYPE_EMOTION:
//                    msg = sender + "发了一条 [动画表情]";
//                    break;
//                case MessageItem.MESSAGE_TYPE_IMG:
//                    msg = sender + "发了一条 [图片]";
//                    break;
//                case MessageItem.MESSAGE_TYPE_VIDEO:
//                    msg = sender + "发了一条 [视频]";
//                    break;
//                case MessageItem.MESSAGE_TYPE_AUDIO:
//                    msg = sender + "发了一条 [声音]";
//                    break;
//                case MessageItem.MESSAGE_TYPE_TXT:
//                    msg = sender + ":" + messageItem.getMessage();
//                    break;
//            }
//        }
//        PreferenceUtils.putString(mContext, noticeKey, msg);
//    }


    public void setmMsgList(List<MessageItem> msgList) {
        mMsgList = msgList;
        notifyDataSetChanged();
    }

    public void upDateMsg(MessageItem msg) {
        mMsgList.add(msg);
        notifyDataSetChanged();
    }

    public void upDateMsgByList(List<MessageItem> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                mMsgList.add(list.get(i));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMsgList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMsgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        MessageHolderBase holder = null;
        if (null == convertView && null != mInflater) {
            holder = new MessageHolderBase();
            switch (type) {
                case MESSAGE_TYPE_MINE_TXT: {
                    convertView = mInflater.inflate(
                            R.layout.zf_chat_mine_text_message_item, parent, false);
                    holder = new TextMessageHolder();
                    convertView.setTag(holder);
                    fillTextMessageHolder((TextMessageHolder) holder,
                            convertView);
                    break;
                }
                case MESSAGE_TYPE_OTHER_TXT: {
                    convertView = mInflater.inflate(
                            R.layout.zf_chat_other_text_message_item, parent, false);
                    holder = new TextMessageHolder();
                    convertView.setTag(holder);
                    fillTextMessageHolder((TextMessageHolder) holder,
                            convertView);
                    break;
                }
                default:
                    break;
            }
        } else {
            holder = (MessageHolderBase) convertView.getTag();
        }
        final MessageItem mItem = mMsgList.get(position);
        if (mItem != null) {
            int msgType = mItem.getMsgType();
            if (msgType == MessageItem.MESSAGE_TYPE_TXT) {
                handleTextMessage((TextMessageHolder) holder, mItem, parent);
            }
        }
        return convertView;
    }

    private void handleTextMessage(final TextMessageHolder holder,
                                   final MessageItem mItem, final View parent) {
        handleBaseMessage(holder, mItem);
        holder.tvMsg.setText(mItem.getMessage());
        holder.tvMsg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence msg = ((TextView) v).getText();
                if (msg != null && !msg.equals("")) {
                    Intent intent = new Intent();
                    intent.putExtra("msg", msg);
                    intent.putExtra("item", mItem);
                    intent.putExtra("is_txt", true);
//                    chatDialog = new ChatDialog(mContext, intent);
//                    chatDialog.showDialog();
                }
                return false;
            }
        });
    }

    private void handleBaseMessage(MessageHolderBase holder,
                                   final MessageItem mItem) {
        holder.time.setText(TimeUtil.getChatTime(Long.parseLong(mItem.getTemp())));
        holder.time.setVisibility(View.VISIBLE);

//        if (mItem.isComMeg() && !toUid.equals("")) {
//            boolean showNick = PreferenceUtils.getBoolean(mContext, PrefKey.SHOW_NICK_NAME + toUid, true);
//            if (showNick) {
//                UIUtils.showView(holder.nick);
//            } else {
//                UIUtils.hindView(holder.nick);
//            }
//        }
        holder.nick.setText(mItem.getNick());
        holder.progressBar.setVisibility(View.GONE);
        holder.progressBar.setProgress(50);
        imageLoader.displayImage(mItem.getAvatar(), holder.head, ImageLoaderUtils.getDisplayImageOptions());
        holder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OtherProActivity.class);
                intent.putExtra("userid", mItem.getUserid());
                mContext.startActivity(intent);
            }
        });
    }

    private void fillBaseMessageholder(MessageHolderBase holder,
                                       View convertView) {
        holder.head = (CircleImageView) convertView.findViewById(R.id.icon);
        holder.nick = (TextView) convertView.findViewById(R.id.tv_nick_name);
        holder.time = (TextView) convertView.findViewById(R.id.datetime);
        // holder.msg = (GifTextView) convertView.findViewById(R.id.textView2);
        holder.rlMessage = (RelativeLayout) convertView
                .findViewById(R.id.relativeLayout1);
//         holder.ivphoto = (ImageView) convertView
//         .findViewById(R.id.iv_chart_item_photo);
        holder.progressBar = (ProgressBar) convertView
                .findViewById(R.id.progressBar1);
        // holder.voiceTime = (TextView) convertView
        // .findViewById(R.id.tv_voice_time);
        holder.flPickLayout = (FrameLayout) convertView
                .findViewById(R.id.message_layout);
    }

    private void fillTextMessageHolder(TextMessageHolder holder,
                                       View convertView) {
        fillBaseMessageholder(holder, convertView);
        holder.tvMsg = (TextView) convertView.findViewById(R.id.textView2);
    }


    private static class MessageHolderBase {
        CircleImageView head;
        TextView time;
        TextView nick;
        ImageView imageView;
        ProgressBar progressBar;
        RelativeLayout rlMessage;
        FrameLayout flPickLayout;
    }

    private static class TextMessageHolder extends MessageHolderBase {
        /**
         * 文字消息体
         */
        TextView tvMsg;
    }

    private static class EmotionMessageHolder extends MessageHolderBase {
        /**
         * 表情消息体
         */
//        GifView gifView;
//        GifImageView gifView;
        ImageView gifView;
        ImageView ivEmo;
    }

    private static class ImageMessageHolder extends MessageHolderBase {
        /**
         * 图片消息体
         */
        ImageView ivphoto;

    }

    private static class AudioMessageHolder extends MessageHolderBase {
        ImageView ivphoto;
        /**
         * 语音秒数
         */
        TextView voiceTime;
        //        GifView msg;
        ImageView msg;
    }

    private static class VideoMessageHolder extends MessageHolderBase {
        ImageView iv_cover;
        TextView duration;
        ImageView iv_player;
    }

    /**
     * 另外一种方法解析表情将[表情]换成fxxx
     *
     * @param message 传入的需要处理的String
     * @return
     */
//    private String convertNormalStringToSpannableString(String message) {
//        String hackTxt;
//        if (message.startsWith("[") && message.endsWith("]")) {
//            hackTxt = message + " ";
//        } else {
//            hackTxt = message;
//        }
//
//        Matcher localMatcher = EMOTION_URL.matcher(hackTxt);
//        while (localMatcher.find()) {
//            String str2 = localMatcher.group(0);
//            if (PushApplication.getInstance().getFaceMap().containsKey(str2)) {
//                String faceName = mContext.getResources().getString(
//                        PushApplication.getInstance().getFaceMap().get(str2));
//                CharSequence name = options(faceName);
//                message = message.replace(str2, name);
//            }
//        }
//        return message;
//    }

//    private String convertNormalStringToSpannableString(String message) {
//        String hackTxt;
//        if (message.startsWith("[") && message.endsWith("]")) {
//            hackTxt = message + " ";
//        } else {
//            hackTxt = message;
//        }
//
//        Matcher localMatcher = EMOTION_URL.matcher(hackTxt);
//        while (localMatcher.find()) {
//            String str2 = localMatcher.group(0);
//            if (App.getInstance().getFaceMap().containsKey(str2)) {
//                String faceName = mContext.getResources().getString(
//                        App.getInstance().getFaceMap().get(str2));
//                CharSequence name = options(faceName);
//                message = message.replace(str2, name);
//            }
//        }
//        return message;
//    }

    /**
     * 取名字f010
     *
     * @param faceName
     */
    private CharSequence options(String faceName) {
        int start = faceName.lastIndexOf("/");
        CharSequence c = faceName.subSequence(start + 1, faceName.length() - 4);
        return c;
    }

    static class ViewHolder {

        ImageView head;
        TextView time;
        //        GifTextView msg;
        ImageView imageView;
        ProgressBar progressBar;
        TextView voiceTime;
        ImageView ivphoto;
        RelativeLayout rlMessage;
        FrameLayout flPickLayout;
    }

    @Override
    public int getItemViewType(int position) {
        // logger.d("chat#getItemViewType -> position:%d", position);
        try {
            if (position >= mMsgList.size()) {
                return MESSAGE_TYPE_INVALID;
            }
            MessageItem item = mMsgList.get(position);
            if (item != null) {
                boolean comMeg = item.isComMeg();
                int type = item.getMsgType();
                if (comMeg) {
                    // 接受的消息
                    switch (type) {
//                        case MessageItem.MESSAGE_TYPE_EMOTION: {
//                            return MESSAGE_TYPE_OTHER_EMOTION;
//                        }
//
//                        case MessageItem.MESSAGE_TYPE_IMG: {
//                            return MESSAGE_TYPE_OTHER_IMAGE;
//                        }
//
//                        case MessageItem.MESSAGE_TYPE_VIDEO: {
//                            return MESSAGE_TYPE_OTHER_VIDEO;
//                        }
                        case MessageItem.MESSAGE_TYPE_TXT: {
                            return MESSAGE_TYPE_OTHER_TXT;
                        }
//                        case MessageItem.MESSAGE_TYPE_AUDIO: {
//                            return MESSAGE_TYPE_OTHER_AUDIO;
//                        }
                        default:
                            break;
                    }
                } else {
                    // 发送的消息
                    switch (type) {
//                        case MessageItem.MESSAGE_TYPE_EMOTION: {
//                            return MESSAGE_TYPE_MINE_EMOTION;
//                        }
//                        case MessageItem.MESSAGE_TYPE_IMG: {
//                            return MESSAGE_TYPE_MINE_IMAGE;
//
//                        }
//                        case MessageItem.MESSAGE_TYPE_VIDEO: {
//                            return MESSAGE_TYPE_MINE_VIDEO;
//                        }
                        case MessageItem.MESSAGE_TYPE_TXT: {
                            return MESSAGE_TYPE_MINE_TXT;
                        }
//                        case MessageItem.MESSAGE_TYPE_AUDIO: {
//                            return MESSAGE_TYPE_MINE_AUDIO;
//                        }
                        default:
                            break;
                    }
                }
            }
            return MESSAGE_TYPE_INVALID;
        } catch (Exception e) {
            Log.e("fff", e.getMessage());
            return MESSAGE_TYPE_INVALID;
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}