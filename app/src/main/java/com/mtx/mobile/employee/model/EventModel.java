package com.mtx.mobile.employee.model;

/**
 * Created by DieHard_04 on 23-04-2021.
 */
public class EventModel {
    private String eventName;

    public EventModel(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
