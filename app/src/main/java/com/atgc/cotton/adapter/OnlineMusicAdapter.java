package com.atgc.cotton.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.Session;
import com.atgc.cotton.SessionInfo;
import com.atgc.cotton.entity.OnlineVoiceEntity;
import com.atgc.cotton.util.SoundUtil2;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by GMARUnity on 2017/6/30.
 */
public class OnlineMusicAdapter extends BaseRecycleViewAdapter implements Observer {

    private Context mContext;
    private OnItemClickListener clickListener;
    private SoundUtil2 mSoundUtil = SoundUtil2.getInstance();
    private String cateId;

//    public OnlineMusicAdapter(Context context, OnItemClickListener listener) {
//        mContext = context;
//        this.clickListener = listener;
//        Session.getInstance().addObserver(this);
//    }

    public OnlineMusicAdapter(Context context, OnItemClickListener listener, String cateid) {
        mContext = context;
        this.clickListener = listener;
        this.cateId = cateid;
        Session.getInstance().addObserver(this);
    }

    @Override
    public void onBindItemHolder(final RecyclerView.ViewHolder holder, int position) {
        final ViewHolder holder1 = (ViewHolder) holder;
        if (datas != null && position < datas.size()) {
            final OnlineVoiceEntity entty = (OnlineVoiceEntity) datas.get(position);
            holder1.tv_name.setText("" + entty.getTitle());
            holder1.tv_time.setText("" + toTime(entty.getDuration()));
            if (entty.isChecked()) {
                holder1.iv_voice.setImageResource(R.drawable.voice_play03);
//                startAnimation(holder1.iv_voice, R.drawable.voice_other_animlist);
//                mSoundUtil.playRecorder(mContext, entty, new SoundUtil.CompletionListener() {
//                    @Override
//                    public void onCompletion() {
////                        stopAnimation(holder1.iv_voice, R.drawable.voice_other_animlist);
//                        holder1.iv_voice.setImageResource(R.drawable.voice_default);
//                    }
//                });

                mSoundUtil.playRecorder(mContext, entty.getMusicUrl(), new SoundUtil2.CompletionListener() {
                    @Override
                    public void onCompletion() {
                        holder1.iv_voice.setImageResource(R.drawable.voice_default);
                    }
                });
            } else {
                holder1.iv_voice.setImageResource(R.drawable.voice_default);
//                stopAnimation(holder1.iv_voice, R.drawable.voice_other_animlist);
//                mSoundUtil.stopRecord();
            }

            holder1.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.ItemClick(entty);
                    }
                }
            });
        }
    }

    private void startAnimation(ImageView imageView, int resId) {
        imageView.setImageResource(resId);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    private void stopAnimation(ImageView imageView, int resId) {
        imageView.setImageResource(resId);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.stop();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_local_voice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public void update(Observable observable, Object o) {
        SessionInfo sin = (SessionInfo) o;
        if (sin.getAction() == Session.ACTION_REFRESH_LIST) {
            String cateid = (String) sin.getData();
            if (!cateId.equals(cateid)) {
                List<OnlineVoiceEntity> list = getDatas();
                for (OnlineVoiceEntity entity : list) {
                    entity.setChecked(false);
                }
                notifyDataSetChanged();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_time;
        ImageView iv_voice;
        RelativeLayout item;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_voice = (ImageView) itemView.findViewById(R.id.iv_icon);
            item = (RelativeLayout) itemView.findViewById(R.id.item);
        }
    }


    public interface OnItemClickListener {
        void ItemClick(OnlineVoiceEntity entity);
    }

//    private String toTime(int var0) {
//        var0 /= 1000;
//        int var1 = var0 / 60;
//        boolean var2 = false;
//        if (var1 >= 60) {
//            int var4 = var1 / 60;
//            var1 %= 60;
//        }
//
//        int var3 = var0 % 60;
//        return String.format("%02d:%02d", new Object[]{Integer.valueOf(var1), Integer.valueOf(var3)});
//    }

    private String toTime(int var0) {
        int var1 = var0 / 60;
        boolean var2 = false;
        if (var1 >= 60) {
            int var4 = var1 / 60;
            var1 %= 60;
        }
        int var3 = var0 % 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(var1), Integer.valueOf(var3)});
    }
}
