package com.hkust.android.event.model;

/**
 * Created by hozdanny on 15/11/30.
 */
public class VoteRecord {
    private String id;
    private int voteCount;
    private String date;
    private boolean isVoted;

    public boolean isVoted() {
        return isVoted;
    }

    public void setIsVoted(boolean isVoted) {
        this.isVoted = isVoted;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
