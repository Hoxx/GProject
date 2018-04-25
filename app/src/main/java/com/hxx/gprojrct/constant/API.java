package com.hxx.gprojrct.constant;

import com.hxx.gprojrct.bean.HttpsBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Android on 2018/3/19.
 */

public interface API {

    @GET
    Call<String> getDataFromHttps(@Url String url);

}
