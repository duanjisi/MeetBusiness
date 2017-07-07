package com.atgc.cotton.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atgc.cotton.entity.LocalVoiceEntity;
import com.atgc.cotton.R;

/**
 * Created by GMARUnity on 2017/6/30.
 */
public class LocalMusicAdapter extends BaseRecycleViewAdapter{

    private Context mContext;

    public LocalMusicAdapter(Context context) {
        mContext = context;
    }

    @Override
    public void onBindItemHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder holder1 = (ViewHolder) holder;
        if (datas != null && position < datas.size()) {
            LocalVoiceEntity entty = (LocalVoiceEntity) datas.get(position);
            holder1.tv_name.setText("" + entty.title);
            holder1.tv_time.setText("" + toTime(entty.duration));
        }
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    private String toTime(int var0) {
        var0 /= 1000;
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
