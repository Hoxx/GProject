package com.hxx.xlibrary.view.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/12/19 .
 */

public abstract class XBannerBaseAdapter<T> extends PagerAdapter {

    private Context context;
    private List<T> data;
    private List<View> views;
    private XBannerLoadDataCallback<ImageView, T> xBannerLoadDataCallback;


    public XBannerBaseAdapter(Context context, List<T> data, XBannerLoadDataCallback<ImageView, T> callback) {
        this.context = context;
        this.data = data;
        this.xBannerLoadDataCallback = callback;
        initView();
    }

    public Context getContext() {
        return context;
    }

    public XBannerLoadDataCallback<ImageView, T> getxBannerLoadDataCallback() {
        return xBannerLoadDataCallback;
    }

    //初始化视图集合
    private void initView() {
        if (data.size() > 1) {
            T tFirst = data.get(0);
            T tLast = data.get(data.size() - 1);
            data.add(0, tLast);//复制最后一个元素添加到列表首部
            data.add(tFirst);//复制第一个元素添加到列表尾部
        }
        if (views == null)
            views = new ArrayList<>();
        views.clear();
        for (T t : data) {
            views.add(getItemView(t));
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    public abstract View getItemView(T t);

}
