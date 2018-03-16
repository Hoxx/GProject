package com.hxx.xlibrary.net.converter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.hxx.xlibrary.exception.XResponseNotFountException;
import com.hxx.xlibrary.net.XHttpConstant;
import com.hxx.xlibrary.util.L;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

/**
 * Created by Android on 2017/8/30 .
 */

public class XGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private XResponseBodyConverterCallBack converterCallBack;

    public XGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, XResponseBodyConverterCallBack converterCallBack) {
        this.gson = gson;
        this.adapter = adapter;
        this.converterCallBack = converterCallBack;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();//ResponseBody.string()只能调用一次
        //判断是否有返回数据
        if (TextUtils.isEmpty(response)) {
            value.close();
            throw new XResponseNotFountException();
        }
        if (converterCallBack != null) {
            L.e(XHttpConstant.HTTP_LOG_TAG, "自定义处理Exception");
            converterCallBack.onConverter(gson,response);
        }
        Charset charset = value.contentType() != null ? value.contentType().charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }


}
