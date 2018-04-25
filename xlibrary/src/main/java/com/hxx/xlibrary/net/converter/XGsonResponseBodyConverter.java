package com.hxx.xlibrary.net.converter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.hxx.xlibrary.exception.XResponseNotFountException;
import com.hxx.xlibrary.net.XHttpCallback;
import com.hxx.xlibrary.net.XHttpConstant;
import com.hxx.xlibrary.util.L;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

/**
 * Created by Android on 2017/8/30 .
 */

public class XGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Type type;
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private XHttpCallback xHttpCallback;

    public XGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, XHttpCallback converterCallBack) {
        this.gson = gson;
        this.adapter = adapter;
        this.xHttpCallback = converterCallBack;
    }

    public XGsonResponseBodyConverter(Gson gson, Type type, TypeAdapter<T> adapter, XHttpCallback converterCallBack) {
        this.type = type;
        this.gson = gson;
        this.adapter = adapter;
        this.xHttpCallback = converterCallBack;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();//ResponseBody.string()只能调用一次
        //判断是否有返回数据
        if (TextUtils.isEmpty(response)) {
            value.close();
            throw new XResponseNotFountException();
        }
        if (xHttpCallback != null) {
            L.e(XHttpConstant.HTTP_LOG_TAG, "自定义处理Exception");
            xHttpCallback.onConverter(gson, response);
        }
        Charset charset = value.contentType() != null ? value.contentType().charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            //添加处理String结果返回
            if (TextUtils.equals(type.toString(), String.class.toString())) {
                return (T) response;
            } else {
                return adapter.read(jsonReader);
            }
        } finally {
            value.close();
        }
    }


}
