package com.pm.cameraui.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;

public class FFmpegUtil {

    private static final String TAG = "FFmpegUtil";

    /**
     * 从视频文件中分离出音频
     * @param videoPath  视频文件路径
     */
    public static void demuxer(String videoPath, OnAudioClipListener onAudioClipListener){
        File outFile = generateFilePath(FileType.MP3);
        //参数分别是视频路径，输出路径，输出类型
        EpEditor.demuxer(videoPath, outFile.getAbsolutePath(), EpEditor.Format.MP3, new OnEditorListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess");
                if (onAudioClipListener != null) {
                    onAudioClipListener.onClipSuccess(outFile);
                }
            }

            @Override
            public void onFailure() {
                Log.d(TAG, "onFailure");
                if (onAudioClipListener != null) {
                    onAudioClipListener.onClipFail();
                }
            }

            @Override
            public void onProgress(float progress) {
                Log.d(TAG, "onProgress() called with: progress = [" + progress + "]");
            }
        });

    }

    /**
     * 剪辑视频文件
     * @param videoPath
     * @param startTime 剪辑开始时间
     * @param duration 剪辑时长
     */
    public static void clipVideo(String videoPath, float startTime, float duration, OnVideoClipListener onVideoClipListener){

        EpVideo epVideo = new EpVideo(videoPath);
        //一个参数为剪辑的起始时间，第二个参数为持续时间,单位：秒
        epVideo.clip(startTime,duration);
        File outFile = generateFilePath(FileType.MP4);

        if (outFile == null){
            return;
        }

        EpEditor.exec(epVideo, new EpEditor.OutputOption(outFile.getAbsolutePath()), new OnEditorListener() {
            @Override
            public void onSuccess() {
                if (onVideoClipListener != null) {
                    onVideoClipListener.onClipSuccess(outFile);
                }

            }

            @Override
            public void onFailure() {
                if (onVideoClipListener != null) {
                    onVideoClipListener.onClipFail();
                }
            }

            @Override
            public void onProgress(float progress) {

            }
        });

    }

    public interface OnVideoClipListener{
        void onClipSuccess(File outFile);
        void onClipFail();
    }

    public interface OnAudioClipListener{
        void onClipSuccess(File outFile);
        void onClipFail();
    }

    private static File generateFilePath(FileType fileType){
        //视频保存路F径
        File outFile = null;
        try {
            outFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/CameraX" + new SimpleDateFormat(
                    FILENAME_FORMAT, Locale.CHINA
            ).format(System.currentTimeMillis()) + fileType.fileType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outFile;
    }

    enum FileType{
        MP4(".mp4"),
        MP3(".mp3"),
        AMR(".amr");

        String fileType;

        FileType(String type) {
            fileType = type;
        }
    }


    private static String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";

}
