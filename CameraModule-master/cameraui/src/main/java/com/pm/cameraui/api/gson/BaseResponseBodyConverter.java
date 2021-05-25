package com.pm.cameraui.api.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.pm.cameraui.api.BaseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

public class BaseResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    BaseResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource buffer = Okio.buffer(value.source());
        String jsonString = buffer.readUtf8();
        try {
            JSONObject object = new JSONObject(jsonString);
            int errorCode = object.getInt("code");
            if (errorCode != 1) {
                String errorMsg = object.getString("msg");
                //异常处理
                throw new BaseException(errorMsg, errorCode);
            }

            return adapter.fromJson(object.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            //数据解析异常
            throw new BaseException(BaseException.PARSE_ERROR_MSG, BaseException.PARSE_ERROR);
        } finally {
            value.close();
        }
    }
}
