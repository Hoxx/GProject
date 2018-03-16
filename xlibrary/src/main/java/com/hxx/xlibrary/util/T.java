package com.hxx.xlibrary.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Somebody on 2016/8/5.
 */
public class T {

    private static boolean isShow = true;
    private static boolean isSingleView = true;

    private static final int DEFAULT_GRAVITY = -1;
    private static final int DEFAULT_X_OFFSET = -1;
    private static final int DEFAULT_Y_OFFSET = -1;

    private static Toast singleToast;

    public static void closeShow() {
        T.isShow = false;
    }

    /**
     * 短时间显示
     *
     * @param context
     * @param text
     */
    public static void show(Context context, String text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示
     *
     * @param context
     * @param text
     */
    public static void show(Context context, int text) {
        show(context, context.getString(text), Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示
     *
     * @param context
     * @param text
     */
    public static void showLong(Context context, String text) {
        show(context, text, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示
     *
     * @param context
     * @param text
     */
    public static void showLong(Context context, int text) {
        show(context, context.getString(text), Toast.LENGTH_LONG);
    }


    /**
     * 屏幕中间长时间显示
     *
     * @param context
     * @param text
     */
    public static void showCenter(Context context, String text) {
        show(context, text, Toast.LENGTH_SHORT, Gravity.CENTER, DEFAULT_X_OFFSET, DEFAULT_Y_OFFSET);
    }

    /**
     * 屏幕中间长时间显示
     *
     * @param context
     * @param text
     */
    public static void showCenter(Context context, int text) {
        show(context, context.getString(text), Toast.LENGTH_SHORT, Gravity.CENTER, DEFAULT_X_OFFSET, DEFAULT_Y_OFFSET);
    }

    /**
     * 屏幕顶部长时间显示
     *
     * @param context
     * @param text
     */
    public static void showTop(Context context, String text) {
        show(context, text, Toast.LENGTH_SHORT, Gravity.TOP, DEFAULT_X_OFFSET, DEFAULT_Y_OFFSET);
    }

    /**
     * 屏幕顶部长时间显示
     *
     * @param context
     * @param text
     */
    public static void showTop(Context context, int text) {
        show(context, context.getString(text), Toast.LENGTH_SHORT, Gravity.TOP, DEFAULT_X_OFFSET, DEFAULT_Y_OFFSET);
    }

    /**
     * 自定义时间显示
     *
     * @param context
     * @param text
     * @param duration
     */
    private static void show(Context context, String text, int duration) {
        show(context, text, duration, DEFAULT_GRAVITY, DEFAULT_X_OFFSET, DEFAULT_Y_OFFSET);
    }

    /**
     * 完全自定义显示
     *
     * @param context
     * @param text
     * @param duration
     * @param gravity
     * @param XOffset
     * @param YOffset
     */
    private static void show(Context context, String text, int duration, int gravity, int XOffset, int YOffset) {
        if (isSingleView) {
            showSingleToast(context, text, duration, gravity, XOffset, YOffset);
        } else {
            showToast(context, text, duration, gravity, XOffset, YOffset);
        }
    }

    /**
     * 单个Toast显示
     *
     * @param context
     * @param text
     * @param duration
     * @param gravity
     * @param XOffset
     * @param YOffset
     */
    private static void showSingleToast(Context context, String text, int duration, int gravity, int XOffset, int YOffset) {
        if (singleToast == null) {
            singleToast = Toast.makeText(context, text, duration);
        } else {
            singleToast.setDuration(duration);
            singleToast.setText(text);
        }
        singleToast.setGravity(
                gravity >= 0 ? gravity : singleToast.getGravity(),
                XOffset >= 0 ? XOffset : singleToast.getXOffset(),
                YOffset >= 0 ? XOffset : singleToast.getYOffset());
        if (isShow) {
            singleToast.show();
        }
    }

    /**
     * 每次一个Toast显示
     *
     * @param context
     * @param text
     * @param duration
     * @param gravity
     * @param XOffset
     * @param YOffset
     */
    private static void showToast(Context context, String text, int duration, int gravity, int XOffset, int YOffset) {
        Toast singleToast = Toast.makeText(context, text, duration);
        singleToast.setGravity(
                gravity >= 0 ? gravity : singleToast.getGravity(),
                XOffset >= 0 ? XOffset : singleToast.getXOffset(),
                YOffset >= 0 ? XOffset : singleToast.getYOffset());
        if (isShow) {
            singleToast.show();
        }
    }

}

