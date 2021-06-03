package com.pm.cameraui.utils;


import com.google.gson.Gson;
import com.pm.cameraui.bean.InsLocation;
import com.pm.cameraui.bean.InspectRecord;

import java.util.ArrayList;
import java.util.List;

public class LocationUtil {
    public static InspectRecord currentRecord;
    private static boolean isEnableRecording = false;
    public static List<InsLocation> locationList = new ArrayList<>();
    public static String getLocationString(){
        return new Gson().toJson(locationList);
    }
    public static void clearAll(){locationList.clear();}
    public static void setEnableLocation(boolean enable){
        isEnableRecording = enable;
    }
    public static boolean isEnableRecording(){
        return isEnableRecording;
    }
}
