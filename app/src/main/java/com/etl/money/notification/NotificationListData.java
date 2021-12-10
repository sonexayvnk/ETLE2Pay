package com.etl.money.notification;


public class NotificationListData {
    private String strIds, strTitle, StrDescription, strDatetime, strStatus;

    public String getIds() {
        return strIds;
    }
    public void setIds(String strIds) {
        this.strIds = strIds;
    }

    public String getTitle() { return strTitle; }
    public void setTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getDescription() {
        return StrDescription;
    }
    public void setDescription(String StrDescription) { this.StrDescription = StrDescription; }

    public String getDatetime() {
        return strDatetime;
    }
    public void setDatetime(String strDatetime) {
        this.strDatetime = strDatetime;
    }

    public String getStatus() {
        return strStatus;
    }
    public void setStatus(String strStatus) {
        this.strStatus = strStatus;
    }}
