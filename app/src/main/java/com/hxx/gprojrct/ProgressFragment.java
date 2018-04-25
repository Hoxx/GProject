package com.hxx.gprojrct;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxx.gprojrct.view.PointProgress;
import com.hxx.xlibrary.mvp.XBaseFragmentView;
import com.hxx.xlibrary.mvp.XBasicPresenter;


/**
 * Created by Android on 2018/3/15.
 */

public class ProgressFragment extends XBaseFragmentView {

    private PointProgress point_progress;

    @Override
    public int layoutResID() {
        return R.layout.progress_fragment;
    }

    @Override
    public XBasicPresenter createPresenter() {
        return null;
    }

    @Override
    public void initView() {
        point_progress = F(R.id.point_progress);
    }

    @Override
    public void initData() {
        point_progress.setProgressColor(Color.BLUE, Color.YELLOW, Color.RED, Color.BLACK);
        point_progress.start(15000, "%.1fS");
    }

}
