package com.atgc.cotton.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atgc.cotton.R;
import com.atgc.cotton.widget.base.BasePopup;


/**
 * 上传图片PopupWindow
 */
public class MorePopup extends BasePopup implements View.OnClickListener {

    private OnItemdListener mOnItemSelectedListener;
    private Context context;
    private boolean isTopShow = true;

    public MorePopup(Context context) {
        super(context);
        this.context = context;
        setAnimationStyle(R.style.popwin_anim_style);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View popView = LayoutInflater.from(context).inflate(R.layout.popup_more, null);
        initViews(popView);
        setContentView(popView);
    }

    public MorePopup(Context context, boolean show) {
        super(context);
        this.context = context;
        this.isTopShow = show;
        setAnimationStyle(R.style.popwin_anim_style);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View popView = LayoutInflater.from(context).inflate(R.layout.popup_more, null);
        initViews(popView);
        setContentView(popView);
    }

    private void initViews(View view) {
        if (!isTopShow) {
            view.findViewById(R.id.rl_id).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.rl_id).setOnClickListener(this);
        }
        view.findViewById(R.id.tv_inform).setOnClickListener(this);
        view.findViewById(R.id.tv_add_black_name).setOnClickListener(this);
        view.findViewById(R.id.tv_cancle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_id:
                dismiss();
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemClick(0);
                }
                break;
            case R.id.tv_inform:
                dismiss();
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemClick(1);
                }
                break;
            case R.id.tv_add_black_name:
                dismiss();
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemClick(2);
                }
                break;
            case R.id.tv_cancle:
                dismiss();
                break;
        }
    }


    public void setOnItemSelectedListener(OnItemdListener onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
    }

    public interface OnItemdListener {
        void onItemClick(int type);
    }


    /**
     * 显示PopupWindow
     *
     * @param view PopupWindow依附的View
     */
    public void show(View view) {
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.45f);
    }
}
