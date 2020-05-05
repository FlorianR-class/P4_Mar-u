package com.florianrib.meetingmanagement.DI;

import com.florianrib.meetingmanagement.service.DummyMeetingApiService;
import com.florianrib.meetingmanagement.service.MeetingApiService;

public class DI {

    private static MeetingApiService service = new DummyMeetingApiService();


    public static MeetingApiService getMeetingApiService() {
        return service;
    }

    public static MeetingApiService getNewInstanceMeetingApiService() {
         return new DummyMeetingApiService();
    }

}
