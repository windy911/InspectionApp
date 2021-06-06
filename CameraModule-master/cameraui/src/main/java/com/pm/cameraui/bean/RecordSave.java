package com.pm.cameraui.bean;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class RecordSave {

    public String videoFilePath;
    public InspectRecord inspectRecord;
    public String traceLocus;
    public ArrayList<Mark> markList = new ArrayList<>();
    public boolean isUploadFinished = false;

    public RecordSave(String videoFilePath, InspectRecord inspectRecord, String traceLocus, ArrayList<Mark> marks) {
        this.videoFilePath = videoFilePath;
        this.inspectRecord = inspectRecord;
        this.traceLocus = traceLocus;
        this.markList.addAll(marks);
        this.isUploadFinished = false;
    }

    public static void saveRecordSave(RecordSave recordSave) {
        String recordSaveStr = new Gson().toJson(recordSave);
        Log.d("RAMBO", "saveRecordSingle = " + recordSaveStr);
    }

    @Override
    public boolean equals(Object o) {
        if (videoFilePath != null) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RecordSave that = (RecordSave) o;
            return videoFilePath.equals(that.videoFilePath);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoFilePath);
    }
}
