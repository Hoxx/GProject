package com.hxx.xlibrary.net.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

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

    public static XGsonConverterFactory create() {
        return create(new Gson());
    }

    public static XGsonConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new XGsonConverterFactory(gson, null);
    }

    public static XGsonConverterFactory create(XResponseBodyConverterCallBack callBack) {
        if (callBack == null)
            throw new NullPointerException("XResponseBodyConverterCallBack == null");
        return new XGsonConverterFactory(new Gson(), callBack);
    }

    public static XGsonConverterFactory create(Gson gson, XResponseBodyConverterCallBack callBack) {
        if (callBack == null)
            throw new NullPointerException("XResponseBodyConverterCallBack == null");
        if (gson == null) throw new NullPointerException("gson == null");
        return new XGsonConverterFactory(gson, callBack);
    }

    private final Gson gson;
    private XResponseBodyConverterCallBack xResponseBodyConverterCallBack;

    private XGsonConverterFactory(Gson gson, XResponseBodyConverterCallBack callBack) {
        this.gson = gson;
        this.xResponseBodyConverterCallBack = callBack;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new XGsonResponseBodyConverter<>(gson, adapter, xResponseBodyConverterCallBack);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new XGsonRequestBodyConverter<>(gson, adapter);
    }

}
