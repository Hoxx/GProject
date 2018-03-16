package com.hxx.xlibrary.image;

import android.widget.ImageView;

/**
 * Created by Android on 2017/12/19 .
 */

public interface XImageLoadInterface {

    void load(ImageView view, String url);

    void load(ImageView view, int resID);

}
