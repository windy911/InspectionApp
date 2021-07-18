package com.pm.cameraui.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.pm.cameraui.Constants;

import java.io.File;

public class DeviceUtil {
    public static boolean checkDiskSize() {
        try {
            File file =  Environment.getDataDirectory();
            StatFs statFs = new StatFs(file.getPath());
            long diskSize = statFs.getAvailableBytes();
            Log.e("DeviceUtil","diskSize="+diskSize+" appMiniDiskSpace="+ Constants.appConfig.appMiniDiskSpace);
            return diskSize > Constants.appConfig.appMiniDiskSpace;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void clearMediaFiles(Context context){
        Log.d("RAMBO","没有待上传文件，清理垃圾数据");
        try{
            final File dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            FileUtils.deleteFile(dir.getPath());
            FileUtils.makeDirs(dir.getPath());
            Log.d("RAMBO","清除缓存成功");
        }catch (Exception e){
            Log.d("RAMBO","清除缓存失败");
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
