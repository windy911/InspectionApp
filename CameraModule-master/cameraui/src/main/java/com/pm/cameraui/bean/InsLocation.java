package com.pm.cameraui.bean;

/**
 * 地里位置
 */
public class InsLocation {
    /**
     * 时间戳
     */
    private long timeStamp;
    /**
     * 经度
     */
    private String latitude;
    /**
     * 纬度l
     */
    private String longitude;
    /**
     * 地址信息
     */
    private String address;

    public InsLocation(String latitude, String longitude, String address, long timeStamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "latitude='" + latitude+
                ", longitude='" + longitude+
                ", address='" + address+
                ", timeStamp="+timeStamp;
    }
}
