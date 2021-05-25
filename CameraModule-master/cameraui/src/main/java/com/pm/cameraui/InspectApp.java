package com.pm.cameraui;

import android.app.Application;
import android.content.Context;

import com.hjq.toast.ToastUtils;


public class InspectApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
      ToastUtils.init(this);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }
}
