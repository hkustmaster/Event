package com.hkust.android.event.model;

/**
 * Created by hozdanny on 15/11/30.
 */
public class VoteRecord {
    private int voteCount = 0;
    private String date;
    private boolean isVoted=false;

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

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
