package com.hxx.xlibrary.mvp;

import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;

import com.hxx.xlibrary.util.L;

/**
 * Created by Android on 2018/1/11.
 */

public abstract class XBaseActivityView<P extends XBasicPresenter> extends XBasicActivity {

    public P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e("XBaseActivityView-onCreate");
        setContentView(layoutResID());
        presenter = createPresenter();
        initView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        L.e("XBaseActivityView-onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        L.e("XBaseActivityView-onRestart");
        if (presenter != null)
            presenter.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.e("XBaseActivityView-onResume");
        if (presenter != null)
            presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.e("XBaseActivityView-onPause");
        if (presenter != null)
            presenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.e("XBaseActivityView-onStop");
        if (presenter != null)
            presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e("XBaseActivityView-onDestroy");
        detachPresenter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        L.e("XBaseActivityView-onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        L.e("XBaseActivityView-onRestoreInstanceState");
    }

    //解除关联
    private void detachPresenter() {
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
    }

    public abstract int layoutResID();

    public abstract P createPresenter();

    public abstract void initView();

    public abstract void initData();


}
