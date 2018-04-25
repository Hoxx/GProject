package com.hxx.gprojrct.https;

import com.hxx.xlibrary.mvp.XBasicPresenter;

/**
 * Created by Android on 2018/3/19.
 */

public interface ContractHttps {

    interface View {

    }

    interface Presenter extends XBasicPresenter {
        void getHttpsData();
    }
}
