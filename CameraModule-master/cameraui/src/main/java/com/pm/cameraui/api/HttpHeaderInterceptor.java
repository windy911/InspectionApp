package com.pm.cameraui.api;

import android.util.Log;

import com.pm.cameraui.Constants;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

class HttpHeaderInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;
        try {
            Log.d("addHeader", "Before");
            newRequest = request.newBuilder()
                    .addHeader("token", getToken())
                    .build();
        } catch (Exception e) {
            Log.d("addHeader", "Error");
            e.printStackTrace();
            return chain.proceed(request);
        }
        Log.d("addHeader", "after");
        return chain.proceed(newRequest);
    }

    //直接返回本地Token
    public static String getToken() {
        return Constants.userInfo == null ? "" : Constants.userInfo.getToken();
    }
}
