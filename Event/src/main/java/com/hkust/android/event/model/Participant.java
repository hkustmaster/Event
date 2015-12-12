package com.hkust.android.event.model;

/**
 * Created by hozdanny on 15/11/30.
 */
public class Participant {
    private String eventId;
    private String _id;
    private String availdableAt;
    private User id;



    public void setId(User id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public User getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }


    public String getAvaildableAt() {
        return availdableAt;
    }

    public void setAvaildableAt(String availdableAt) {
        this.availdableAt = availdableAt;
    }
}
