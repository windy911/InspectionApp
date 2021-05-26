package com.pm.cameraui.mvp;


import com.google.gson.Gson;
import com.pm.cameraui.Constants;
import com.pm.cameraui.base.BaseObserver;
import com.pm.cameraui.base.BasePresenter;
import com.pm.cameraui.bean.AppConfig;
import com.pm.cameraui.bean.InspectRecord;
import com.pm.cameraui.bean.Topic;
import com.pm.cameraui.bean.UserInfo;

import java.util.List;


public class VideoPresenter extends BasePresenter<VideoView> {
    public VideoPresenter(VideoView baseView) {
        super(baseView);
    }

    public void test(String userName,String password){
        addDisposable(apiServer.login(userName,password), new BaseObserver<UserInfo>(baseView) {
            @Override
            public void onSuccess(UserInfo o) {
                baseView.showJsonText("");
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
        addDisposable(apiServer.getAppConfiguration(), new BaseObserver<AppConfig>(baseView) {
            @Override
            public void onSuccess(AppConfig o) {
                //從服務器拿配置寫到本地臨時變量
                Constants.appConfig = o;
                baseView.showJsonText(new Gson().toJson(o));
            }
            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

    public void newInspectRecord(InspectRecord inspectRecord){
        addDisposable(apiServer.addInspectRecord(inspectRecord), new BaseObserver<InspectRecord>(baseView) {
            @Override
            public void onSuccess(InspectRecord o) {
//                baseView.showJsonText(new Gson().toJson(o));
                baseView.newInspectionTopic(o);
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

    public void updateInspectRecord(InspectRecord inspectRecord){
        addDisposable(apiServer.updateInspectRecord(inspectRecord), new BaseObserver<InspectRecord>(baseView) {
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

}
