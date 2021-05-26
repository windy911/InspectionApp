package com.pm.cameraui.utils;



import com.pm.cameraui.bean.InspectRecord;

import org.jetbrains.annotations.Nullable;

public class RecordUtil {
    private static String startTime = "";
    private static String endTime = "";
    private static long startTimeLong = 0;
    private static long endTimeLong = 0;

    public static InspectRecord startRecord(InspectRecord record){
        if (record == null) return null;
        startTimeLong = System.currentTimeMillis();
        startTime = TimeUtil.getFormatDateTime(startTimeLong);
        record.setStartTimeLong(startTimeLong);
        return record;
    }

    public static InspectRecord endRecord(InspectRecord record){
        if (record == null) return null;
        endTimeLong = System.currentTimeMillis();
        endTime = TimeUtil.getFormatDateTime(endTimeLong);
        startTimeLong = record.getStartTimeLong();
        startTime = record.getStartTime();
        record.setEndTimeLong(endTimeLong);
        record.setEndTime(endTime);
        record.setStartTime(TimeUtil.getFormatDateTime(endTimeLong));
        record.setEndTime(TimeUtil.getFormatDateTime(startTimeLong));
        record.setDuration(String.valueOf(endTimeLong - startTimeLong));
        return record;
    }

//    public static InspectRecord saveRecord(InspectRecord inspectRecord, String outFilePath, InspectVM inspectVM){
//
//        inspectRecord.setLocalVideoFilePath(outFilePath);
//        inspectRecord.setStartTime(TimeUtil.getFormatDateTime(endTimeLong));
//        inspectRecord.setEndTime(TimeUtil.getFormatDateTime(startTimeLong));
//        inspectRecord.setDuration(String.valueOf(endTimeLong - startTimeLong));
//        inspectRecord.setUploadStatus(1);
//        inspectRecord.setTraceLocus(LocationUtil.getLocationString());
//
////        inspectVM.updateInspectRecord(inspectRecord);
////        DaoManager.getInstance().getSession().getInspectRecordDao().update(inspectRecord);
//
//        return inspectRecord;
//    }

    public static void newRecord(@Nullable InspectRecord inspectRecord) {
//        DaoManager.getInstance().getSession().getInspectRecordDao().insert(inspectRecord);
    }

//    public static long getMarkCount(){
//        return DaoManager.getInstance().getSession().getMarkDao().queryBuilder().where(MarkDao.Properties.MarkedObjId.eq(inspectRecord.getId())).count();
//    }

}
