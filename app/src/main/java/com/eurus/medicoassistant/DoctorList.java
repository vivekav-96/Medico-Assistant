package com.eurus.medicoassistant;

import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import java.util.ArrayList;

public class DoctorList extends AppCompatActivity {

    private ProgressBar progressBar;
    private ArrayList<Doctor> doctorsList;
    private ListAdapter listAdapter;
    private DoctorDetails doctorDetails;
    private ListView doctorsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //progressBar=findViewById(R.id.doctorsListProgressBar);
       // progressBar.setVisibility(View.VISIBLE);
        doctorsListView=findViewById(R.id.doctorsListView);
        doctorDetails=new DoctorDetails();
        doctorsList=doctorDetails.getDoctorDetails();
        listAdapter=new DoctorsListAdapter(DoctorList.this, doctorsList);
        doctorsListView.setAdapter(listAdapter);
        //progressBar.setVisibility(View.GONE);

    }
   /* @Override
    protected void onResume() {
        super.onResume();
        doctorDetails=new DoctorDetails();
        doctorsList=doctorDetails.getDoctorDetails();
        listAdapter=new DoctorsListAdapter(DoctorList.this, doctorsList);
        doctorsListView.setAdapter(listAdapter);
    }*/



}
