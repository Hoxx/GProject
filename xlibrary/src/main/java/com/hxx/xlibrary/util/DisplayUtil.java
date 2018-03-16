package com.hxx.xlibrary.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.hxx.xlibrary.XLibrary;
import com.hxx.xlibrary.exception.XInitializeException;


/**
 * Created by Android on 2018/2/7.
 */

public class DisplayUtil {

    /**
     * px 转换 dp/dip
     *
     * @param px 像素值
     * @return 转换后的dp/dip值
     */
    public static float px2Dip(float px) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return px / scale + 0.5f;
    }

    /**
     * dp/dip 转换 px
     *
     * @param dip dp/dip值
     * @return 转换后的像素值
     */
    public static float dip2Px(float dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return dip * scale + 0.5f;
    }

    /**
     * px 转换 sp
     *
     * @param px 像素值
     * @return 转换后的sp值
     */
    public static float px2Sp(float px) {
        float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return px / scale + 0.5f;
    }

    /**
     * sp 转换 px
     *
     * @param sp sp值
     * @return 转换后的像素值
     */
    public static float sp2Px(float sp) {
        float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return sp / scale + 0.5f;
    }

    /**
     * 获取屏幕的尺寸信息
     *
     * @return int[2] int[0]:屏幕宽度，int[1]:屏幕高度
     */
    public int[] getScreenWidthAndHeight() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) return new int[]{0, 0};
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }


    private static Context getContext() {
        if (XLibrary.context == null) throw new XInitializeException();
        return XLibrary.context;
    }
}
