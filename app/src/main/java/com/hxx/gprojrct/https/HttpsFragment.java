package com.hxx.gprojrct.https;

import com.hxx.gprojrct.R;
import com.hxx.xlibrary.mvp.XBaseFragmentView;

/**
 * Created by Android on 2018/3/19.
 */

public class HttpsFragment extends XBaseFragmentView<ContractHttps.Presenter> implements ContractHttps.View {
    @Override
    public int layoutResID() {
        return R.layout.https_fragment;
    }

    @Override
    public ContractHttps.Presenter createPresenter() {
        return new HttpsPresenter(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        presenter.getHttpsData();
    }
}
