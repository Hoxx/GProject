package com.hxx.gprojrct;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.hxx.gprojrct.view.PointProgress;
import com.hxx.xlibrary.mvp.XBaseFragmentView;
import com.hxx.xlibrary.mvp.XBasicPresenter;
import com.hxx.xlibrary.view.LocalDialog;


/**
 * Created by Android on 2018/3/15.
 */

public class ProgressFragment extends XBaseFragmentView implements View.OnClickListener {

    private PointProgress point_progress;
    private Button btn_pop;

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
        btn_pop = F(R.id.btn_pop);
        btn_pop.setOnClickListener(this);
    }

    @Override
    public void initData() {
        point_progress.setProgressColor(Color.BLUE, Color.YELLOW, Color.RED, Color.BLACK);
        point_progress.start(15000, "%.1fS");
        localDialog = new LocalDialog.Builder(getActivity()).setLayoutResId(R.layout.pop_voice_menu).setShowView(btn_pop).build();
    }

    private LocalDialog localDialog;


    @Override
    public void onClick(View v) {
        localDialog.showViewTop();
    }
}
