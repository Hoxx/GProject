package com.hxx.xlibrary.mvp;

import com.hxx.xlibrary.XDataClient;
import com.hxx.xlibrary.db.XDBClient;
import com.hxx.xlibrary.manager.XRequestManager;

/**
 * Created by Android on 2018/1/11.
 */

public abstract class XBasePresenter<V> {

    protected V view;

    public XBasePresenter(V view) {
        this.view = view;
    }


    public void onRestart() {

    }


    public void onResume() {

    }


    public void onPause() {

    }


    public void onStop() {

    }

    public <T> T getAPI(Class<T> service) {
        return XDataClient.getInstance().getAPI(service);
    }

    public XRequestManager XRequest() {
        return XDataClient.getInstance().XRequest();
    }

    public XDBClient XDB() {
        return XDataClient.getInstance().XDB();
    }

    public void detachView() {
        if (view != null) {
            view = null;
        }
    }

}