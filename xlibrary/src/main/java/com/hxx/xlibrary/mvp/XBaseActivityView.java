package com.hxx.xlibrary.mvp;

import android.os.Bundle;

/**
 * Created by Android on 2018/1/11.
 */

public abstract class XBaseActivityView<P extends XBasicPresenter> extends XBasicActivity {

    public P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID());
        presenter = createPresenter();
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (presenter != null)
            presenter.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null)
            presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter != null)
            presenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null)
            presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachPresenter();
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
