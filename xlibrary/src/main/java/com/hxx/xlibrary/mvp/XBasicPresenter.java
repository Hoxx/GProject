package com.hxx.xlibrary.mvp;

/**
 * Created by Android on 2018/1/11.
 */

public interface XBasicPresenter {

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void detachView();
}
