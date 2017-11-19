package com.eurus.medicoassistant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sachin on 18-11-2017.
 */

public class HistoryAppointmentFragment extends Fragment {
    RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DatabaseReference mRef;
    private List<Appointment> appointments = new ArrayList<>();
    AppointmentsListAdapter adapter;
    private String uid;
    ProgressBar progressBar;
    private Date current_datetime;
    SimpleDateFormat hour_minute_format;
    TextView empty_appointments;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Utils.pref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        uid = sharedPreferences.getString("uid", "");
        mRef = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.appointments_list);
        progressBar = view.findViewById(R.id.progressbar);
        empty_appointments = view.findViewById(R.id.empty_appointments_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AppointmentsListAdapter(appointments,AppointmentsListAdapter.APPOINTMENT_EXPIRED,getActivity(),uid);
        recyclerView.setAdapter(adapter);
        current_datetime = new Date();
        hour_minute_format = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        loadAppointments();
    }

    private void loadAppointments() {
        mRef.child("Appointments").child(uid).orderByChild("Date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Log.d("DS",dataSnapshot.toString());
                    String date_str = postSnapshot.child("Date").getValue(String.class);
                    String timeSlot = postSnapshot.child("Time Slot").getValue(String.class);
                    String date_time_str = date_str + " " + timeSlot;
                    try
                    {
                        Date date_time = hour_minute_format.parse(date_time_str);
                        if(date_time.before(current_datetime))
                        {
                            Appointment appointment = new Appointment();
                            String doctor = postSnapshot.child("Doctor").getValue(String.class);
                            appointment.setDate(date_str);
                            appointment.setDoctor(doctor);
                            appointment.setTimeSlot(timeSlot);
                            appointment.setDayOfWeek(Utils.getDayofWeek(date_time));
                            appointments.add(appointment);
                        }
                    }
                    catch (ParseException e)
                    {
                        Log.d("ParseException",e.getMessage());
                    }

                }
                progressBar.setVisibility(View.GONE);
                if(appointments.size()!=0)
                {
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    empty_appointments.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
