package com.pm.cameraui.mvp;


import com.pm.cameraui.base.BaseView;
import com.pm.cameraui.bean.InspectRecord;
import com.pm.cameraui.bean.Mark;
import com.pm.cameraui.bean.RecordSave;
import com.pm.cameraui.bean.Topic;

import java.util.List;


public interface VideoView extends BaseView {
    void showJsonText(String text);
    void newInspectionTopic(InspectRecord record);
    void addMarkRecord(Mark mark);
    void updateInspection(InspectRecord record);
    void updateInspection2(RecordSave recordSave);
    void showTopicList(List<Topic> o);
    void onApplicationResult();
    void onTaskFinish();
}
