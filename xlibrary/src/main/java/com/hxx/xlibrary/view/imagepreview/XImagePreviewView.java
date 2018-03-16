package com.hxx.xlibrary.view.imagepreview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.hxx.xlibrary.image.XImageClient;
import com.hxx.xlibrary.util.L;


/**
 * Created by Android on 2018/1/5.
 */

public class XImagePreviewView extends AppCompatImageView {
    //操作模式
    private final int ACTION_MOVE = 0x11;
    private final int ACTION_ZOOM = 0x12;
    //最大的缩放值
    private float MAX_SCALE_VALUE = 5.0f;
    private float MIN_SCALE_VALUE = 0.5f;
    //触摸点
    private PointF onePoint;//第一个触摸点
    private PointF twoPoint;//第二个触摸点
    //执行模式
    private int ACTION_MODE;
    //触摸点距离
    private float downPointDistance;
    private PointF zoomCenterPoint;//缩放中心点
    //缩放
    private Matrix initMatrix;
    private Matrix actionMatrix;
    //计算图片大小
    private boolean isInitImage;
    private float imageWidth;
    private float imageHeight;
    //手势
    private GestureDetector gestureDetector;
    //自动缩放
    private boolean isAutoZoom;

    public XImagePreviewView(Context context) {
        super(context);
        init();
    }

    public XImagePreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setScaleType(ScaleType.CENTER);
        onePoint = new PointF();//第一个触摸点
        twoPoint = new PointF();//第二个触摸点
        actionMatrix = new Matrix();
        initMatrix = new Matrix();
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                float[] values = getMatrixValue(getImageMatrix());
                if (getMatrixScale(values) > 1) {
                    autoZoomOutAnim(600f, 100f, e.getX(), e.getY(), 300);
                } else {
                    autoZoomInAnim(300f, 600f, e.getX(), e.getY());
                }
                return true;
            }
        });
    }

    public void setImageData(String path) {
        XImageClient.getInstance().load(this, path);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initImageParams();
        setScaleType(ScaleType.MATRIX);
        gestureDetector.onTouchEvent(event);
        if (isAutoZoom) return super.onTouchEvent(event);
        int pointerCount = event.getPointerCount();//触摸点个数
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN://第一个手指触摸屏幕
                ACTION_MODE = ACTION_MOVE;
                onePoint.set(event.getX(0), event.getY(0));
                break;
            case MotionEvent.ACTION_POINTER_DOWN://当屏幕上已经有一个点被按住，此时再按下其他点时触发
                ACTION_MODE = ACTION_ZOOM;
                if (pointerCount == 2) {//记录第二个触摸点
                    zoomCenterPoint = null;
                    twoPoint.set(event.getX(1), event.getY(1));
                    downPointDistance = distance(event);
                    MIN_SCALE_VALUE = 0.5f;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (ACTION_MODE == ACTION_MOVE && pointerCount == 1) {
                    actionMoveImage(event);
                }
                if (ACTION_MODE == ACTION_ZOOM && pointerCount >= 2) {
                    actionZoomImage(event);
                }
                break;
            case MotionEvent.ACTION_UP://当最后一个手指离开屏幕
            case MotionEvent.ACTION_CANCEL:
                actionEmpty();
                break;
            case MotionEvent.ACTION_POINTER_UP://当屏幕上有多个点被按住，松开其中一个点时触发（即非最后一个点被放开时）
                break;
        }
        return true;
    }

    //执行图片没有触摸时的操作
    private void actionEmpty() {
        if (getMatrixScale(getMatrixValue(getImageMatrix())) < 1) {
            setImageMatrix(initMatrix);//设置初始的Matrix
        }
    }

    //获取图片的尺寸（不是当前控件的大小）
    private void initImageParams() {
        if (isInitImage && imageWidth != 0 && imageHeight != 0) return;
        isInitImage = true;
        initMatrix.set(getImageMatrix());//获取初始的图片Matrix
        Drawable drawable = getDrawable();
        if (drawable != null) {
            //获得ImageView中Image的真实宽高，
            imageWidth = drawable.getBounds().width();
            imageHeight = drawable.getBounds().height();
        }
    }

    //执行图片移动
    private void actionMoveImage(MotionEvent event) {
        if (!checkEnableMove()) return;
        actionMatrix.set(getImageMatrix());//初始化Matrix
        float transX = event.getX() - onePoint.x;
        float transY = event.getY() - onePoint.y;
        onePoint.set(event.getX(0), event.getY(0));//重新定义起始点
        float[] values = getMatrixValue(actionMatrix);
        transX = checkXMove(values, transX);//检查X方向移动距离
        transY = checkYMove(values, transY);//检查Y方向移动距离
        actionMatrix.postTranslate(transX, transY);
        setImageMatrix(actionMatrix);
    }

    //是否可以进行拖动处理
    private boolean checkEnableMove() {
        return imageWidth > 0 && imageHeight > 0;//图片的尺寸不为0
    }

    //校验X轴移动边界
    private float checkXMove(float[] values, float distance) {
        if (getZoomWidth(values) <= getWidth())
            return 0;
        if (getMatrixTransX(values) + distance > 0)//左边界（当前的移动的距离加上将要移动的距离，如果会超过左边界，则本次移动的值变为，当前图片距离左边界的值）
            return -getMatrixTransX(values);
        if (getMatrixTransX(values) + distance < -(getZoomWidth(values) - getWidth()))//右边界（同左边界）
            return -(getZoomWidth(values) - getWidth()) - getMatrixTransX(values);
        return distance;
    }

    //校验Y轴移动边界
    private float checkYMove(float[] values, float distance) {
        if (getZoomHeight(values) <= getHeight())
            return 0;
        if (getMatrixTransY(values) + distance > 0)//上边界（同左边界）
            return -getMatrixTransY(values);
        if (getMatrixTransY(values) + distance < -(getZoomHeight(values) - getHeight()))//下边界（同左边界）
            return -(getZoomHeight(values) - getHeight()) - getMatrixTransY(values);
        return distance;
    }

    //执行图片缩放
    private void actionZoomImage(MotionEvent event) {
        float movePointDistance = distance(event);
        float scale = movePointDistance / downPointDistance;//计算缩放比例
        downPointDistance = movePointDistance;//重置距离
        actionMatrix.set(getImageMatrix());//初始化Matrix
        float[] values = getMatrixValue(actionMatrix);
        scale = checkZoom(values, scale);//检查缩放的系数
        zoomCenterPoint = getZoomCenterPoint(values);//获取缩放的中心点
        actionMatrix.postScale(scale, scale, zoomCenterPoint.x, zoomCenterPoint.y);
        setImageMatrix(actionMatrix);
    }

    //检验scale，使图像缩放后不会超出最大倍数
    private float checkZoom(float[] values, float scale) {
        if (scale * getMatrixScale(values) > MAX_SCALE_VALUE)//最大缩放值（如果当前的缩放单位*将要缩放的单位，大于最大的缩放值，则此次的缩放值为最大缩放值除当前缩放值）
            scale = MAX_SCALE_VALUE / getMatrixScale(values);
        if (scale * getMatrixScale(values) < MIN_SCALE_VALUE)//最小缩放值（同最大缩放值）
            scale = MIN_SCALE_VALUE / getMatrixScale(values);
        return scale;
    }

    // 计算两个触摸点之间的距离
    private float distance(MotionEvent event) {
        if (event.getPointerCount() < 2) return 0;
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    //获取缩放区域的中心点(根据图片边界动态改变缩放的中心点：)
    private PointF getZoomCenterPoint(float[] values) {
        //当前双指的中心点
        float x = (onePoint.x + twoPoint.x) / 2;
        float y = (onePoint.y + twoPoint.y) / 2;
        return checkZoomCenterPoint(x, y, values);
    }

    //校验缩放中心点的坐标
    private PointF checkZoomCenterPoint(float x, float y, float[] values) {
        //X轴到达左边界，切换缩放中心点X坐标为0，即左边缘
        if (getMatrixTransX(values) >= 0) {
            if (getZoomWidth(values) < getWidth()) {
                x = getWidth() / 2;
            } else {
                x = 0;
            }
        }
        //X轴到达右边界，切换缩放中心点X坐标为视图宽度，即右边缘
        if (getMatrixTransX(values) <= -(getZoomWidth(values) - getWidth())) {
            if (getZoomWidth(values) < getWidth()) {
                x = getWidth() / 2;
            } else {
                x = imageWidth;
            }
        }
        //Y轴到达上边界，切换缩放中心点Y坐标为0，即上边缘
        if (getMatrixTransY(values) >= 0) {
            if (getZoomHeight(values) < getHeight()) {
                y = getHeight() / 2;
            } else {
                y = 0;
            }
        }
        //Y轴到达下边界，切换缩放中心点Y坐标为，即下边缘
        if (getMatrixTransY(values) <= -(getZoomHeight(values) - getHeight())) {
            if (getZoomHeight(values) < getHeight()) {
                y = getHeight() / 2;
            } else {
                y = imageHeight;
            }
        }
        if (getMatrixScale(values) <= 1) {
            x = getWidth() / 2;
            y = getHeight() / 2;
        }
        return new PointF(x, y);
    }

    //获取缩放之后的宽度
    private float getZoomWidth(float[] values) {
        return getMatrixScaleX(values) * imageWidth;
    }

    //获取缩放之后的高度
    private float getZoomHeight(float[] values) {
        return getMatrixScaleY(values) * imageHeight;
    }

    //获取当前图片Matrix的scaleX(X,Y的缩放值相同)
    private float getMatrixScale(float[] values) {
        return Math.max(getMatrixScaleX(values), getMatrixScaleY(values));
    }

    //获取当前图片Matrix的scaleX(X,Y的缩放值相同)
    private float getMatrixScaleX(float[] values) {
        return getMatrixValue(values, Matrix.MSCALE_X);
    }

    //获取当前图片Matrix的scaleY(X,Y的缩放值相同)
    private float getMatrixScaleY(float[] values) {
        return getMatrixValue(values, Matrix.MSCALE_Y);
    }

    //获取当前图片Matrix的transX
    private float getMatrixTransX(float[] values) {
        return getMatrixValue(values, Matrix.MTRANS_X);
    }

    //获取当前 图片Matrix的transY
    private float getMatrixTransY(float[] values) {
        return getMatrixValue(values, Matrix.MTRANS_Y);
    }

    //获取Matrix的值
    private float getMatrixValue(float[] values, int valueType) {
        return values[valueType];
    }

    //获取Matrix的值
    private float[] getMatrixValue(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        return values;
    }

    private void autoZoomIn(float zoomCenterPointX, float zoomCenterPointY) {
        actionMatrix.set(getImageMatrix());//初始化Matrix
        float[] values = getMatrixValue(actionMatrix);
        float scaleX = getMatrixScaleX(values);
        float scaleY = getMatrixScaleY(values);
//        zoomCenterPoint = checkZoomCenterPoint(zoomCenterPointX, zoomCenterPointY, values);//获取缩放的中心点
//        actionMatrix.setScale(2f, 2f, zoomCenterPoint.x, zoomCenterPoint.y);
//        actionMatrix.setScale(2f, 2f, imageWidth / 2, imageHeight / 2);
//        actionMatrix.postScale(2f / scaleX, 2f / scaleY, zoomCenterPointX, zoomCenterPointY);
        actionMatrix.postScale(2f / scaleX, 2f / scaleY, getWidth() / 2, getHeight() / 2);
//        actionMatrix.postScale(2f / scaleX, 2f / scaleY, zoomCenterPoint.x, zoomCenterPoint.y);
        setImageMatrix(actionMatrix);
    }

    private void autoZoomOut() {
        setImageMatrix(initMatrix);
    }

    //自动放大动画
    private void autoZoomInAnim(float startValue, final float endValue, final float zoomCenterPointX, final float zoomCenterPointY) {
        downPointDistance = startValue;
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(startValue, endValue).setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float scale = value / downPointDistance;//计算缩放比例
                downPointDistance = value;//重置距离
                actionMatrix.set(getImageMatrix());//初始化Matrix
                float[] values = getMatrixValue(actionMatrix);
                scale = checkZoom(values, scale);//检查缩放的系数
                zoomCenterPoint = checkZoomCenterPoint(zoomCenterPointX, zoomCenterPointY, values);//获取缩放的中心点
                actionMatrix.postScale(scale, scale, zoomCenterPoint.x, zoomCenterPoint.y);
                setImageMatrix(actionMatrix);
                isAutoZoom = (value != endValue);
            }
        });
        valueAnimator.start();
    }

    //自动缩小动画
    private void autoZoomOutAnim(float startValue, final float endValue, final float zoomCenterPointX, final float zoomCenterPointY, long duration) {
        downPointDistance = startValue;
        MIN_SCALE_VALUE = 1f;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startValue, endValue).setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                isAutoZoom = true;
                float value = (float) animation.getAnimatedValue();
                float scale = value / downPointDistance;//计算缩放比例
                downPointDistance = value;//重置距离
                //开始缩放
                actionMatrix.set(getImageMatrix());//初始化Matrix
                float[] values = getMatrixValue(actionMatrix);
                scale = checkZoom(values, scale);//检查缩放的系数
                zoomCenterPoint = checkZoomCenterPoint(zoomCenterPointX, zoomCenterPointY, values);//获取缩放的中心点
                actionMatrix.postScale(scale, scale, zoomCenterPoint.x, zoomCenterPoint.y);
                setImageMatrix(actionMatrix);
                isAutoZoom = (value != endValue);
                if (value == endValue) {//避免自动缩放时，缩放值误差,设置初始的Matrix
                    float endScale = getMatrixScale(values);
                    L.e("endScale:" + endScale);
                    if (endScale != 1) {
                        actionMatrix.postScale(1f / endScale, 1f / endScale, zoomCenterPoint.x, zoomCenterPoint.y);
                        setImageMatrix(actionMatrix);
                        L.e("getMatrixScale(getMatrixValue(getImageMatrix())):" + getMatrixScale(getMatrixValue(getImageMatrix())));
                    }
                }
            }
        });
        valueAnimator.start();
    }

}
