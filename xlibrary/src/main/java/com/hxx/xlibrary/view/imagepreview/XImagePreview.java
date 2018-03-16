package com.hxx.xlibrary.view.imagepreview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2018/1/5.
 */

public class XImagePreview extends ViewPager {

    private List<ImageView> views;

    public XImagePreview(Context context) {
        super(context);
        init();
    }

    public XImagePreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        views = new ArrayList<>();
    }


    public void setData(List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            XImagePreviewView view = new XImagePreviewView(getContext());
            views.add(view);
        }
        ImageAdapter adapter = new ImageAdapter(data, views);
        setAdapter(adapter);
    }

    class ImageAdapter extends PagerAdapter {

        private List<String> data;
        private List<ImageView> views;
        private ViewGroup.LayoutParams layoutParams;

        public ImageAdapter(List<String> data, List<ImageView> views) {
            this.data = data;
            this.views = views;
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        public int getCount() {
            if (data == null) return 0;
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = views.get(position);
            ((XImagePreviewView) imageView).setImageData(data.get(position));
            container.addView(imageView, layoutParams);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView imageView = views.get(position);
            container.removeView(imageView);
        }
    }

}
