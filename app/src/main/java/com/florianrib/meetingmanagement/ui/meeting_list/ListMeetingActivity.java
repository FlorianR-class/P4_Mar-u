package com.florianrib.meetingmanagement.ui.meeting_list;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.florianrib.meetingmanagement.DI.DI;
import com.florianrib.meetingmanagement.R;
import com.florianrib.meetingmanagement.service.MeetingApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMeetingActivity extends AppCompatActivity {

    @BindView(R.id.list_meeting) public RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) public Toolbar mToolbar;
    @BindView(R.id.add_meeting) public FloatingActionButton fab;

    private MeetingApiService mApiService;
    private MyMeetingRecyclerViewAdapter mAdapter;
    private ListMeetingActivity mMeetingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiService = DI.getMeetingApiService();
        ButterKnife.bind(this);
        this.mMeetingActivity = this;
        setSupportActionBar(mToolbar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        initRecyclerView();


        /** Action on button Add */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMeetingActivity addMeetingActivity = new AddMeetingActivity(mMeetingActivity);
                addMeetingActivity.show();
                addMeetingActivity.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        initRecyclerView();
                    }
                });

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*** Filters Menu */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.i(TAG, String.valueOf(item.getItemId()));
//        Log.i(TAG, (String) item.getTitle());
        // aussi avec .contain()
        if (item.getTitle().toString().indexOf("Réunion")>-1){
            filterRoom(item.getTitle().toString());
            Toast.makeText(getApplicationContext(), "Filtre "+item.getTitle().toString(), Toast.LENGTH_SHORT).show();
        } else if (R.id.filter_date == item.getItemId()){
            goCalendar();
        } else if (R.id.delete_filter == item.getItemId()){
            resetFilters();
            Toast.makeText(getApplicationContext(), "Reset des filtres", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /*** Init RecyclerView */
    public void initRecyclerView() {
        mAdapter = new MyMeetingRecyclerViewAdapter(mApiService.getMeetings());
        mRecyclerView.setAdapter(mAdapter);
    }

    /*** Action if Landscape to Portrait and vice versa */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /*** POC */
        mApiService.clearAllMeetingsList();
        resetFilters();
    }

    /*** Reset filter */
    public void resetFilters() {
        mApiService.resetFilter();
        initRecyclerView();
    }

    /*** Filter by Room */
    public void filterRoom(String room) {
        mApiService.resetFilter();
        mApiService.filterByRoom(room);
        initRecyclerView();
    }

    /*** Filter by Date */
    public void filterDate(String date) {
        mApiService.resetFilter();
        mApiService.filterByDate(date);
        initRecyclerView();
    }

    /*** Access to calendar */
    public void goCalendar() {
        Calendar baseCalendar = Calendar.getInstance();
        final int year = baseCalendar.get(Calendar.YEAR);
        final int month = baseCalendar.get(Calendar.MONTH);
        final int day = baseCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ListMeetingActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String dateChosen = dateFormat.format(cal.getTime());
                ListMeetingActivity.this.filterDate(dateChosen);
                Toast.makeText(getApplicationContext(), "Filtre "+dateChosen, Toast.LENGTH_SHORT).show();
            }
        },
                year,
                month,
                day);
        datePickerDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        /*** POC */
        mApiService.clearAllMeetingsList();
        resetFilters();
    }
}
