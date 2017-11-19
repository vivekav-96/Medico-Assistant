package com.eurus.medicoassistant;

import android.util.Log;

import com.eurus.medicoassistant.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Kay on 11/16/2017.
 */

public class DoctorDetails {
    private FirebaseDatabase firebase;
    private DatabaseReference ref;
    private ArrayList<Doctor> doctorDetails;


    public ArrayList<Doctor> getDoctorsList() {
        return doctorDetails;
    }


    DoctorDetails()
    {
        firebase=FirebaseDatabase.getInstance();
        ref=firebase.getReference("doctor");
        doctorDetails=new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren())
                {

                    Doctor temp=data.getValue(Doctor.class);
                    Log.v(">>>>>",temp.getName());
                    doctorDetails.add(temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
