package com.florianrib.meetingmanagement;

import com.florianrib.meetingmanagement.DI.DI;
import com.florianrib.meetingmanagement.model.Meeting;
import com.florianrib.meetingmanagement.service.MeetingApiService;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MeetingServiceTest {

    private MeetingApiService service;

    private List<String> emails = new ArrayList<>();

    @Before
    public void setup() {
        service = DI.getNewInstanceMeetingApiService();
    }

    @Test
    public void getMeetingsWithSuccess() {
        assertTrue(service.getMeetings().isEmpty());
        Meeting meeting1 = new Meeting("14h00", "05/04/2020", "Réunion A","Peach", R.drawable.a_lens_24dp, emails);
        Meeting meeting2 = new Meeting("16h00", "08/04/2020", "Réunion B","Mario", R.drawable.b_lens_24dp, emails);
        Meeting meeting3 = new Meeting("19h00", "07/05/2020", "Réunion C","Luigi", R.drawable.c_lens_24dp, emails);
        service.addMeeting(meeting1);
        service.addMeeting(meeting2);
        service.addMeeting(meeting3);
        assertEquals(3, service.getMeetings().size());
        assertTrue(service.getMeetings().contains(meeting1));
        assertTrue(service.getMeetings().contains(meeting2));
        assertTrue(service.getMeetings().contains(meeting3));
    }

    @Test
    public void deleteMeetingWithSuccess() {
        assertTrue(service.getMeetings().isEmpty());
        Meeting meeting1 = new Meeting("19h00", "07/05/2020", "Réunion C","Luigi", R.drawable.c_lens_24dp, emails);
        service.addMeeting(meeting1);
        assertEquals(1, service.getMeetings().size());
        assertTrue(service.getMeetings().contains(meeting1));
        service.deleteMeeting(meeting1);
        assertTrue(service.getMeetings().isEmpty());
    }

    @Test
    public void addMeetingWithSuccess(){
        assertTrue(service.getMeetings().isEmpty());
        Meeting meeting1 = new Meeting("14h00","14/04/2020","Réunion B","Peach",R.drawable.c_lens_24dp, emails);
        assertFalse(service.getMeetings().contains(meeting1));
        service.addMeeting(meeting1);
        assertTrue(service.getMeetings().contains(meeting1));
        assertEquals(1, service.getMeetings().size());
    }

    @Test
    public void filterRoomMeetingsWithSuccess(){
        assertTrue(service.getMeetings().isEmpty());
        Meeting meeting1 = new Meeting("14h00","14/04/2020","Réunion B","Peach",R.drawable.c_lens_24dp, emails);
        Meeting meeting2 = new Meeting("19h00", "07/05/2020", "Réunion C","Luigi", R.drawable.e_lens_24dp, emails);
        service.addMeeting(meeting1);
        service.addMeeting(meeting2);
        assertTrue(service.getMeetings().contains(meeting1));
        assertTrue(service.getMeetings().contains(meeting2));
        assertEquals(2, service.getMeetings().size());
        service.filterByRoom("Réunion B");
        assertTrue(service.getMeetings().contains(meeting1));
        assertFalse(service.getMeetings().contains(meeting2));
        assertEquals(1, service.getMeetings().size());
        Meeting meetingFilter = service.getMeetings().get(0);
        assertEquals(meetingFilter.getmLocation(),"Réunion B");
    }

    @Test
    public void filterDateMeetingsWithSuccess(){
        assertTrue(service.getMeetings().isEmpty());
        Meeting meeting1 = new Meeting("14h00","14/04/2020","Réunion B","Peach",R.drawable.c_lens_24dp, emails);
        Meeting meeting2 = new Meeting("19h00", "07/05/2020", "Réunion C","Luigi", R.drawable.e_lens_24dp, emails);
        service.addMeeting(meeting1);
        service.addMeeting(meeting2);
        assertTrue(service.getMeetings().contains(meeting1));
        assertTrue(service.getMeetings().contains(meeting2));
        assertEquals(2, service.getMeetings().size());
        service.filterByDate("07/05/2020");
        assertFalse(service.getMeetings().contains(meeting1));
        assertTrue(service.getMeetings().contains(meeting2));
        assertEquals(1, service.getMeetings().size());
        Meeting meetingFiltered = service.getMeetings().get(0);
        assertEquals(meetingFiltered.getmDate(),"07/05/2020");
    }

    @Test
    public void resetFilterWithSucess() {
        assertTrue(service.getMeetings().isEmpty());
        Meeting meeting1 = new Meeting("14h00","14/04/2020","Réunion C","Peach",R.drawable.c_lens_24dp, emails);
        Meeting meeting2 = new Meeting("16h00", "08/04/2020", "Réunion B","Mario", R.drawable.b_lens_24dp, emails);
        service.addMeeting(meeting1);
        service.addMeeting(meeting2);
        assertTrue(service.getMeetings().contains(meeting1));
        assertTrue(service.getMeetings().contains(meeting2));
        assertEquals(2, service.getMeetings().size());
        service.filterByDate("08/04/2020");
        assertFalse(service.getMeetings().contains(meeting1));
        assertTrue(service.getMeetings().contains(meeting2));
        assertEquals(1, service.getMeetings().size());
        Meeting meetingFiltered = service.getMeetings().get(0);
        assertEquals(meetingFiltered.getmDate(),"08/04/2020");
        service.resetFilter();
        assertEquals(2, service.getMeetings().size());
        assertTrue(service.getMeetings().contains(meeting1));
        assertTrue(service.getMeetings().contains(meeting2));
    }
}