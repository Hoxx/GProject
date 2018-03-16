package com.hxx.xlibrary.view.banner;

import android.view.View;

/**
 * Created by Android on 2017/12/19 .
 */

public interface XBannerLoadDataCallback<V extends View, T> {

    void load(V v, T t);
}
