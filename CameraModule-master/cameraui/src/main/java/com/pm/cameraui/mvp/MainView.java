package com.pm.cameraui.mvp;


import com.pm.cameraui.base.BaseView;
import com.pm.cameraui.bean.UserInfo;


public interface MainView extends BaseView {
    void onLogin(UserInfo o);
}
