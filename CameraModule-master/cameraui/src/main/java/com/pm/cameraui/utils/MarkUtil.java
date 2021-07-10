package com.pm.cameraui.utils;


import android.util.Log;

import com.pm.cameraui.bean.InspectRecord;
import com.pm.cameraui.bean.Mark;
import com.pm.cameraui.mvp.VideoPresenter;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MarkUtil {

    public static Mark singleImageMark(InspectRecord inspectRecord, String imageLocalFile, String imageRemoteUrl, long recordTimeLong,String longitude,String latitude) {
        if (imageLocalFile == null || inspectRecord == null) return null;
        Mark mark = new Mark();
        mark.setMarkType(0);//1--图片标记
        mark.setDuration(0L);//持续时间为0
        long timeStamp = System.currentTimeMillis();
        mark.setStartTimeLong(recordTimeLong/1000);
        mark.setEndTimeLong(recordTimeLong/1000);//开始结束时间为同一个
        mark.setEndTime(TimeUtil.getFormatDateTime(timeStamp));
        mark.setStartTime(TimeUtil.getFormatDateTime(timeStamp));
        mark.setMarkedObjId(inspectRecord.getId());
        mark.setPictureLocalPath(imageLocalFile);
        mark.setTagPicUrl(imageRemoteUrl);
        mark.setLongitude(longitude);
        mark.setLatitude(latitude);
        return mark;
    }

    public static Mark withAudioMark(InspectRecord inspectRecord, String imageLocalFile, String imageRemoteUrl, long audioStartTime, long audioEndTime, long startTimestamp, long endTimestamp,String longitude,String latitude) {
        if (imageLocalFile == null || inspectRecord == null) return null;
        Mark mark = new Mark();
        mark.setMarkType(1);//1--图片+音频标记
        mark.setMarkedObjId(inspectRecord.getId());
        mark.setPictureLocalPath(imageLocalFile);
        mark.setTagPicUrl(imageRemoteUrl);
        mark.setStartTime(TimeUtil.getFormatDateTime(startTimestamp));
        mark.setEndTime(TimeUtil.getFormatDateTime(endTimestamp));
        mark.setStartTimeLong(audioStartTime);
        mark.setEndTimeLong(audioEndTime);
        mark.setDuration(audioEndTime - audioStartTime);
        mark.setLongitude(longitude);
        mark.setLatitude(latitude);
        return mark;
    }

//    public static void upDateMark(Mark mark){
//        if (mark != null){
//            DaoManager.getInstance().getSession().getMarkDao().update(mark);
//        }
//    }
//    public static void insertMark(Mark mark){
//        if (mark != null){
//            DaoManager.getInstance().getSession().getMarkDao().insert(mark);
//        }
//    }

    /**
     * 从视频文件中提取音频并更新到Mark表，每次结束录制时后台进行
     * @param record
     */
    public static int markUploadCount = 0;
    public static void generateAudioForMarks(InspectRecord record, List<Mark> markList, VideoPresenter presenter,boolean isBackground){

        resetUploadCount();

        new Thread(new Runnable() {
            @Override
            public void run() {
//              List<Mark> markList = DaoManager.getInstance().getSession().getMarkDao().queryBuilder().where(MarkDao.Properties.MarkedObjId.eq(record.getId())).list();
                ExecutorService threadPool = Executors.newCachedThreadPool();
                for(Mark mark :markList){
                    //循环把为上传的标记做适配剪辑和上传
                    if (!mark.getStartTimeLong().equals(mark.getEndTimeLong()) && StringUtils.isEmpty(mark.getVedioUrl())){
                        Log.e("RAMBO","markid="+mark.getId()+"开始处理");
                        Runnable markTask = new Runnable() {
                            @Override
                            public void run() {
                                addUploadCount();
                                float duration = mark.getEndTimeLong() - mark.getStartTimeLong();
                                FFmpegUtil.clipVideo(record.getLocalVideoFilePath(),mark.getStartTimeLong(),duration,new FFmpegUtil.OnVideoClipListener(){
                                    @Override
                                    public void onClipSuccess(File outFile) {
                                        //上传剪辑的视频文件
                                        Log.e("RAMBO","markid="+mark.getId()+"文件剪辑成功");
                                        UploadUtil.upload(outFile.getAbsolutePath(),  null, (localPath, remoteUrl) -> {
                                            mark.setVedioUrl(remoteUrl);
                                            Log.e("RAMBO","markid="+mark.getId()+"文件上传成功");
                                            mark.setVedioLocalPath(outFile.getAbsolutePath());
                                            mark.setMarkType(1);
//                                            DaoManager.getInstance().getSession().getMarkDao().update(mark);
//                                            inspectVM.updateMarkRecord(mark);
                                            delUploadCount();
                                            if(isBackground){
                                                presenter.updateMarkRecord2(mark);
                                            }else {
                                                presenter.updateMarkRecord(mark);
                                            }
                                        });
                                    }
                                    @Override
                                    public void onClipFail() {
                                        delUploadCount();
                                        //忽略错误，下次继续上传
                                    }
                                });
                            }
                        };
                        threadPool.execute(markTask);
                        try {
                            if(isFinishUpload()){
                                if(isBackground){
                                    presenter.updateMarkRecord2(null);
                                }else {
                                    presenter.updateMarkRecord(null);
                                }
                            }
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //没有需要分割上传的VoiceMark，直接就Finish。
                if(isFinishUpload()){
                    if(isBackground){
                        presenter.updateMarkRecord2(null);
                    }else {
                        presenter.updateMarkRecord(null);
                    }
                }

                try{
                    Thread.sleep(10*1000);
                    cleanLocalVideoFile(record);
                }catch (Exception e){

                }
            }
        }).start();
    }

    public static void cleanLocalVideoFile(InspectRecord record){
        //大的总视频文件上传成功了！,删除记录条
        try{
            if(record!=null){
                FileUtils.deleteFile(record.getLocalVideoFilePath());
                Log.d("RAMBO"," cleanLocalVideoFile:"+record.getLocalVideoFilePath());
            }
        }catch (Exception e){
                Log.d("RAMBO"," cleanLocalVideoFile 失败！");
        }
    }

    public static void delUploadCount(){
        if(markUploadCount>0){
            markUploadCount-=1;
        }
        Log.d("RAMBO","delUploadCount markUploadCount = " + markUploadCount);
    }
    public static void addUploadCount(){
        markUploadCount+=1;
        Log.d("RAMBO","addUploadCount markUploadCount = " + markUploadCount);
    }
    public static void resetUploadCount(){
        markUploadCount = 0;
        Log.d("RAMBO","resetUploadCount markUploadCount = " + markUploadCount);
    }
    public static boolean isFinishUpload(){

        return markUploadCount == 0;
    }

//    /**
//     * 每次启动扫描码数据库里面有没有为上传完成的标记
//     */
//    public static void checkMarksStatus(InspectVM inspectVM){
//        List<InspectRecord> recordListList = DaoManager.getInstance().getSession().getInspectRecordDao().queryBuilder().where(InspectRecordDao.Properties.HasUndoMarks.eq(true)).list();
//        for (InspectRecord record : recordListList) {
//            generateAudioForMarks(record,inspectVM);
//        }
//    }

}
