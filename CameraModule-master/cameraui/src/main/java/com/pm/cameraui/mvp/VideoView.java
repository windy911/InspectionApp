package com.pm.cameraui.mvp;


import com.pm.cameraui.base.BaseView;
import com.pm.cameraui.bean.InspectRecord;


public interface VideoView extends BaseView {
    void showJsonText(String text);
    void newInspectionTopic(InspectRecord record);
}
