package com.hkust.android.event.model;

/**
 * Created by hozdanny on 15/12/5.
 */
public class ParticipantsForAllEvent {
    private String availdableAt;
    private String id;
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAvaildableAt() {
        return availdableAt;
    }

    public void setAvaildableAt(String availdableAt) {
        this.availdableAt = availdableAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
