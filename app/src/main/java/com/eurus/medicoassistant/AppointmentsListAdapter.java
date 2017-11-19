package com.eurus.medicoassistant;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sachin on 19-11-2017.
 */

public class AppointmentsListAdapter extends Adapter<RecyclerView.ViewHolder> {

    public static final int APPOINTMENT_UPCOMING = 0;
    public static final int APPOINTMENT_EXPIRED = 1;
    List<Appointment> appointments;
    private int appointmentType;

    public AppointmentsListAdapter(List<Appointment> appointments,int appointmentType) {
        this.appointments = appointments;
        this.appointmentType = appointmentType;
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
        }
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {
        TextView doctorTV,dateTV,timeTV,dayOfWeekTV;
        Button addPrescriptionButton,bookFollowUpButton;
        public HistoryHolder(View itemView) {
            super(itemView);
            doctorTV = itemView.findViewById(R.id.doctorTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            dayOfWeekTV = itemView.findViewById(R.id.dayOfWeekTV);
            addPrescriptionButton = itemView.findViewById(R.id.addPrescriptionButton);
            bookFollowUpButton = itemView.findViewById(R.id.bookFollowUpButton);
        }
    }
}
