package com.hxx.xlibrary.mvp;

/**
 * Created by Android on 2018/1/11.
 */

public abstract class XBasePresenter<V> {

    protected V view;

    public XBasePresenter(V view) {
        this.view = view;
    }


    public void onStart() {

    }
    
    public void onRestart() {

    }


    public void onResume() {

    }


    public void onPause() {

    }


    public void onStop() {

    }


    public void detachView() {
        if (view != null) {
            view = null;
        }
    }

}