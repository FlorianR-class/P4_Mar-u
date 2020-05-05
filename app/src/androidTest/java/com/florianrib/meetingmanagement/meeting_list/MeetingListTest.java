package com.florianrib.meetingmanagement.meeting_list;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.florianrib.meetingmanagement.R;
import com.florianrib.meetingmanagement.ui.meeting_list.ListMeetingActivity;
import com.florianrib.meetingmanagement.utils.DeleteViewAction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.contrib.PickerActions.setTime;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.florianrib.meetingmanagement.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MeetingListTest {

    private ListMeetingActivity mActivity;

    @Rule
    public ActivityTestRule<ListMeetingActivity> mActivityRule =
            new ActivityTestRule(ListMeetingActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myMeetingsList_deleteAction_shouldRemoveItem() {
        onView(withId(R.id.list_meeting)).check(withItemCount(0));
        newMeeting(2021,8,5, "Réunion A", "Peach", 14 ,00);
        onView(withId(R.id.list_meeting)).check(withItemCount(1));
        onView(allOf(withId(R.id.list_meeting), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        onView(withText("OUI")).perform(click());
        onView(withId(R.id.list_meeting)).check(withItemCount(0));
    }

    @Test
    public void myMeetingList_filteredByLocation_shouldShowOnlyOneItem() {
        onView(withId(R.id.list_meeting)).check(withItemCount(0));
        newMeeting(2020,04,8, "Réunion B", "Mario", 16, 00);
        newMeeting(2020,04,5, "Réunion A", "Peach", 14 ,00);
        onView(withId(R.id.list_meeting)).check(withItemCount(2));
        onView(Matchers.allOf(withContentDescription("More options"), childAtPosition
                        (childAtPosition(withId(R.id.toolbar),
                                1), 0),
                isDisplayed())).perform(click());
        onView(Matchers.allOf(withId(R.id.title), withText("Filtrer par Lieu"), childAtPosition(
                childAtPosition(withId(R.id.content),
                        0), 0),
                isDisplayed())).perform(click());

        onView(Matchers.allOf(withId(R.id.title), withText("Réunion A"),
                childAtPosition(childAtPosition(withId(R.id.content),
                        0), 0),
                isDisplayed())).perform(click());

        onView(Matchers.allOf(withId(R.id.item_list_meeting), withText("Réunion A - 14h00 - Peach"),
                childAtPosition(Matchers.allOf(withId(R.id.constraint),
                        childAtPosition(withId(R.id.list_meeting),
                                0)), 1),
                isDisplayed())).check(matches(withText("Réunion A - 14h00 - Peach")));
        onView(Matchers.allOf(withId(R.id.list_meeting), isDisplayed())).check(withItemCount(1));

    }

    @Test
    public void myMeetingList_resetFilter_shouldShowTwoItem() {
        onView(withId(R.id.list_meeting)).check(withItemCount(0));
        newMeeting(2022,02,20, "Réunion F", "Mario", 18, 59);
        newMeeting(2020,04,5, "Réunion A", "Peach", 14 ,00);
        onView(withId(R.id.list_meeting)).check(withItemCount(2));
        filterByDates(2022, 02, 20);
        onView(withId(R.id.list_meeting)).check(withItemCount(1));
        onView(Matchers.allOf(withId(R.id.item_list_meeting), withText("Réunion F - 18h59 - Mario"),
                childAtPosition(Matchers.allOf(withId(R.id.constraint),
                        childAtPosition(withId(R.id.list_meeting),
                                0)), 1),
                isDisplayed())).check(matches(withText("Réunion F - 18h59 - Mario")));

        onView(Matchers.allOf(withContentDescription("More options"), childAtPosition
                        (childAtPosition(withId(R.id.toolbar),
                                1), 0),
                isDisplayed())).perform(click());
        onView(Matchers.allOf(withId(R.id.title), withText("Reset du filtre"), childAtPosition(
                childAtPosition(withId(R.id.content),
                        0), 0),
                isDisplayed())).perform(click());
        onView(Matchers.allOf(withId(R.id.list_meeting), isDisplayed())).check(withItemCount(2));
    }
    @Test
    public void myMeetingList_filteredByDate_shouldShowOnlyOneItem(){
        onView(withId(R.id.list_meeting)).check(withItemCount(0));
        newMeeting(2020,04,8, "Réunion B", "Mario", 16, 00);
        newMeeting(2020,05,9, "Réunion C", "Luigi", 18 ,50);
        onView(withId(R.id.list_meeting)).check(withItemCount(2));
        filterByDates(2020, 04, 8);
        onView(withId(R.id.list_meeting)).check(withItemCount(1));
        onView(Matchers.allOf(withId(R.id.item_list_meeting), withText("Réunion B - 16h00 - Mario"),
                childAtPosition(Matchers.allOf(withId(R.id.constraint),
                        childAtPosition(withId(R.id.list_meeting),
                                0)), 1),
                isDisplayed())).check(matches(withText("Réunion B - 16h00 - Mario")));
    }

    @Test
    public void myMeetingsList_addAction_shouldAddItem(){
        onView(withId(R.id.list_meeting)).check(withItemCount(0));
        onView(withId(R.id.add_meeting)).perform(click());
        onView(ViewMatchers.withId(R.id.name_input)).perform(replaceText("Luigi"), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.participant_editText)).perform(replaceText("ursul@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.add_email)).perform(click());
        onView(withId(R.id.date_button)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 06, 06));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.date_button)).check(matches(Matchers.allOf(withHint(String.format("%02d/%02d", 06, 06) + '/' + 2021),
                isDisplayed())));
        onView(withId(R.id.hour_button)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(setTime( 16 , 00));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.hour_button)).check(matches(Matchers.allOf(withHint(String.format("%02dh%02d", 16, 00)),
                isDisplayed())));
        onView(withId(R.id.create_button)).perform(click());
        onView(withId(R.id.list_meeting))
                .perform(RecyclerViewActions.scrollToPosition((1)));
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        onView(Matchers.allOf(withId(R.id.item_list_mail), withText(" ursul@gmail.com "),
                childAtPosition(Matchers.allOf(withId(R.id.constraint),
                        childAtPosition(withId(R.id.list_meeting),
                                0)), 2),
                isDisplayed())).check(matches(withText(" ursul@gmail.com ")));
        onView(withId(R.id.list_meeting)).check(withItemCount(1));
    }

    private void newMeeting(int year, int month, int day, String room, String name, int hour, int minute){
        onView(withId(R.id.add_meeting)).perform(click());
        onView(ViewMatchers.withId(R.id.location_button)).perform(click());
        onView(withText(room)).perform(click());
        onView(ViewMatchers.withId(R.id.name_input)).perform(replaceText(name), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.participant_editText)).perform(replaceText("proga@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.add_email)).perform(click());
        onView(withId(R.id.date_button)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.hour_button)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(setTime(hour, minute));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.create_button)).perform(click());
    }

    private void filterByDates(int year, int month, int day){
        onView(Matchers.allOf(withContentDescription("More options"), childAtPosition
                        (childAtPosition(withId(R.id.toolbar),
                                1), 0),
                isDisplayed())).perform(click());
        onView(Matchers.allOf(withId(R.id.title), withText("Filtrer par Date"), childAtPosition(
                childAtPosition(withId(R.id.content),
                        0), 0),
                isDisplayed())).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(setDate(year, month, day));
        onView(withText("OK")).perform(click());
    }

    public static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
