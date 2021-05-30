package com.pm.cameraui.bean;

/**
 * 上传状态1:未上传 2:上传中 3:上传失败 4:上传完成
 *
 * 1、录屏中：如果异常更新记录状态异常；2、录屏完成，未上传：更新刚刚录完的视频信息（结束时间、时长、本机视频地址）+位置gps集合；3、上传中：更新上传状态；4、上传失败：更新状态和失败信息；5、上传完成：更新视频华为云存储地址
 */
public class UploadStatus {
    public static final int UPLOAD_NOT_START = 1;
    public static final int UPLOADING = 2;
    public static final int UPLOAD_FAIL = 3;
    public static final int UPLOAD_SUCCESS = 4;

}
