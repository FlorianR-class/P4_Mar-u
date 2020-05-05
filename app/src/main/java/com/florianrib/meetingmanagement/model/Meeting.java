package com.florianrib.meetingmanagement.model;

import java.util.List;

public class Meeting {

    private String mHour;
    private String mDate;
    private String mLocation;
    private String mTopic;
    private int mAvatar;
    private List<String> mParticipants;

    public Meeting(String mHour, String mDate, String mLocation, String mTopic, int mAvatar, List<String> mParticipants) {
        this.mHour = mHour;
        this.mDate = mDate;
        this.mLocation = mLocation;
        this.mTopic = mTopic;
        this.mAvatar = mAvatar;
        this.mParticipants = mParticipants;
    }


    public String getmHour() { return mHour; }

    public void setmHour(String mHour) {this.mHour = mHour;}

    public String getmDate() {return mDate;}

    public void setmDate(String mDate) {this.mDate = mDate;}

    public String getmLocation() {return mLocation;}

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getmTopic() {
        return mTopic;
    }

    public void setmTopic(String mTopic) {
        this.mTopic = mTopic;
    }

    public List<String> getmParticipants() {
        return mParticipants;
    }

    public void setmParticipants(List<String> mParticipants) {
        this.mParticipants = mParticipants;
    }

    public int getmAvatar() {return mAvatar;}

    public void setmAvatar(int mAvatar) {this.mAvatar = mAvatar;}
}
