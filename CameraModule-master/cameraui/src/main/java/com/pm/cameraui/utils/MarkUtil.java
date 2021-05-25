package com.pm.cameraui.utils;


import com.pm.cameraui.bean.InspectRecord;
import com.pm.cameraui.bean.Mark;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MarkUtil {

    public static Mark singleImageMark(InspectRecord inspectRecord, String imageLocalFile, String imageRemoteUrl, long recordTimeLong) {
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
        return mark;
    }

    public static Mark withAudioMark(InspectRecord inspectRecord, String imageLocalFile, String imageRemoteUrl, long audioStartTime, long audioEndTime, long startTimestamp, long endTimestamp) {
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

//    /**
//     * 从视频文件中提取音频并更新到Mark表，每次结束录制时后台进行
//     * @param record
//     */
//    public static void generateAudioForMarks(InspectRecord record, InspectVM inspectVM){
//
//        List<Mark> markList = DaoManager.getInstance().getSession().getMarkDao().queryBuilder().where(MarkDao.Properties.MarkedObjId.eq(record.getId())).list();
//        ExecutorService threadPool = Executors.newCachedThreadPool();
//
//        Runnable markTask = new Runnable() {
//            @Override
//            public void run() {
//                for(Mark mark :markList){
//                    //循环把为上传的标记做适配剪辑和上传
//                    if (StringUtils.isEmpty(mark.getVedioUrl())){
//                        float duration = mark.getEndTimeLong() - mark.getStartTimeLong();
//                        FFmpegUtil.clipVideo(record.getLocalVideoFilePath(),mark.getStartTimeLong(),duration,new FFmpegUtil.OnVideoClipListener(){
//
//                            @Override
//                            public void onClipSuccess(File outFile) {
//                                //上传剪辑的视频文件
//                                UploadUtil.upload(outFile.getAbsolutePath(),  null, (localPath, remoteUrl) -> {
//                                    mark.setVedioUrl(remoteUrl);
//                                    mark.setVedioLocalPath(outFile.getAbsolutePath());
//                                    DaoManager.getInstance().getSession().getMarkDao().update(mark);
//                                    inspectVM.updateMarkRecord(mark);
////                                    inspectVM.transferMarkMp4(record.getVideoUrl(),mark);
////
////                                    List<Mark> list = DaoManager.getInstance().getSession().getMarkDao().queryBuilder().where(MarkDao.Properties.MarkedObjId.eq(record.getId())).list();
////                                    if (list.size() == 0){
////                                        record.setHasUndoMarks(false);
////                                        DaoManager.getInstance().getSession().getInspectRecordDao().update(record);
////                                    }
//
//                                });
//                            }
//
//                            @Override
//                            public void onClipFail() {
//                                //忽略错误，下次继续上传
//                            }
//                        });
//                    }
//                }
//            }
//        };
//
//        threadPool.execute(markTask);
//    }

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
