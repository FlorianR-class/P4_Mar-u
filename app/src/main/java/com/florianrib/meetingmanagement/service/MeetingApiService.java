package com.florianrib.meetingmanagement.service;

import com.florianrib.meetingmanagement.model.Meeting;

import java.util.List;

public interface MeetingApiService {

    List<Meeting> getMeetings();

    void clearAllMeetingsList();

    void addMeeting(Meeting meeting);

    void deleteMeeting(Meeting meeting);

    void filterByRoom(String room);

    void filterByDate(String date);

    void resetFilter();


}
