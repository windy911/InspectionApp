package com.pm.cameraui.mvp;


import com.google.gson.Gson;
import com.pm.cameraui.base.BaseObserver;
import com.pm.cameraui.base.BasePresenter;
import com.pm.cameraui.bean.AppConfig;
import com.pm.cameraui.bean.InspectRecord;
import com.pm.cameraui.bean.Topic;
import com.pm.cameraui.bean.UserInfo;

import java.util.List;


public class MvpPresenter extends BasePresenter<MvpView> {
    public MvpPresenter(MvpView baseView) {
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

    public void getInspectionTopic(){
        addDisposable(apiServer.getInspectionTopic(), new BaseObserver<List<Topic>>(baseView) {
            @Override
            public void onSuccess(List<Topic> o) {
                baseView.showJsonText(new Gson().toJson(o));
            }
            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

    public void getAppConfiguration(){
//        addDisposable(apiServer.getAppConfiguration(), new BaseObserver<AppConfig>(baseView) {
//            @Override
//            public void onSuccess(AppConfig o) {
//                baseView.showJsonText(new Gson().toJson(o));
//            }
//            @Override
//            public void onError(String msg) {
//                baseView.showError(msg);
//            }
//        });
    }

    public void newInspectRecord(InspectRecord inspectRecord){
        addDisposable(apiServer.addInspectRecord(inspectRecord), new BaseObserver<InspectRecord>(baseView) {
            @Override
            public void onSuccess(InspectRecord o) {
                baseView.showJsonText(new Gson().toJson(o));
            }
            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

    public void getInspectionList(){
        addDisposable(apiServer.getInspectionList(), new BaseObserver<List<InspectRecord>>(baseView) {
            @Override
            public void onSuccess(List<InspectRecord> o) {
                baseView.showJsonText(new Gson().toJson(o));
            }
            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }



}
