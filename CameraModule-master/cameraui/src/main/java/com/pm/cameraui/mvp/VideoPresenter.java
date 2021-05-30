package com.pm.cameraui.mvp;


import android.util.Log;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.pm.cameraui.Constants;
import com.pm.cameraui.base.BaseObserver;
import com.pm.cameraui.base.BasePresenter;
import com.pm.cameraui.bean.AppConfig;
import com.pm.cameraui.bean.InspectRecord;
import com.pm.cameraui.bean.Mark;
import com.pm.cameraui.bean.Topic;
import com.pm.cameraui.bean.UserInfo;
import com.pm.cameraui.utils.MarkUtil;

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
                baseView.showTopicList(o);
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
                baseView.onApplicationResult();
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
        Log.d("RAMBO","updateInspectRecord："+inspectRecord.toString());
        addDisposable(apiServer.updateInspectRecord(inspectRecord), new BaseObserver<InspectRecord>(baseView) {
            @Override
            public void onSuccess(InspectRecord o) {
                baseView.updateInspection(o);
                Log.d("RAMBO","InspectRecord 上传成功："+o.toString());
            }
            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }


    public void addMarkRecord(Mark mark){
        addDisposable(apiServer.addMarkRecord(mark), new BaseObserver<Mark>(baseView) {
            @Override
            public void onSuccess(Mark o) {
                baseView.addMarkRecord(o);
            }
            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

    public void updateMarkRecord(Mark mark){


        if(baseView!=null&&mark==null){
            baseView.onTaskFinish();
            return;
        }

        addDisposable(apiServer.updateMarkRecord(mark), new BaseObserver<Mark>(baseView) {
            @Override
            public void onSuccess(Mark o) {
//              baseView.addMarkRecord(o);
                Log.d("RAMBO","updateMarkRecord Success :" +mark.toString());
                if(MarkUtil.isFinishUpload()){
                    baseView.onTaskFinish();
                }
            }
            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

}
