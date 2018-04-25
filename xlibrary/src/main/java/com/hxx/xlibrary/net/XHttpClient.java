package com.hxx.xlibrary.net;

import android.util.SparseArray;

import com.hxx.xlibrary.exception.XHttpNotFoundBaseUrlException;
import com.hxx.xlibrary.exception.XNetInitializeException;
import com.hxx.xlibrary.net.converter.XGsonConverterFactory;
import com.hxx.xlibrary.util.L;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by Android on 2017/12/20.
 */

public class XHttpClient {

    private static XHttpClient instance = new XHttpClient();

    private OkHttpClient.Builder builder;
    private Retrofit retrofit;

    private XHttpCallback xHttpCallback;
    //请求头参数处理
    private SparseArray<Map<String, String>> params;

    public static XHttpClient getInstance() {
        if (instance == null)
            instance = new XHttpClient();
        return instance;
    }

    public void init(XHttpCallback callback) {
        if (builder != null) return;
        if (callback == null) throw new XHttpNotFoundBaseUrlException();
        this.xHttpCallback = callback;
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(XHttpConstant.HTTP_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS);
        builder.readTimeout(XHttpConstant.HTTP_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS);
        builder.writeTimeout(XHttpConstant.HTTP_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS);
        if (XHttpConstant.DEBUG)
            builder.addInterceptor(getHttpLoggingInterceptor());//日志数出拦截器
        builder.addInterceptor(getParamsInterceptor());//头部参数拦截器
        builder.retryOnConnectionFailure(true);//失败重试
        builder();
    }

    //清除参数
    public void clearParams() {
        if (params != null) {
            params.clear();
            params = null;
        }
    }

    //创建请求接口实例
    public <T> T createService(Class<T> service) {
        if (retrofit == null)
            throw new XNetInitializeException();
        return retrofit.create(service);
    }

    //创建请求框架
    private void builder() {
        OkHttpClient okHttpClient = builder.build();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient);
        if (xHttpCallback != null) {
            builder.baseUrl(xHttpCallback.getHttpBaseUrl());
            builder.addConverterFactory(XGsonConverterFactory.create(xHttpCallback));
        }
        retrofit = builder.build();
    }

    //网络请求控制台日志输出
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        L.e(XHttpConstant.HTTP_LOG_TAG, "添加网络请求控制台日志输出");
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                L.e(XHttpConstant.HTTP_LOG_TAG, message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    //添加请求头部参数
    private Interceptor getParamsInterceptor() {
        L.e(XHttpConstant.HTTP_LOG_TAG, "添加请求头部参数");
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                initParams();
                if (params == null || params.size() <= 0) return chain.proceed(chain.request());
                Request origin = chain.request();
                HttpUrl.Builder httpUrlBuilder = origin.url().newBuilder();
                Request.Builder builder = origin.newBuilder();
                for (int i = 0; i < params.size(); i++) {
                    int paramKey = params.keyAt(i);
                    L.e(XHttpConstant.HTTP_LOG_TAG, "请求参数类型-Key:" + paramKey);
                    Map<String, String> paramValue = params.valueAt(i);
                    for (Map.Entry<String, String> itemEntry : paramValue.entrySet()) {
                        String key = itemEntry.getKey();
                        String value = itemEntry.getValue();
                        L.e(XHttpConstant.HTTP_LOG_TAG, "请求参数-Key:" + key);
                        L.e(XHttpConstant.HTTP_LOG_TAG, "请求参数-Value:" + value);
                        switch (paramKey) {
                            case XHttpConstant.PARAMS_TYPE_HEAD:
                                builder.addHeader(key, value);
                                break;
                            case XHttpConstant.PARAMS_TYPE_QUERY:
                                httpUrlBuilder.addQueryParameter(key, value);
                                break;
                        }
                    }
                }
                builder.url(httpUrlBuilder.build());
                return chain.proceed(builder.build());
            }
        };
    }

    //初始化请求头参数
    private void initParams() {
        L.e(XHttpConstant.HTTP_LOG_TAG, "初始化请求头参数");
        if (params == null || params.size() <= 0) {
            if (xHttpCallback instanceof XHttpHeaderParamsCallback) {
                params = ((XHttpHeaderParamsCallback) xHttpCallback).getParams();
            }
        }
    }

}
