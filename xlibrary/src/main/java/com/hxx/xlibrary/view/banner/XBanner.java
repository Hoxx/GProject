package com.hxx.xlibrary.view.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;


import com.hxx.xlibrary.image.XImageClient;

import java.util.List;

/**
 * Created by Android on 2017/12/19 .
 */

public class XBanner extends ViewPager implements XBannerLoadDataCallback<ImageView, String> {

    //数据
    private List<String> data;
    //当前位置
    private int currentPosition;
    //自动切换
    private boolean autoEnable;
    private boolean isStop;
    private long switchDelayed = 2000;
    private int MessageWhat = 0x1;
    private Handler switchHandler = new Handler(Looper.getMainLooper(),
            new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (!isStop && autoEnable) {
                        setCurrentItem(currentPosition + 1);
                        sendChangeMessage();
                    }
                    return false;
                }
            });
    //点击监听
    private XBannerItemClickListener xBannerItemClickListener;

    public XBanner(Context context) {
        super(context);
    }

    public XBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onPause() {
        isStop = true;
    }

    public void onResume() {
        isStop = false;
    }

    public void setData(List<String> data) {
        this.data = data;
        init();
    }

    public void setxBannerItemClickListener(XBannerItemClickListener xBannerItemClickListener) {
        this.xBannerItemClickListener = xBannerItemClickListener;
    }

    public void autoScroll() {
        isStop = false;
        autoEnable = true;
        sendChangeMessage();
    }

    public void stopAutoScroll() {
        autoEnable = false;
        isStop = true;
    }

    //初始化
    private void init() {
        if (data == null) return;
        XBannerAdapter adapter = new XBannerAdapter(getContext(), data, this);
        setAdapter(adapter);
        currentPosition = 1;
        setCurrentItem(currentPosition, false);//设置初始位置显示第二个，不用滚动的模式
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state != SCROLL_STATE_IDLE) return;//滑动未停止，不作处理
                if (currentPosition == 0)//当前是第一张，则切换显示倒数第二张
                    setCurrentItem(data.size() - 2, false);
                if (currentPosition == data.size() - 1)//当前是最后一张，则切换显示第二张
                    setCurrentItem(1, false);
            }
        });
    }

    @Override
    public void load(ImageView imageView, String s) {
        XImageClient.getInstance().load(imageView, s);
    }

    private void sendChangeMessage() {
        switchHandler.sendEmptyMessageDelayed(MessageWhat, switchDelayed);
    }

    private float actionDownX, actionDownY;
    private long actionDownTime = 0, actionUpTime = 0;
    private final long SingleTapUpTime = 300;
    private float actionDis = 30;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDownX = ev.getX();
                actionDownY = ev.getY();
                actionDownTime = System.currentTimeMillis();
                switchHandler.removeCallbacksAndMessages(null);
                break;
            case MotionEvent.ACTION_UP:
                actionUpTime = System.currentTimeMillis();
                if (actionUpTime - actionDownTime <= SingleTapUpTime &&
                        Math.abs(ev.getX() - actionDownX) < actionDis &&
                        Math.abs(ev.getY() - actionDownY) < actionDis) {//点击事件
                    if (xBannerItemClickListener != null)
                        xBannerItemClickListener.onItem(currentPosition);
                }
                sendChangeMessage();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


}
