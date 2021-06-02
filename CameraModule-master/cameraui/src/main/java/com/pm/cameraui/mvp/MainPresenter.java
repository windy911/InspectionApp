package com.pm.cameraui.mvp;

import com.pm.cameraui.base.BaseObserver;
import com.pm.cameraui.base.BasePresenter;
import com.pm.cameraui.bean.UserInfo;
import com.pm.cameraui.utils.SPHelp;


public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(MainView baseView) {
        super(baseView);
    }

    public void login(String userName, String password) {
        addDisposable(apiServer.login(userName, password), new BaseObserver<UserInfo>(baseView) {
            @Override
            public void onSuccess(UserInfo o) {
                baseView.onLogin(o);
                SPHelp.getInstance(baseView.getActivity()).setStringValue(SPHelp.SP_LOGIN_NAME, userName);
                SPHelp.getInstance(baseView.getActivity()).setStringValue(SPHelp.SP_LOGIN_PASSWORD, password);
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

}
