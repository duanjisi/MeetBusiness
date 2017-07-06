package com.atgc.cotton.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * Created by Johnny on 2017/6/1.
 */
public class MyRecyclerView extends LRecyclerView {
    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
