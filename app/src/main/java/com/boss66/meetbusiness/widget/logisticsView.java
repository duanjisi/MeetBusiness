package com.boss66.meetbusiness.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.entity.LogisticsModel;
import com.boss66.meetbusiness.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMARUnity on 2017/7/1.
 */
public class LogisticsView extends LinearLayout {

    private List<LogisticsModel> mDatas = new ArrayList<>();//下面给出了它的set跟get方法
    private Context mContext;
    private int sceenW;


    public LogisticsView(Context context) {
        this(context, null);
    }

    public LogisticsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogisticsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

    }

    private void init() {
        setOrientation(VERTICAL);
        mDatas = getmDatas();//获取数据
        int size = mDatas.size();
        sceenW = UIUtils.getScreenWidth(mContext) / 17;
        for (int i = 0; i < size; i++) {
            //获取布局，注意第二个参数一定是ViewGroup，否则margin padding之类的属性将不能使用
            View itemview = LayoutInflater.from(mContext).inflate(R.layout.item_logistics_view, this, false);
            TextView description = (TextView) itemview.findViewById(R.id.description_tv);
            ImageView iv_line = (ImageView) itemview.findViewById(R.id.iv_line);
            View v_end = itemview.findViewById(R.id.v_end);
            TextView tv_time = (TextView) itemview.findViewById(R.id.tv_time);
            ImageView icon = (ImageView) itemview.findViewById(R.id.stepicon_iv);
            description.setText(mDatas.get(i).getDescription());
            //根据不同状态设置不同图标
            switch (mDatas.get(i).getCurrentState()) {
                case LogisticsModel.STATE_COMPLETED:
                    icon.setImageResource(R.drawable.selected);
                    break;
                case LogisticsModel.STATE_DEFAULT:
                    LinearLayout.LayoutParams params = (LayoutParams) icon.getLayoutParams();
                    params.width = sceenW;
                    params.height = sceenW;
                    icon.setLayoutParams(params);
                    //结尾图标隐藏竖线
//                    iv_line.setVisibility(GONE);
                    //v_end.setVisibility(GONE);
                    tv_time.setTextColor(getResources().getColor(R.color.actionsheet_red));
                    description.setTextColor(getResources().getColor(R.color.actionsheet_red));
                    icon.setImageResource(R.drawable.shot_next_step);
                    break;
                case LogisticsModel.STATE_PROCESSING:
                    icon.setImageResource(R.drawable.selected);
                    break;
            }

            this.addView(itemview);

        }
        requestLayout();//重新绘制布局
        invalidate();//刷新当前界面
    }

    public List<LogisticsModel> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<LogisticsModel> mDatas) {
        this.mDatas = mDatas;
        init();
    }


}
