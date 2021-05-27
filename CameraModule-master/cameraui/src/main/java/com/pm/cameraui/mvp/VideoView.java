package com.pm.cameraui.mvp;


import com.pm.cameraui.base.BaseView;
import com.pm.cameraui.bean.InspectRecord;
import com.pm.cameraui.bean.Mark;


public interface VideoView extends BaseView {
    void showJsonText(String text);
    void newInspectionTopic(InspectRecord record);
    void addMarkRecord(Mark mark);
}
