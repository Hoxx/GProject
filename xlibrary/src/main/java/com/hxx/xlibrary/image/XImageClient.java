package com.hxx.xlibrary.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Android on 2017/12/19 .
 */

public class XImageClient implements XImageLoadInterface {

    private static XImageClient instance=new XImageClient();

    private Context context;

    public static XImageClient getInstance() {
        if (instance == null)
            instance = new XImageClient();
        return instance;
    }

    public void init(Context c) {
        this.context = c;
    }

    @Override
    public void load(ImageView view, String url) {
        Glide.with(context).load(url).into(view);
    }

    @Override
    public void load(ImageView view, int resID) {
        Glide.with(context).load(resID).into(view);
    }
}
