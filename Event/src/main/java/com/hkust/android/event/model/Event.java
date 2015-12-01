package com.hkust.android.event.model;

import java.util.ArrayList;

/**
 * Created by hozdanny on 15/11/30.
 */
public class Event {
    private String id;
    private User eventHolder;
    private String status;
    private String time;
    private ArrayList<String> tbdTime;
    private ArrayList<Participant> participants;
    private String location;
    private String description;

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getEventHolder() {
        return eventHolder;
    }

    public void setEventHolder(User eventHolder) {
        this.eventHolder = eventHolder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getTbdTime() {
        return tbdTime;
    }

    public void setTbdTime(ArrayList<String> tbdTime) {
        this.tbdTime = tbdTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}