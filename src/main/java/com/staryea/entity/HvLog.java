package com.staryea.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Auther: wucf
 * @Date: 2018/6/27 10:36
 * @Description:华为观影日志对象：对应mongodb数据库中的collection为HvLog
 */
@Document(collection = "HvLog")
public class HvLog {
    private String id;
    private String UserID;//用户标志
    private String ContentID;//内容的唯一标识
    private String ServiceType;//服务类型
    private String BeginTime;//起始时间（ 格式YYYYMMDDHHMMSS）本地时间，不带时区
    private String EndTime;//结束时间（ 格式YYYYMMDDHHMMSS）本地时间，不带时区

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getContentID() {
        return ContentID;
    }

    public void setContentID(String contentID) {
        ContentID = contentID;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    @Override
    public String toString() {
        return "HvLog{" +
                "id='" + id + '\'' +
                ", UserID='" + UserID + '\'' +
                ", ContentID='" + ContentID + '\'' +
                ", ServiceType='" + ServiceType + '\'' +
                ", BeginTime='" + BeginTime + '\'' +
                ", EndTime='" + EndTime + '\'' +
                '}';
    }
}
