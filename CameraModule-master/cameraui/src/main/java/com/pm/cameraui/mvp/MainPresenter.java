package com.pm.cameraui.mvp;


import com.pm.cameraui.base.BaseObserver;
import com.pm.cameraui.base.BasePresenter;
import com.pm.cameraui.bean.UserInfo;


public class MainPresenter extends BasePresenter<MainView> {
    public MainPresenter(MainView baseView) {
        super(baseView);
    }

    public void login(String userName,String password){
        addDisposable(apiServer.login(userName,password), new BaseObserver<UserInfo>(baseView) {
            @Override
            public void onSuccess(UserInfo o) {
                baseView.onLogin(o);
            }
            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

}
