package com.eurus.medicoassistant;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private int remainingDays;
    private TextView text_remainingDaysTV;
    private TextView remainingDaysTV;
    private TextView text_daysTV;
    private TextView default_text;
    private ImageView new_appointment,appointment_history;
    private String uid;
    private Date current_date;
    private DatabaseReference mRef;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    SimpleDateFormat sdf;
    ProgressBar progressBar;
    private AppCompatActivity myContext;

    private void setDisplayValues(int difference)
    {
        default_text.setVisibility(View.GONE);
        String text_remainingDays="", remainingDays="", text_days="";

        switch (difference)
        {
            case 0: text_remainingDays="YOUR APPOINTMENT IS";
                remainingDays="TODAY";
                remainingDaysTV.setPadding(0,90,0,0);
                remainingDaysTV.setTextSize(70);
                break;
            case 1: text_remainingDays="YOUR APPOINTMENT IS";
                remainingDays="TOMMOROW";
                remainingDaysTV.setTextSize(50);
                remainingDaysTV.setPadding(0,90,0,0);
                break;
            default: text_remainingDays="NEXT APPOINTMENT IN";
                remainingDays=""+difference;
                remainingDaysTV.setTextSize(110);
                text_days="DAYS";

        }
        text_remainingDaysTV.setText(text_remainingDays);
        remainingDaysTV.setText(remainingDays);
        text_daysTV.setText(text_days);
        new_appointment.setOnClickListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(AppCompatActivity) activity;
        super.onAttach(activity);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utils.pref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        uid = sharedPreferences.getString("uid", "");
        mRef = FirebaseDatabase.getInstance().getReference();
        text_remainingDaysTV=(view.findViewById(R.id.text_remainingDaysTV));
        remainingDaysTV=view.findViewById(R.id.remaingDaysTV);
        default_text=view.findViewById(R.id.text_default);
        text_daysTV=view.findViewById(R.id.text_daysTV);
        progressBar = view.findViewById(R.id.progressbar);
        new_appointment = view.findViewById(R.id.new_appointment);


        current_date = new Date();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        new_appointment = view.findViewById(R.id.new_appointment);
        mRef.child("Appointments").child(uid).orderByChild("Date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    int difference = Integer.MAX_VALUE;
                    for(DataSnapshot postDataSnapshot : dataSnapshot.getChildren())
                    {
                        String date_str = postDataSnapshot.child("Date").getValue(String.class);
                        Date date = sdf.parse(date_str);
                        Log.d("Difference",difference+"");
                        if(date.after(current_date))
                        {
                            if(Utils.differenceInDaysBetweenDates(current_date,date)<difference)
                                difference = Utils.differenceInDaysBetweenDates(current_date,date);
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    if(difference!=Integer.MAX_VALUE)
                        setDisplayValues(difference+1);

                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.new_appointment:
                myContext.getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_frag_lay , new DoctorListFragment()).commit();
                break;

        }
    }
}
