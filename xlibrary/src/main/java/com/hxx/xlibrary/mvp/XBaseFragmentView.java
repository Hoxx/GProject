package com.hxx.xlibrary.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Android on 2018/1/11.
 */

public abstract class XBaseFragmentView<P extends XBasicPresenter> extends XBasicFragment {

    public P presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fragment被创建时调用
        presenter = createPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //创建Fragment的布局
        return inflater.inflate(layoutResID(), container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //当Activity完成onCreate()时调用
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null)
            presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null)
            presenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null)
            presenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //当Fragment的UI从视图结构中移除时调用
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁Fragment时调用
        detachPresenter();
    }

    //解除Presenter关联
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
