package com.eurus.medicoassistant;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookAppointment extends AppCompatActivity implements RadioButton.OnClickListener{

    private DatabaseReference mRef;
    private CalendarView calendarView;
    private RadioButton morning,evening;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TimeSlotRVAdapter adapter;
    List<String> slotList = new ArrayList<>();
    String selected_date_str;
    ProgressBar progressBar;
    Button submit;
    TextView empty_text;
    String selected = "",uid;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appontment);
        sharedPreferences = getSharedPreferences(Utils.pref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        uid = sharedPreferences.getString("uid","");
        mRef = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Jennifer Wong");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarView = findViewById(R.id.calenderView);
        recyclerView = findViewById(R.id.recyclerView);
        morning = findViewById(R.id.morning_radiobtn);
        evening = findViewById(R.id.evening_radtobtn);
        progressBar = findViewById(R.id.progressbar);
        empty_text = findViewById(R.id.empty_text);
        submit = findViewById(R.id.submit_appointment_btn);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selected_date_str = dayOfMonth + "-" + (month+1) + "-" + year;
                loadInitalData();
            }
        });

        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new TimeSlotRVAdapter(slotList,selected);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selected = adapter.selected = slotList.get(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendarView.setDate(calendar.getTimeInMillis(),false,true);
        calendarView.setMinDate(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        selected_date_str = sdf.format(date);

        morning.setChecked(true);
        loadInitalData();
        int noOfDays = 14;
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        calendarView.setMaxDate(calendar.getTimeInMillis());
        morning.setOnClickListener(this);
        evening.setOnClickListener(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected.equals(""))
                    Toast.makeText(getApplicationContext(),"Select a time slot",Toast.LENGTH_LONG).show();
                else
                {
                    final Dialog dialog = new Dialog(BookAppointment.this);
                    dialog.setContentView(R.layout.dialog_submit_appointment);
                    final TextView name = dialog.findViewById(R.id.name_textview_dialog);
                    TextView line = dialog.findViewById(R.id.nextLine);
                    Button ok = dialog.findViewById(R.id.btn_ok_dialog);
                    Button cancel = dialog.findViewById(R.id.btn_cancel_dialog);
                    dialog.show();
                    final String doctor_name = toolbar.getTitle().toString();
                    name.setText(doctor_name);
                    final String batch = morning.isChecked()? "Morning" : "Evening" ;
                    line.setText(selected_date_str + " " + batch + " " + selected);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            final ProgressDialog progressDialog = new ProgressDialog(BookAppointment.this);
                            progressDialog.setTitle("Booking an Appointment");
                            progressDialog.setMessage("This will only take a sec..");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            Map<String,String> map = new HashMap<>();
                            map.put("Date",selected_date_str);
                            map.put("Time Slot",selected);
                            map.put("Doctor",doctor_name);
                            map.put("Time of Day",batch);
                            mRef.child("Appointments").child(uid).push().setValue(map , new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    mRef.child("Booked Slots").child(doctor_name).child(selected_date_str).child(batch).child(selected).setValue(true);
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Successfully Booked",Toast.LENGTH_LONG).show();
                                    finish();
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
            }
        });
    }


    private void loadSlots(DataSnapshot dataSnapshot) {
        slotList.clear();
        if(morning.isChecked())
            slotList.addAll(Utils.morningSlots);
        else
            slotList.addAll(Utils.eveningSlots);
        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
        {
            String slot = postSnapshot.getKey();
            if(postSnapshot.getValue(Boolean.class)) {
                slotList.remove(slot);
            }
        }
        progressBar.setVisibility(View.GONE);
        if(slotList.size()!=0) {
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
        }
        else
        {
            empty_text.setVisibility(View.VISIBLE);
        }
    }



    public void loadInitalData()
    {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        empty_text.setVisibility(View.GONE);
        String batch = morning.isChecked()? "Morning" : "Evening" ;
        Log.d("Date","Available Slots/Jennifer Wong/" + selected_date_str + batch);

        mRef.child("Booked Slots").child("Jennifer Wong").child(selected_date_str).child(batch).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DS",dataSnapshot.toString());
                loadSlots(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                empty_text.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Log.d("RBCLick",String.valueOf(v.getId()));
        switch (v.getId())
        {
            case R.id.morning_radiobtn:
                evening.setChecked(false);
                break;
            case R.id.evening_radtobtn:
                morning.setChecked(false);
                break;
        }
        loadInitalData();
    }
}
