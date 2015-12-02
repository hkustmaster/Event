package com.hkust.android.event.model;

import java.util.ArrayList;

/**
 * Created by hozdanny on 15/11/30.
 */
public class Event {
    private String id;
    private String title;
    private User host;
    private String status;
    private String time;
    private String beginAt;
    private String endAt;
    private String location;
    private String description;
    private ArrayList<Participant> participants;
    private int quota;
    private int maxNumOfParticipant;

    public String getBeginAt() {
        return beginAt;
    }

    public void setBeginAt(String beginAt) {
        this.beginAt = beginAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
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

    public int getMaxNumOfParticipant() {
        return maxNumOfParticipant;
    }

    public void setMaxNumOfParticipant(int maxNumOfParticipant) {
        this.maxNumOfParticipant = maxNumOfParticipant;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}