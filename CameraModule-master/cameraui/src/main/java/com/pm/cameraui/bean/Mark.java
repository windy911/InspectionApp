package com.pm.cameraui.bean;

//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Generated;
//import org.greenrobot.greendao.annotation.Id;

//@Entity
public class Mark {
    /**
     *  "audioText": "",
     * 	"audioUrl": "",
     * 	"createBy": 0,
     * 	"createDate": "",
     * 	"duration": 0,
     * 	"endTime": "",
     * 	"endTimeLong": 0,
     * 	"id": 0,
     * 	"markedObjId": 0,
     * 	"name": "",
     * 	"pictureLocalPath": "",
     * 	"rank": 0,
     * 	"startTime": "",
     * 	"startTimeLong": 0,
     * 	"tagPicUrl": "",
     * 	"updateBy": 0,
     * 	"updateDate": ""
     */

    private String audioText;
    private String audioUrl;
    private Long createBy;
    private String createDate;
    private Long duration;
    private String endTime;
    private Long endTimeLong;
//    @Id
    private Long id;
    private Long markedObjId;
    private String name;
    private String pictureLocalPath;
    private Integer rank;
    private String startTime;
    private Long startTimeLong;
    private String tagPicUrl;
    private Long updateBy;
    private String updateDate;
    private String vedioLocalPath;
    private String vedioUrl;

    private String longitude;//经度
    private String latitude;//纬度

    /**
     * 标记类型。0--单图片标记，1--图片+音频标记
     */
    private int markType;
//    @Generated(hash = 901054254)
    public Mark(String audioText, String audioUrl, Long createBy, String createDate,
                Long duration, String endTime, Long endTimeLong, Long id,
                Long markedObjId, String name, String pictureLocalPath, Integer rank,
                String startTime, Long startTimeLong, String tagPicUrl, Long updateBy,
                String updateDate, String vedioLocalPath, String vedioUrl,
                int markType,String longitude,String latitude) {
        this.audioText = audioText;
        this.audioUrl = audioUrl;
        this.createBy = createBy;
        this.createDate = createDate;
        this.duration = duration;
        this.endTime = endTime;
        this.endTimeLong = endTimeLong;
        this.id = id;
        this.markedObjId = markedObjId;
        this.name = name;
        this.pictureLocalPath = pictureLocalPath;
        this.rank = rank;
        this.startTime = startTime;
        this.startTimeLong = startTimeLong;
        this.tagPicUrl = tagPicUrl;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
        this.vedioLocalPath = vedioLocalPath;
        this.vedioUrl = vedioUrl;
        this.markType = markType;
        this.longitude = longitude;
        this.latitude = latitude;
    }
//    @Generated(hash = 1181118646)
    public Mark() {
    }
    public String getAudioText() {
        return this.audioText;
    }
    public void setAudioText(String audioText) {
        this.audioText = audioText;
    }
    public String getAudioUrl() {
        return this.audioUrl;
    }
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    public Long getCreateBy() {
        return this.createBy;
    }
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
    public String getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public Long getDuration() {
        return this.duration;
    }
    public void setDuration(Long duration) {
        this.duration = duration;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public Long getEndTimeLong() {
        return this.endTimeLong;
    }
    public void setEndTimeLong(Long endTimeLong) {
        this.endTimeLong = endTimeLong;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getMarkedObjId() {
        return this.markedObjId;
    }
    public void setMarkedObjId(Long markedObjId) {
        this.markedObjId = markedObjId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPictureLocalPath() {
        return this.pictureLocalPath;
    }
    public void setPictureLocalPath(String pictureLocalPath) {
        this.pictureLocalPath = pictureLocalPath;
    }
    public Integer getRank() {
        return this.rank;
    }
    public void setRank(Integer rank) {
        this.rank = rank;
    }
    public String getStartTime() {
        return this.startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public Long getStartTimeLong() {
        return this.startTimeLong;
    }
    public void setStartTimeLong(Long startTimeLong) {
        this.startTimeLong = startTimeLong;
    }
    public String getTagPicUrl() {
        return this.tagPicUrl;
    }
    public void setTagPicUrl(String tagPicUrl) {
        this.tagPicUrl = tagPicUrl;
    }
    public Long getUpdateBy() {
        return this.updateBy;
    }
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }
    public String getUpdateDate() {
        return this.updateDate;
    }
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
    public String getVedioLocalPath() {
        return this.vedioLocalPath;
    }
    public void setVedioLocalPath(String vedioLocalPath) {
        this.vedioLocalPath = vedioLocalPath;
    }
    public String getVedioUrl() {
        return this.vedioUrl;
    }
    public void setVedioUrl(String vedioUrl) {
        this.vedioUrl = vedioUrl;
    }
    public int getMarkType() {
        return this.markType;
    }
    public void setMarkType(int markType) {
        this.markType = markType;
    }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }
    public String getLatitude() {  return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }

    @Override
    public String toString() {
        return "Mark{" +
                "audioText='" + audioText + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                ", createBy=" + createBy +
                ", createDate='" + createDate + '\'' +
                ", duration=" + duration +
                ", endTime='" + endTime + '\'' +
                ", endTimeLong=" + endTimeLong +
                ", id=" + id +
                ", markedObjId=" + markedObjId +
                ", name='" + name + '\'' +
                ", pictureLocalPath='" + pictureLocalPath + '\'' +
                ", rank=" + rank +
                ", startTime='" + startTime + '\'' +
                ", startTimeLong=" + startTimeLong +
                ", tagPicUrl='" + tagPicUrl + '\'' +
                ", updateBy=" + updateBy +
                ", updateDate='" + updateDate + '\'' +
                ", vedioLocalPath='" + vedioLocalPath + '\'' +
                ", vedioUrl='" + vedioUrl + '\'' +
                ", markType=" + markType +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
