package com.florianrib.meetingmanagement.service;

import com.florianrib.meetingmanagement.model.Meeting;

import java.util.ArrayList;
import java.util.List;

public class DummyMeetingApiService implements MeetingApiService {

    private List<Meeting> meetings = new ArrayList<>();
    private List<Meeting> meetingsNoFilter = meetings;
    private String roomFilter;
    private String dateFilter;

    @Override
    public List<Meeting> getMeetings() {
        return meetings;
    }

    @Override
    public void clearAllMeetingsList() {
        meetings.clear();
        meetingsNoFilter.clear();
    }

    @Override
    public void addMeeting(Meeting meeting) {
        if (roomFilter != null && meeting.getmLocation().trim().equals(roomFilter.trim())) {
            meetings.add(meeting);
        }
        if (dateFilter != null && meeting.getmDate().trim().equals(dateFilter.trim())) {
            meetings.add(meeting);
        }
        meetingsNoFilter.add(meeting);
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
        meetingsNoFilter.remove(meeting);
    }

    @Override
    public void resetFilter() {
        roomFilter = null;
        dateFilter = null;
        meetings = meetingsNoFilter;
    }

    public void filterByRoom(String room) {
        meetings = new ArrayList<>();
        roomFilter = room;
        for (Meeting meet : meetingsNoFilter) {
            if (meet.getmLocation().trim().equals(room.trim())) {
                meetings.add(meet);
            }
        }
    }

    public void filterByDate(String date) {
        meetings = new ArrayList<>();
        dateFilter = date;
        for (Meeting meet : meetingsNoFilter) {
            if (meet.getmDate().trim().equals(date.trim())) {
                meetings.add(meet);
            }
        }
    }
}
