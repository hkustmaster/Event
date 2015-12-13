package com.hkust.android.event.model;

/**
 * Created by hozdanny on 15/11/30.
 */
public class Message {
    private String _id;
    private String activity;
    private String content;
    private String createAt;
    private UserForMessage from;

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public UserForMessage getFrom() {
        return from;
    }

    public void setFrom(UserForMessage from) {
        this.from = from;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
