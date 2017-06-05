package com.boss66.meetbusiness.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * @ClassName: UIUtil
 * @Description: 布局相关方法操作类
 * @author: zyu
 * @date: 2014-12-6 下午2:44:11
 */
public final class UIUtils {
    private static int ScreenHeight;
    private static int ScreenWidth;

    /**
     * 转换dip为px
     *
     * @param context
     * @param dip
     * @return
     */
    public static int dip2px(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 转换px为dip
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dip(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        ScreenHeight = dm.heightPixels;
        return ScreenHeight;
    }

    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        ScreenWidth = dm.widthPixels;
        return ScreenWidth;
    }

    public static int setCursorIm(Context context, ImageView im, int num) {
        int width = getScreenWidth(context) / num;
        ViewGroup.LayoutParams lp = im.getLayoutParams();
        lp.width = width;
        return width;
    }


    public static int setCursorIm(Context context, View parent, ImageView im, int num) {
        Log.i("info", "====================width:" + parent.getWidth());
        int width = parent.getWidth() / num;
        ViewGroup.LayoutParams lp = im.getLayoutParams();
        lp.width = width;
        return width;
    }

}
