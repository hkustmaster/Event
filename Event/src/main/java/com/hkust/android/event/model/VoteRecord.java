package com.hkust.android.event.model;

/**
 * Created by hozdanny on 15/11/30.
 */
public class VoteRecord {
    private String id;
    private String eventId;
    private String result;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
