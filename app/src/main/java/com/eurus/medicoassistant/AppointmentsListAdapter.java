package com.eurus.medicoassistant;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sachin on 19-11-2017.
 */

public class AppointmentsListAdapter extends Adapter<AppointmentsListAdapter.AppointmentHolder> {

    public static final int APPOINTMENT_UPCOMING = 0;
    public static final int APPOINTMENT_EXPIRED = 1;

    private int appointmentType;

    public AppointmentsListAdapter(int appointmentType) {
        this.appointmentType = appointmentType;
    }

    @Override
    public AppointmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == APPOINTMENT_UPCOMING)
            return new AppointmentHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_appointment_upcoming_card, parent, false));
        else if(viewType == APPOINTMENT_EXPIRED)
            return new AppointmentHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_appointment_card, parent, false));
        return null;
    }

    @Override
    public void onBindViewHolder(AppointmentHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        return appointmentType;
    }

    public class AppointmentHolder extends RecyclerView.ViewHolder {
        public AppointmentHolder(View itemView) {
            super(itemView);
        }
    }
}
