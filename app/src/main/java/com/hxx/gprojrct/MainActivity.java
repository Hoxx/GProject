package com.hxx.gprojrct;

import android.support.v4.app.Fragment;

import com.hxx.xlibrary.mvp.XBaseActivityView;


public class MainActivity extends XBaseActivityView<ContractMain.Presenter> implements ContractMain.View {

    @Override
    public int layoutResID() {
        return R.layout.activity_main;
    }

    @Override
    public ContractMain.Presenter createPresenter() {
        return new PresenterMain(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        switchFragmentContent(new ProgressFragment());
    }

    private void switchFragmentContent(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, fragment).commit();
    }

}
