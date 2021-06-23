package com.mtx.mobile.employee.model;

/**
 * Created by DieHard_04 on 23-05-2021.
 */
public class UploadInfo {

    private String name;
    private String url;
    private String dateTime;

    private String eventName;
    private String eventTime;
    private String eventDescription;

    private String eventVenue;
    public  UploadInfo() {
    }

    public UploadInfo(String name, String url, String dateTime) {
        this.name = name;
        this.url = url;
        this.dateTime = dateTime;
    }

    public UploadInfo(String name, String url, String dateTime, String eventName, String eventTime, String eventDescription) {
        this.name = name;
        this.url = url;
        this.dateTime = dateTime;
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.eventDescription = eventDescription;
    }

    public UploadInfo(String name, String url, String dateTime, String eventName, String eventTime, String eventVenue ,String eventDescription) {
        this.name = name;
        this.url = url;
        this.dateTime = dateTime;
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.eventVenue = eventVenue;
        this.eventDescription = eventDescription;
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }
}
