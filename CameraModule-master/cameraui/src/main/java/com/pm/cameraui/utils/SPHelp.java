package com.pm.cameraui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * sharePreference帮助类
 */
public class SPHelp {

    public static final String SP_LOGIN_NAME = "SP_LOGIN_NAME";
    public static final String SP_LOGIN_PASSWORD = "SP_LOGIN_PASSWORD";

    public static final String SP_RECORD_SAVE = "SP_RECORD_SAVE";


    public static SPHelp instance;
    private SharedPreferences preferences;

    private SPHelp() {
    }

    public static synchronized SPHelp getInstance(Context context) {
        if (instance == null) {
            instance = new SPHelp();
            instance.preferences = context.getSharedPreferences("CAMERA_SHARED_PREFERENCE", 0);
        }
        return instance;
    }

    /**
     * desc:将数组转为16进制
     *
     * @return modified:
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * desc:将16进制的数据转为数组
     *
     * @return modified:
     */
    public static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int int_ch3;
            if (hex_char1 >= '0' && hex_char1 <= '9') {
                int_ch3 = (hex_char1 - 48) * 16;   //// 0 的Ascll - 48
            } else if (hex_char1 >= 'A' && hex_char1 <= 'F') {
                int_ch3 = (hex_char1 - 55) * 16; //// A 的Ascll - 65
            } else {
                return null;
            }
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch4;
            if (hex_char2 >= '0' && hex_char2 <= '9') {
                int_ch4 = (hex_char2 - 48); //// 0 的Ascll - 48
            } else if (hex_char2 >= 'A' && hex_char2 <= 'F') {
                int_ch4 = hex_char2 - 55; //// A 的Ascll - 65
            } else {
                return null;
            }
            int_ch = int_ch3 + int_ch4;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }

    /**
     * 存储Int 的值
     */
    public void setIntValue(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    /**
     * 获取int 的值
     */
    public int getIntValue(String key) {
        if (preferences.contains(key)) {
            return preferences.getInt(key, 0);
        }
        return 0;
    }

    public int getIntValue(String key, int defaultValue) {
//    if (preferences.contains(key)) {
        return preferences.getInt(key, defaultValue);
//    }
//    return 0;
    }

    /**
     * 存储String 的值
     */
    public void setStringValue(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    /**
     * 获取String 的值
     */
    public String getStringValue(String key) {
        if (preferences.contains(key)) {
            return preferences.getString(key, "");
        }
        return "";
    }

    /**
     * 存储Boolean 的值 ；
     */
    public void setBooleanValue(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 获取Boolean值
     */
    public boolean getBooleanValue(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    /**
     * @Title: get3gValue
     * @Description: 获取默认值为true的方法
     * @param: key
     * @param:
     * @return: boolean
     */
    public boolean getBoolValue(String key) {
        return preferences.getBoolean(key, true);
    }

    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    /*
    保存一个Serializable对象
     */
    public void setObjectValue(String key, Object value) {
        try {

            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(value);
            //将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            preferences.edit().putString(key, bytesToHexString).commit();
        } catch (Exception e) {
        }

    }

    /*
    读取一个Serializable对象
     */
    public Object getObjectValue(String key) {
        try {
            String strObject = preferences.getString(key, "");
            if (!TextUtils.isEmpty(strObject)) {
                byte[] stringToBytes = StringToBytes(strObject);
                ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                ObjectInputStream is = new ObjectInputStream(bis);
                //返回反序列化得到的对象
                Object readObject = is.readObject();
                return readObject;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
