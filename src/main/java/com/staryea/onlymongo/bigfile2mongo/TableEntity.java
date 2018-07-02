package com.staryea.onlymongo.bigfile2mongo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "table_info")
public class TableEntity {
    private String userId;
    @Field
    private String contentId;
    @Field
    private String serviceType;
    @Field
    private String beginTime;
    @Field
    private String endTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    //       document.put("userId",array[0]);
//                document.put("contentId",array[1]);
//                document.put("serviceType",array[2]);
//                document.put("beginTime",array[3]);
//                document.put("endTime",array[4]);
}
