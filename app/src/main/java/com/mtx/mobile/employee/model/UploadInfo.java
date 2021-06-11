package com.mtx.mobile.employee.model;

/**
 * Created by DieHard_04 on 23-05-2021.
 */
public class UploadInfo {

    private String name;
    private String url;
    private String dateTime;

    public  UploadInfo() {
    }

    public UploadInfo(String name, String url, String dateTime) {
        this.name = name;
        this.url = url;
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
