package com.eurus.medicoassistant;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sachin on 19-11-2017.
 */

public class AppointmentsListAdapter extends Adapter<RecyclerView.ViewHolder> {

    public static final int APPOINTMENT_UPCOMING = 0;
    public static final int APPOINTMENT_EXPIRED = 1;
    List<Appointment> appointments;
    private int appointmentType;
    Activity activity;
    String uid;
    DatabaseReference mRef;

    public AppointmentsListAdapter(List<Appointment> appointments,int appointmentType , Activity activity, String uid) {
        this.appointments = appointments;
        this.appointmentType = appointmentType;
        this.activity = activity;
        this.uid = uid;
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == APPOINTMENT_UPCOMING)
            return new UpComingHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_appointment_upcoming_card, parent, false));
        else if(viewType == APPOINTMENT_EXPIRED)
            return new HistoryHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_appointment_card, parent, false));
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof UpComingHolder)
        {
            Appointment appointment = appointments.get(position);
            UpComingHolder upComingHolder = (UpComingHolder) holder;
            upComingHolder.doctorTV.setText(appointment.getDoctor());
            upComingHolder.dateTV.setText(appointment.getDate());
            upComingHolder.timeTV.setText(appointment.getTimeSlot());
            upComingHolder.remainingTV.setText(appointment.getRemaining());
        }
        else if(holder instanceof HistoryHolder)
        {
            Appointment appointment = appointments.get(position);
            HistoryHolder historyHolder = (HistoryHolder) holder;
            historyHolder.doctorTV.setText(appointment.getDoctor());
            historyHolder.dateTV.setText(appointment.getDate());
            historyHolder.timeTV.setText(appointment.getTimeSlot());
            historyHolder.dayOfWeekTV.setText(appointment.getDayOfWeek());
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    @Override
    public int getItemViewType(int position) {
        return appointmentType;
    }

    public class UpComingHolder extends RecyclerView.ViewHolder {

        TextView doctorTV,dateTV,timeTV,remainingTV;
        Button cancel;
        public UpComingHolder(View itemView) {
            super(itemView);
            doctorTV = itemView.findViewById(R.id.doctorTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            remainingTV = itemView.findViewById(R.id.remainingTV);
            cancel = itemView.findViewById(R.id.cancelAppointmentButtion);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //createDialog();
                }
            });
        }
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {
        TextView doctorTV,dateTV,timeTV,dayOfWeekTV;
        Button addPrescriptionButton,bookFollowUpButton;
        public HistoryHolder(final View itemView) {
            super(itemView);
            doctorTV = itemView.findViewById(R.id.doctorTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            dayOfWeekTV = itemView.findViewById(R.id.dayOfWeekTV);
            addPrescriptionButton = itemView.findViewById(R.id.addPrescriptionButton);
            bookFollowUpButton = itemView.findViewById(R.id.bookFollowUpButton);

            addPrescriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,Temp.class);
                    intent.putExtra("Name",appointments.get(getLayoutPosition()).getDoctor());
                    activity.startActivity(intent);
                }
            });
            bookFollowUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,BookAppointment.class);
                    intent.putExtra("Name",appointments.get(getLayoutPosition()).getDoctor());
                    activity.startActivity(intent);
                }
            });
        }
    }

    /*
    private void createDialog(final Appointment appointment) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_submit_appointment);
        final TextView name = dialog.findViewById(R.id.name_textview_dialog);
        TextView line = dialog.findViewById(R.id.nextLine);
        Button ok = dialog.findViewById(R.id.btn_ok_dialog);
        Button cancel = dialog.findViewById(R.id.btn_cancel_dialog);
        dialog.show();
        name.setText(appointment.getDoctor());
        line.setText(appointment.getDate() + " " + appointment.getTimeOfDay());
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ProgressDialog mProgress = new ProgressDialog(activity);
                mProgress.setTitle("Booking an Appointment");
                mProgress.setMessage("This will only take a sec..");
                mProgress.setCancelable(false);
                mProgress.show();
                mRef.child("Appointments").child(uid).orderByChild("Date").equalTo(appointment.getDate()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postDataSnapshot : dataSnapshot.getChildren())
                        {
                            if(postDataSnapshot.)
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    */
}
