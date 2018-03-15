package com.hxx.gprojrct.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Android on 2018/3/15.
 */

public class PointProgress extends View {

    //屏幕宽度
    private int screenWidth;
    //View默认最小高度
    private int defaultMinSize = 100;
    //画笔
    private Paint paint;
    //progress高度
    private int progressHeight = 20;
    //progress总进度
    private RectF progressAllRectF;
    //progress进度
    private Progress progressRectF;
    //进度指示视图
    private PointView pointView;
    //进度指示视图宽度
    private float pointWidth = 100;
    //进度指示视图高度
    private float pointHeight = 50;
    //进度最大值
    private float max;
    //文字
    private String text;
    //动画
    private ValueAnimator valueAnimator;
    //文字格式
    private String textFormat = "%.1f秒";
    //颜色
    private int progressColor = Color.GREEN;
    private int progressBgColor = Color.GRAY;
    private int progressPointColor = Color.GREEN;
    private int progressTextColor = Color.WHITE;

    public PointProgress(Context context) {
        super(context);
        init();
    }

    public PointProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        initProgress();
        initPointView();
    }

    private void initProgress() {
        progressAllRectF = new RectF(0, 0, screenWidth, progressHeight);
        progressRectF = new Progress(progressAllRectF);
    }

    private void initPointView() {
        pointView = new PointView(progressHeight, pointWidth, pointHeight);
    }

    /**
     * 开始计时
     *
     * @param maxMillis
     * @param format
     */
    public void start(long maxMillis, String format) {
        if (maxMillis < 1000) return;
        if (!TextUtils.isEmpty(text))
            this.textFormat = format;
        setMax(maxMillis);
        valueAnimator = ValueAnimator.ofFloat(0, maxMillis).setDuration(maxMillis);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float tValue = value / 1000;
                setProgress(value, String.format(textFormat, tValue));
            }
        });
        valueAnimator.start();
    }

    /**
     * 设置颜色
     *
     * @param progressColor      progress颜色
     * @param progressBgColor    progress背景颜色
     * @param progressPointColor progress指示颜色
     * @param progressTextColor  progress文字颜色
     */
    public void setProgressColor(int progressColor, int progressBgColor, int progressPointColor, int progressTextColor) {
        if (progressColor != 0) {
            this.progressColor = progressColor;
        }
        if (progressBgColor != 0) {
            this.progressBgColor = progressBgColor;
        }
        if (progressPointColor != 0) {
            this.progressPointColor = progressPointColor;
        }
        if (progressTextColor != 0) {
            this.progressTextColor = progressTextColor;
        }
    }

    /**
     * 设置进度最大值
     *
     * @param max
     */
    private void setMax(float max) {
        this.max = max;
        progressRectF.setMax(max);
    }

    /**
     * 设置进度值，与显示的文字
     *
     * @param progress
     * @param text
     */
    private void setProgress(float progress, String text) {
        if (progress > max) return;
        progressRectF.setProgress(progress);
        if (progressRectF.right + pointWidth >= screenWidth)
            pointView.reverse();
        pointView.setRowX(progressRectF.right);
        this.text = text;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getWidth(widthMeasureSpec), getHeight(heightMeasureSpec));
    }

    /**
     * 获取视图宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int getWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {//match_content模式
            return size;
        } else if (mode == MeasureSpec.AT_MOST) {//wrap_content模式
            return screenWidth;
        }
        return 0;
    }

    /**
     * 获取视图高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int getHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {//match_content模式
            return size;
        } else if (mode == MeasureSpec.AT_MOST) {//wrap_content模式
            return defaultMinSize;
        }
        return 0;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(progressBgColor);
        canvas.drawRect(progressAllRectF, paint);
        if (isCanDraw()) {
            drawProgress(canvas);
            drawPoint(canvas);
            drawText(canvas);
        }

    }

    /**
     * 是否可以绘制
     *
     * @return
     */
    private boolean isCanDraw() {
        return progressRectF != null && progressRectF.isEnable() && pointView != null && !TextUtils.isEmpty(text);
    }

    /**
     * 绘制进度
     *
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        paint.setColor(progressColor);
        canvas.drawRect(progressRectF, paint);
    }

    /**
     * 绘制进度指示
     *
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {
        paint.setColor(progressPointColor);
        canvas.drawRoundRect(pointView.getRectF(), 10, 10, paint);
        canvas.drawPath(pointView.getRowPath(), paint);

    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        paint.setColor(progressTextColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(30);
        float textHeight = paint.getFontMetrics().descent - paint.getFontMetrics().ascent;
        canvas.drawText(text, pointView.getRectF().centerX(), pointView.getRectF().centerY() + textHeight / 3, paint);
    }

    /**
     * 进度指示
     */
    class PointView {
        private RectF rectF;
        private Path rowPath;
        private boolean isReverse;

        PointView(float top, float width, float height) {
            rectF = new RectF(0, top + 10, width, top + 10 + height);
            rowPath = new Path();
        }

        /**
         * 重新设置箭头路径
         *
         * @param rowX
         */
        void resetRowPath(float rowX) {
            rowPath.reset();
            rowPath.moveTo(rowX, rectF.top - 10);
            rowPath.lineTo(rowX, rectF.centerY());
            rowPath.lineTo(rectF.centerX(), rectF.centerY());
            rowPath.close();
        }

        /**
         * 设置箭头的X轴坐标，计算文字边框的位置
         *
         * @param rowX
         */
        void setRowX(float rowX) {
            if (isReverse) {
                rectF.offsetTo(rowX - rectF.width(), rectF.top);
            } else {
                rectF.offsetTo(rowX, rectF.top);
            }
            resetRowPath(rowX);
        }

        /**
         * 翻转
         */
        public void reverse() {
            isReverse = true;
        }


        /**
         * 获取显示文字的圆角边框
         *
         * @return
         */
        RectF getRectF() {
            return rectF;
        }

        /**
         * 获取箭头路径
         *
         * @return
         */
        Path getRowPath() {
            return rowPath;
        }
    }

    //进度条
    class Progress extends RectF {

        private float maxProgress;
        private float max;

        Progress(RectF r) {
            super(r);
            maxProgress = r.right;
        }

        /**
         * 设置最大值，用于计算比例
         *
         * @param max
         */
        void setMax(float max) {
            this.max = max;
        }

        /**
         * 根据Progress计算宽度
         *
         * @param progress
         */
        void setProgress(float progress) {
            if (max != 0 && progress <= max) {
                float scale = progress / max;
                right = maxProgress * scale;
            } else {
                right = progress;
            }
        }

        /**
         * 是否可用
         *
         * @return
         */
        boolean isEnable() {
            return max > 0;
        }
    }

}
