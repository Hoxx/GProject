package com.hxx.xlibrary.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class LocalDialog {

    private Builder builder;
    private PopupWindow window;

    private int[] showViewLocation;


    private LocalDialog(Builder builder) {
        this.builder = builder;
        init();
    }

    //初始化
    private void init() {
        window = new PopupWindow();
        window.setContentView(builder.contentView);
        window.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        measureViewSize(builder.contentView, window.getWidth(), window.getHeight());
    }

    //显示
    public void showViewTop() {
        showViewLocation = getViewWithScreenLocation(builder.showView);
        show(Gravity.TOP, 0, getShowLocationY());
    }

    public void show(int gravity, int x, int y) {
        if (builder != null && window != null && !window.isShowing()) {
            window.showAtLocation(builder.showView, gravity, x, y);
        }
    }

    //获取显示Y坐标
    private int getShowLocationY() {
        int contentHeight = builder.contentView.getMeasuredHeight();
        int showViewY = showViewLocation[1];
        return showViewY - contentHeight;
    }

    //获取ContentView的高宽
    private void measureViewSize(View view, int widthMeasureSpec, int heightMeasureSpec) {
        view.measure(makeDropDownMeasureSpec(widthMeasureSpec), makeDropDownMeasureSpec(heightMeasureSpec));
    }

    //测量
    private int makeDropDownMeasureSpec(int measureSpec) {
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), View.MeasureSpec.UNSPECIFIED);
        } else {
            return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), View.MeasureSpec.EXACTLY);
        }
    }

    //获取视图在屏幕中的坐标
    private int[] getViewWithScreenLocation(View view) {
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        return locations;
    }

    public static class Builder {

        private LayoutInflater inflater;
        private View contentView;
        private View showView;

        public Builder(Context context) {
            inflater = LayoutInflater.from(context);
        }


        public Builder setLayoutResId(int layoutResId) {
            contentView = inflater.inflate(layoutResId, null, false);
            return this;
        }

        public Builder setShowView(View showView) {
            this.showView = showView;
            return this;
        }

        public LocalDialog build() {
            return new LocalDialog(this);
        }
    }


}
