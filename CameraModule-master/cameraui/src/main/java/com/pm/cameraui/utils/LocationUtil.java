package com.pm.cameraui.utils;


import com.pm.cameraui.bean.InsLocation;
import com.pm.cameraui.bean.InspectRecord;

import java.util.ArrayList;
import java.util.List;


public class LocationUtil {
    public static InspectRecord currentRecord;
    public static List<InsLocation> locationList = new ArrayList<>();
    public static String getLocationString(){
        return locationList.toString();
    }
    public static void clearAll(){locationList.clear();}
}
