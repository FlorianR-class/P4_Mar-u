package com.florianrib.meetingmanagement.ui.meeting_list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.florianrib.meetingmanagement.DI.DI;
import com.florianrib.meetingmanagement.R;
import com.florianrib.meetingmanagement.model.Meeting;
import com.florianrib.meetingmanagement.service.MeetingApiService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddMeetingActivity extends Dialog {

    @BindView(R.id.hour_button) Button mButtonHour;
    @BindView(R.id.date_button) Button mButtonDate;
    @BindView(R.id.location_button) Button mButtonLocation;
    @BindView(R.id.create_button) Button mCreateButton;
    @BindView(R.id.name_input) EditText mNameEditText;
    @BindView(R.id.add_email) ImageView mEmailAddButton;
    @BindView(R.id.participant_editText) EditText mEmailEditText;

    /** List to add Email */
    private List<String> mEmailList = new ArrayList<>();

    /** Calendar return Meeting */
    private final Calendar mCalendarMeeting = Calendar.getInstance();

    /** ApiService */
    private MeetingApiService mApiService;

    public AddMeetingActivity(Activity activity) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.activity_add_meeting);
        ButterKnife.bind(this);
        mApiService = DI.getMeetingApiService();


        /** Calendar de base, temps réel */
        Calendar calendarReal = Calendar.getInstance();
        final int hour = calendarReal.get(Calendar.HOUR_OF_DAY);
        final int minute = calendarReal.get(Calendar.MINUTE);
        final int year = calendarReal.get(Calendar.YEAR);
        final int month = calendarReal.get(Calendar.MONTH);
        final int day = calendarReal.get(Calendar.DAY_OF_MONTH);

        /** Hour Format */
        final DateFormat heureFormat = new SimpleDateFormat("HH'h'mm");
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        /** Init good text on Button */
        mButtonHour.setHint(heureFormat.format(calendarReal.getTime()));
        mButtonDate.setHint(dateFormat.format(calendarReal.getTime()));

        /** Action on button Hours */
        mButtonHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** TimePickerDialog */
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddMeetingActivity.this.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourChoice, int minuteChoice) {
                        mCalendarMeeting.set(Calendar.HOUR_OF_DAY, hourChoice);
                        mCalendarMeeting.set(Calendar.MINUTE, minuteChoice);
                        // Change button text hour
                        mButtonHour.setHint(heureFormat.format(mCalendarMeeting.getTime()));
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(AddMeetingActivity.this.getContext()));
                timePickerDialog.show();
            }
        });

        /** Action on button Date */
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** DatePickerDialog */
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddMeetingActivity.this.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfyear, int dayOfMonth) {
                        mCalendarMeeting.set(year, monthOfyear, dayOfMonth);
                        mButtonDate.setHint(dateFormat.format(mCalendarMeeting.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        /** Action on button Location Meeting */
        mButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"Réunion A", "Réunion B", "Réunion C", "Réunion D", "Réunion E", "Réunion F", "Réunion G", "Réunion H", "Réunion I", "Réunion J"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMeetingActivity.this.getContext());
                builder.setTitle("Choisissez votre salle")
                        .setItems(items, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mButtonLocation.setHint(items[which]);
                            }
                        });
                builder.show();
            }
        });

        /** Action if EditText Changed */
        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.i("TAG", "onTextChanged"+ mEmailEditText.getText());
                if (s.toString().length() == 0)
                    mEmailAddButton.setColorFilter(Color.WHITE);
                else {
                    mEmailAddButton.setColorFilter(Color.parseColor("#00A9F4"));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        /** Action on button Add Email */
        mEmailAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailCurrent = mEmailEditText.getText().toString();
                if (emailCurrent.contains("@") && emailCurrent.contains(".")) {
                    mEmailList.add(mEmailEditText.getText().toString());
                    mEmailEditText.getText().clear();
                    Toast toast = Toast.makeText(AddMeetingActivity.this.getContext(), "Participant ajouté !", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                } else {
                    mEmailEditText.setError("Adresse mail incorrecte !");
                }
            }
        });

        /** Action on button Save */
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            int mAvatar;
            @Override
            public void onClick(View v) {
                switch(mButtonLocation.getHint().toString()){
                    case "Réunion A": mAvatar = R.drawable.a_lens_24dp; break;
                    case "Réunion B": mAvatar = R.drawable.b_lens_24dp; break;
                    case "Réunion C": mAvatar = R.drawable.c_lens_24dp; break;
                    case "Réunion D": mAvatar = R.drawable.d_lens_24dp; break;
                    case "Réunion E": mAvatar = R.drawable.e_lens_24dp; break;
                    case "Réunion F": mAvatar = R.drawable.f_lens_24dp; break;
                    case "Réunion G": mAvatar = R.drawable.g_lens_24dp; break;
                    case "Réunion H": mAvatar = R.drawable.h_lens_24dp; break;
                    case "Réunion I": mAvatar = R.drawable.i_lens_24dp; break;
                    case "Réunion J": mAvatar = R.drawable.j_lens_24dp;break; }

                    if (TextUtils.isEmpty(mNameEditText.getText())){
                        mNameEditText.setError( "Veuillez saisir un sujet" );
                    } else if (mEmailList.size() == 0){
                        mEmailAddButton.setColorFilter(Color.RED);
                        mEmailEditText.setError( "Ajouter un participant !" );
                    } else {
                        Meeting meeting = new Meeting(mButtonHour.getHint().toString(), mButtonDate.getHint().toString(), mButtonLocation.getHint().toString(), mNameEditText.getText().toString() , mAvatar, mEmailList);
                        mApiService.addMeeting(meeting);
                        Toast.makeText(getContext(), "Réunion ajoutée !", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
            }
        });
    }
}
