package com.pm.cameraui.bean;

//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Generated;
//import org.greenrobot.greendao.annotation.Id;

//@Entity
public class InspectRecord {
//    @Id(autoincrement = true)
    private Long _id;
    /**
     * "createBy": 0,
     * 	"createDate": "",
     * 	"duration": 0,
     * 	"endTime": "",
     * 	"endTimeLong": 0,
     * 	"id": 0,
     * 	"labels": 0,
     * 	"name": "",
     * 	"ownerCompnay": "",
     * 	"recordStatus": 0,
     * 	"startTime": "",
     * 	"startTimeLong": 0,
     * 	"topicId": 0,
     * 	"traceLocus": "",
     * 	"updateBy": 0,
     * 	"updateDate": "",
     * 	"uploadStatus": 0
     *
     */
    private Long id;
    private String comment;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private String name;
    private Long topicId;
    private Integer labels;
    private String traceLocus;
    private String ownerCompnay;
    /**
     * 0--未上传
     * 1--已上传完成
     * 2--上传未完成
     */
    private int uploadStatus = 1;
    private int recordStatus;
    private Long startTimeLong;
    private String startTime;
    private Long endTimeLong;
    private String endTime;
    private String duration;




    private int revision;
    private String updatedBy;
    private String updatedTime;
    private String localVideoFilePath;
    private String videoUrl;
    /**
     * 是否还有音频为补全的标记
     */
    private boolean hasUndoMarks;
//    @Generated(hash = 211708177)
    public InspectRecord(Long _id, Long id, String comment, String createBy,
                         String createDate, String updateBy, String updateDate, String name,
                         Long topicId, Integer labels, String traceLocus, String ownerCompnay,
                         int uploadStatus, int recordStatus, Long startTimeLong,
                         String startTime, Long endTimeLong, String endTime, String duration,
                         int revision, String updatedBy, String updatedTime,
                         String localVideoFilePath, String videoUrl, boolean hasUndoMarks) {
        this._id = _id;
        this.id = id;
        this.comment = comment;
        this.createBy = createBy;
        this.createDate = createDate;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
        this.name = name;
        this.topicId = topicId;
        this.labels = labels;
        this.traceLocus = traceLocus;
        this.ownerCompnay = ownerCompnay;
        this.uploadStatus = uploadStatus;
        this.recordStatus = recordStatus;
        this.startTimeLong = startTimeLong;
        this.startTime = startTime;
        this.endTimeLong = endTimeLong;
        this.endTime = endTime;
        this.duration = duration;
        this.revision = revision;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
        this.localVideoFilePath = localVideoFilePath;
        this.videoUrl = videoUrl;
        this.hasUndoMarks = hasUndoMarks;
    }
//    @Generated(hash = 1789280741)
    public InspectRecord() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getCreateBy() {
        return this.createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return this.updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public String getUpdateDate() {
        return this.updateDate;
    }
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getTopicId() {
        return this.topicId;
    }
    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
    public Integer getLabels() {
        return this.labels;
    }
    public void setLabels(Integer labels) {
        this.labels = labels;
    }
    public String getTraceLocus() {
        return this.traceLocus;
    }
    public void setTraceLocus(String traceLocus) {
        this.traceLocus = traceLocus;
    }
    public String getOwnerCompnay() {
        return this.ownerCompnay;
    }
    public void setOwnerCompnay(String ownerCompnay) {
        this.ownerCompnay = ownerCompnay;
    }
    public int getUploadStatus() {
        return this.uploadStatus;
    }
    public void setUploadStatus(int uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
    public int getRecordStatus() {
        return this.recordStatus;
    }
    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }
    public Long getStartTimeLong() {
        return this.startTimeLong;
    }
    public void setStartTimeLong(Long startTimeLong) {
        this.startTimeLong = startTimeLong;
    }
    public String getStartTime() {
        return this.startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public Long getEndTimeLong() {
        return this.endTimeLong;
    }
    public void setEndTimeLong(Long endTimeLong) {
        this.endTimeLong = endTimeLong;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getDuration() {
        return this.duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public int getRevision() {
        return this.revision;
    }
    public void setRevision(int revision) {
        this.revision = revision;
    }
    public String getUpdatedBy() {
        return this.updatedBy;
    }
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    public String getUpdatedTime() {
        return this.updatedTime;
    }
    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
    public String getLocalVideoFilePath() {
        return this.localVideoFilePath;
    }
    public void setLocalVideoFilePath(String localVideoFilePath) {
        this.localVideoFilePath = localVideoFilePath;
    }
    public String getVideoUrl() {
        return this.videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    public boolean getHasUndoMarks() {
        return this.hasUndoMarks;
    }
    public void setHasUndoMarks(boolean hasUndoMarks) {
        this.hasUndoMarks = hasUndoMarks;
    }

    
}
