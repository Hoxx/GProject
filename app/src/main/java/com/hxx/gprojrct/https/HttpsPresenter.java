package com.hxx.gprojrct.https;

import com.hxx.gprojrct.constant.API;
import com.hxx.xlibrary.XDataClient;
import com.hxx.xlibrary.mvp.XBasePresenter;
import com.hxx.xlibrary.net.XOnRequestCallback;
import com.hxx.xlibrary.net.XRequestError;
import com.hxx.xlibrary.util.L;

/**
 * Created by Android on 2018/3/19.
 */

public class HttpsPresenter extends XBasePresenter<ContractHttps.View> implements ContractHttps.Presenter {

    private String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=15210710762";

    public HttpsPresenter(ContractHttps.View view) {
        super(view);
    }


    @Override
    public void getHttpsData() {
        XRequest().request(getAPI(API.class).getDataFromHttps(url),
                new XOnRequestCallback<String>() {
                    @Override
                    public void onSuccess(String value) {
                        L.e("onSuccess：" + value);
                    }

                    @Override
                    public void onFailed(XRequestError error) {

                    }
                });
    }
}
