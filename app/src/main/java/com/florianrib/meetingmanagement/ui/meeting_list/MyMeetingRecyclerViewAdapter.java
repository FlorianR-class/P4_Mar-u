package com.florianrib.meetingmanagement.ui.meeting_list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.florianrib.meetingmanagement.DI.DI;
import com.florianrib.meetingmanagement.R;
import com.florianrib.meetingmanagement.model.Meeting;
import com.florianrib.meetingmanagement.service.MeetingApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyMeetingRecyclerViewAdapter extends RecyclerView.Adapter<MyMeetingRecyclerViewAdapter.ViewHolder> {

    private List<Meeting> mMeetings;
    private MeetingApiService mApiService;
    private Context mContext;

    public MyMeetingRecyclerViewAdapter(List<Meeting> mMeetings) {
        this.mMeetings = mMeetings;
    }

    @Override
    public MyMeetingRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_meeting, parent, false);
        mContext = parent.getContext();
        return new MyMeetingRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyMeetingRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.displayMeeting(mMeetings.get(position));
        mApiService = DI.getMeetingApiService();

        /** Action on button Delete */
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(mContext.getString(R.string.confirm_delete));
                builder.setCancelable(false);
                builder.setPositiveButton(mContext.getString(R.string.oui), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mApiService.deleteMeeting(mMeetings.get(position));
                        notifyDataSetChanged();
                        dialog.dismiss();
                        Toast.makeText(mContext, mContext.getString(R.string.toast_delete), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton(mContext.getString(R.string.non), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_list_mail)        public TextView mMail;
        @BindView(R.id.item_list_meeting)        public TextView mReunion;
        @BindView(R.id.item_list_avatar)        public ImageView mAvatar;
        @BindView(R.id.item_list_delete_button) public ImageButton mDeleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /** Meeting Display */
        void displayMeeting (Meeting meeting){
            //mReunion.setText(meeting.getmLocation()+" - "+meeting.getmHour()+" - "+meeting.getmDate()+" - "+meeting.getmTopic());
            mReunion.setText(meeting.getmLocation()+" - "+meeting.getmHour()+" - "+meeting.getmTopic());
            mMail.setText(meeting.getmParticipants().toString().replace("[", " ").replace("]", " "));
            Glide.with(mAvatar.getContext())
                    .load(meeting.getmAvatar())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mAvatar);
        }


    }
}
