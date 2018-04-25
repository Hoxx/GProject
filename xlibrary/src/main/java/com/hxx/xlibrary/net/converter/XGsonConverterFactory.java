package com.hxx.xlibrary.net.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.hxx.xlibrary.net.XHttpCallback;
import com.hxx.xlibrary.util.L;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Android on 2017/8/30 .
 */

public class XGsonConverterFactory extends Converter.Factory {

    private final Gson gson;
    private XHttpCallback xHttpCallback;

    public static XGsonConverterFactory create(XHttpCallback callBack) {
        if (callBack == null)
            throw new NullPointerException("XHttpCallback == null");
        return new XGsonConverterFactory(new Gson(), callBack);
    }

    private XGsonConverterFactory(Gson gson, XHttpCallback callBack) {
        this.gson = gson;
        this.xHttpCallback = callBack;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new XGsonResponseBodyConverter<>(gson, type, adapter, xHttpCallback);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new XGsonRequestBodyConverter<>(gson, adapter);
    }

}
