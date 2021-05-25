package com.pm.cameraui.api;

import com.pm.cameraui.bean.AppConfig;
import com.pm.cameraui.bean.InspectRecord;
import com.pm.cameraui.bean.Mark;
import com.pm.cameraui.bean.Topic;
import com.pm.cameraui.bean.UserInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 作者： ch
 * 时间： 2016/12/27.13:56
 * 描述：
 * 来源：
 */

public interface ApiServer {



    /**
     * 用户登录
     */
    @POST("worksiterecord-record-srv/inspectionUser/loginIn")
    Observable<UserInfo> login(@Query("account") String account, @Query("password") String password);

    /**
     * 获取工作内容列表
     */
    @GET("worksiterecord-record-srv/inspectionTopic/getTopicList")
    Observable<List<Topic>> getInspectionTopic();

    /**
     * 获取配置信息
     */
    @GET("worksiterecord-record-srv/inspectionConfig/getAppConfig")
    Observable<AppConfig> getAppConfiguration();


    /**
     * 新增工作记录
     */
    @POST("worksiterecord-record-srv/inspectionRecord/insert")
    Observable<InspectRecord> addInspectRecord(@Body InspectRecord inspectRecord);


    /**
     *更新工作记录
     *视频录制完成对话框后，更新结束时间数据后做Update操作
     */
    @POST("worksiterecord-record-srv/inspectionRecord/update")
    Observable<InspectRecord> updateInspectRecord(@Body InspectRecord inspectRecord);

    /**
     * 新增标记
     */
    @POST("worksiterecord-mark-srv/inspectionMark/insert")
    Observable<Mark> addMarkRecord(@Body Mark mark);

    /**
     * 更新标记
     * 视频录制完成后对话框后，从视频中截取录音mark的视频，发送出去当mark
     */
    @POST("worksiterecord-mark-srv/inspectionMark/update")
    Observable<Mark> updateMarkRecord(@Body Mark mark);

    /**
     * 记录列表
     */
    @GET("mobile/inspectionRecord/selectAll")
    Observable<List<InspectRecord>> getInspectionList();

}
