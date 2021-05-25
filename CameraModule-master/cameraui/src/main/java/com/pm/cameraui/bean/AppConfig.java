package com.pm.cameraui.bean;

public class AppConfig {
    public String lbsClockTime = "3000";
    public HuaweiCloudConfig huaweiCloudConfig = new HuaweiCloudConfig();
    public long appMiniDiskSpace = 1024 * 1024 * 200;

//    {"huaWeiCloudConfig":
//        {"ak":"2IDWIZ2HQHNFR2KCAKRE",
//         "sk":"lBqbCqqM1DNXv8nprKdiduZgWHpFZDtR4aAlk8p0",
//         "projectId":"0c2fe2c31180f56e2f8ac01f5a8f2d87",
//         "bucketName":"rc-work-site-test",
//         "endPoint":"obs.cn-south-1.myhuaweicloud.com"},
//         "lbsClockTime":3000,"appMiniDiskSpace":2048}

    public static class HuaweiCloudConfig {
        public String ak = "2IDWIZ2HQHNFR2KCAKRE";
        public String sk = "lBqbCqqM1DNXv8nprKdiduZgWHpFZDtR4aAlk8p0";
        public String projectId = "0c2fe2c31180f56e2f8ac01f5a8f2d87";
        public String bucketName = "rc-work-site-test";
        public String endPoint = "obs.cn-south-1.myhuaweicloud.com";
    }
}
