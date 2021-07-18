package com.pm.cameraui.utils;

import android.content.Context;
import android.util.Log;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.AccessControlList;
import com.obs.services.model.CompleteMultipartUploadResult;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.ProgressListener;
import com.obs.services.model.UploadFileRequest;
import com.pm.cameraui.Constants;
import com.pm.cameraui.bean.AppConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;

public class UploadUtil {

    public static final String TAG = "RAMBO";
//    public static final String AK = "2IDWIZ2HQHNFR2KCAKRE";
//    public static final String SK = "lBqbCqqM1DNXv8nprKdiduZgWHpFZDtR4aAlk8p0";
//    public static final String ENDPOINT = "obs.cn-south-1.myhuaweicloud.com";
//    public static final String BUCKET = "rc-work-site-test";

    /**
     * 上传华为云
     */
    public static void upload(String filePath, ProgressListener progressListener, OnUploadsListener uploadsListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppConfig.HuaweiCloudConfig huaweiCloudConfig = Constants.appConfig.huaWeiCloudConfig;
                String objectKey = "video/" + filePath.substring(filePath.lastIndexOf("/") + 1);
                ObsClient obsClient = new ObsClient(huaweiCloudConfig.ak, huaweiCloudConfig.sk, huaweiCloudConfig.endPoint);
                UploadFileRequest request = new UploadFileRequest(huaweiCloudConfig.bucketName, objectKey);
                request.setUploadFile(filePath);
                request.setTaskNum(10);
                request.setEnableCheckpoint(true);
                ObjectMetadata objectMetadata = new ObjectMetadata();
//                objectMetadata.setContentType("video/mp4;charset=UTF-8");
                request.setObjectMetadata(objectMetadata);
                request.setProgressListener(progressListener);

                try {
                    // 进行断点续传上传
                    CompleteMultipartUploadResult result = obsClient.uploadFile(request);
                    obsClient.setObjectAcl(huaweiCloudConfig.bucketName, result.getObjectKey(), AccessControlList.REST_CANNED_PUBLIC_READ);
                    if (uploadsListener != null) {
                        uploadsListener.onUploadSuccess(filePath, result.getObjectUrl());
                    }
                    Log.i(TAG, result.toString());
                } catch (ObsException e) {
                    //发生异常时可再次调用断点续传上传接口进行重新上传
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 上传华为云
     */
    public static void uploadImage(String filePath, OnUploadsListener uploadsListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppConfig.HuaweiCloudConfig huaweiCloudConfig = Constants.appConfig.huaWeiCloudConfig;
                String objectKey = "pic/" + filePath.substring(filePath.lastIndexOf("/") + 1);
                ObsClient obsClient = new ObsClient(huaweiCloudConfig.ak, huaweiCloudConfig.sk, huaweiCloudConfig.endPoint);
                UploadFileRequest request = new UploadFileRequest(huaweiCloudConfig.bucketName, objectKey);
                request.setUploadFile(filePath);

                try {
                    // 进行断点续传上传
                    CompleteMultipartUploadResult result = obsClient.uploadFile(request);
                    obsClient.setObjectAcl(huaweiCloudConfig.bucketName, result.getObjectKey(), AccessControlList.REST_CANNED_PUBLIC_READ);
                    if (uploadsListener != null) {
                        uploadsListener.onUploadSuccess(filePath, result.getObjectUrl());
                    }
                    Log.i(TAG, result.toString());
                } catch (ObsException e) {
                    //发生异常时可再次调用断点续传上传接口进行重新上传
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    /**
     * 合并多个视频文件
     *
     * @param context   context
     * @param outFile   输出文件路径
     * @param videoPath 待合并的文件路径列表
     */
    public static void mergeVideos(Context context, String outFile, List<String> videoPath, OnMergeSuccessListener onMergeSuccessListener) {

        if(videoPath.size()==1){
            //当只有一个视频不需要合并
            new Thread(new Runnable() {
                @Override
                public void run() {
                        Log.d("RAMBO","只有一个文件："+videoPath.get(0));

                        try{
                            //重命名
                            //new File(videoPath.get(0)).renameTo(new File(outFile));

                            onMergeSuccessListener.onMergeSuccess(videoPath.get(0));
                        }catch (Exception e){
                            onMergeSuccessListener.onMergeSuccess("");
                        }
                }
            }).start();

        } else if(videoPath.size()>1){
            ArrayList<EpVideo> epVideos = new ArrayList<>();
            for (String url : videoPath) {
                Log.e(TAG, "RAMBO 合并开始文件 = " + url);
                epVideos.add(new EpVideo(url));
            }

            try {
                Log.e(TAG, "RAMBO 合并开始 epVideos size = " + epVideos.size());
            //merge 方法速度太慢了，mergeByLc速度更快但会莫名的直接失败。
            EpEditor.mergeByLc(context, epVideos, new EpEditor.OutputOption(outFile), new OnEditorListener() {
//                EpEditor.merge(epVideos, new EpEditor.OutputOption(outFile), new OnEditorListener() {
                    @Override
                    public void onSuccess() {
                        Log.e(TAG, "合并成功 输出路径：" + outFile);
                        for (String path : videoPath) {
                            Log.e(TAG, "RAMBO 合并成功清理删除临时文件：" + path);
                            FileUtils.deleteFile(path);
                        }
                        if (onMergeSuccessListener != null) {
                            onMergeSuccessListener.onMergeSuccess(outFile);
                        }
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "RAMBO 合并失败");
                        if (onMergeSuccessListener != null) {
                            onMergeSuccessListener.onMergeSuccess("");
                        }
                    }

                    @Override
                    public void onProgress(float progress) {
                        Log.i(TAG, "合并中，进度：" + progress);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "RAMBO 合并失败 e =" + e);
                if (onMergeSuccessListener != null) {
                    onMergeSuccessListener.onMergeSuccess("");
                }
            }
        }



    }

//多条合并快速失败后慢速实现
    public static void mergeVideosSlow(Context context, String outFile, List<String> videoPath, OnMergeSuccessListener onMergeSuccessListener) {


            ArrayList<EpVideo> epVideos = new ArrayList<>();
            for (String url : videoPath) {
                Log.e(TAG, "RAMBO 合并开始文件 = " + url);
                epVideos.add(new EpVideo(url));
            }

            try {
                Log.e(TAG, "RAMBO 合并开始 epVideos size = " + epVideos.size());
                //merge 方法速度太慢了，mergeByLc速度更快但会莫名的直接失败。
                //EpEditor.mergeByLc(context, epVideos, new EpEditor.OutputOption(outFile), new OnEditorListener() {
                                 EpEditor.merge(epVideos, new EpEditor.OutputOption(outFile), new OnEditorListener() {
                    @Override
                    public void onSuccess() {
                        Log.e(TAG, "合并成功 输出路径：" + outFile);
                        for (String path : videoPath) {
                            Log.e(TAG, "RAMBO 合并成功清理删除临时文件：" + path);
                            FileUtils.deleteFile(path);
                        }
                        if (onMergeSuccessListener != null) {
                            onMergeSuccessListener.onMergeSuccess(outFile);
                        }
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "RAMBO 合并失败");
                        if (onMergeSuccessListener != null) {
                            onMergeSuccessListener.onMergeSuccess("");
                        }
                    }

                    @Override
                    public void onProgress(float progress) {
                        Log.i(TAG, "合并中，进度：" + progress);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "RAMBO 合并失败 e =" + e);
                if (onMergeSuccessListener != null) {
                    onMergeSuccessListener.onMergeSuccess("");
                }
            }




    }

    public interface OnMergeSuccessListener {
        void onMergeSuccess(String outFile);
    }

    public interface OnUploadsListener {
        void onUploadSuccess(String localPath, String remoteUrl);
    }

}
