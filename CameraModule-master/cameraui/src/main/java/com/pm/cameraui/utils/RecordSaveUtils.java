package com.pm.cameraui.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.pm.cameraui.bean.RecordSave;
import com.pm.cameraui.bean.RecordSaveList;

public class RecordSaveUtils {

    //获取所有的未上传的数据
    public static RecordSaveList loadAllRecordSave(Context context) {
        String strRecordSaves = SPHelp.getInstance(context).getStringValue(SPHelp.SP_RECORD_SAVE);
        if (TextUtils.isEmpty(strRecordSaves)) {
            return new RecordSaveList();
        } else {
            RecordSaveList recordSaveList = new Gson().fromJson(strRecordSaves, RecordSaveList.class);
            return recordSaveList;
        }
    }

    //新增保存一条记录带了所有数据
    public static void saveRecordSaves(Context context, RecordSave recordSave) {
        RecordSaveList recordSaveList = loadAllRecordSave(context);
        if (recordSaveList != null && recordSaveList.recordSaves != null) {
            recordSaveList.recordSaves.add(recordSave);
        }
        String strSave = new Gson().toJson(recordSaveList);
        SPHelp.getInstance(context).setStringValue(SPHelp.SP_RECORD_SAVE, strSave);
    }

    public static void saveRecordSaves(Context context, RecordSaveList recordSaveList) {
        String strSave = new Gson().toJson(recordSaveList);
        SPHelp.getInstance(context).setStringValue(SPHelp.SP_RECORD_SAVE, strSave);
    }

    public static void removeRecordSave(Context context, RecordSave recordSave) {
        RecordSaveList recordSaveList = loadAllRecordSave(context);
        recordSaveList.removeReocrd(recordSave);
        saveRecordSaves(context, recordSaveList);
    }

    public static boolean haveLocalSaves(Context context) {
        return (loadAllRecordSave(context).recordSaves.size() > 0);
    }

}
