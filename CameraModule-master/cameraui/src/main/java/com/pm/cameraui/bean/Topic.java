package com.pm.cameraui.bean;


//@Entity
public class Topic {
    /**
     * {
     * 		"id": 1,
     * 		"revision": 0,
     * 		"name": "消费",
     * 		"orgId": "0001",
     * 		"code": "111",
     * 		"status": 0,
     * 		"createdBy": "小黑",
     * 		"createdTime": "2021-05-03 08:22:12",
     * 		"updatedBy": "小黑",
     * 		"updatedTime": "2021-05-03 08:22:39",
     * 		"comment": "我是你爸爸你知道吗"
     *        }
     */
//    @Id(autoincrement = true)
    private Long _id;
    private long id;
    private int revision;
    private String name;
    private String orgId;
    private String code;
    private int status;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private String updatedTime;
    private String comment;
//    @Generated(hash = 2057444312)
    public Topic(Long _id, long id, int revision, String name, String orgId,
                 String code, int status, String createdBy, String createdTime,
                 String updatedBy, String updatedTime, String comment) {
        this._id = _id;
        this.id = id;
        this.revision = revision;
        this.name = name;
        this.orgId = orgId;
        this.code = code;
        this.status = status;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
        this.comment = comment;
    }
//    @Generated(hash = 849012203)
    public Topic() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getRevision() {
        return this.revision;
    }
    public void setRevision(int revision) {
        this.revision = revision;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOrgId() {
        return this.orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getCreatedBy() {
        return this.createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public String getCreatedTime() {
        return this.createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
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
    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
