package com.hxx.xlibrary.view.banner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Android on 2017/12/19 .
 */

public class XBannerAdapter extends XBannerBaseAdapter<String> {

    private ViewGroup.LayoutParams layoutParams;

    XBannerAdapter(Context context, List<String> data, XBannerLoadDataCallback<ImageView, String> xBannerLoadDataCallback) {
        super(context, data, xBannerLoadDataCallback);
    }

    @Override
    public View getItemView(String s) {
        if (layoutParams == null)
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (getxBannerLoadDataCallback() != null)
            getxBannerLoadDataCallback().load(imageView, s);
        return imageView;
    }


}
