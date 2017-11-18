package com.eurus.medicoassistant;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PrescriptionList extends AppCompatActivity {

    private ArrayList<Bitmap> prescriptions;
    private ListAdapter listAdapter;
    private FirebaseDatabase firebase;
    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get data
        listview=findViewById(R.id.prescriptionLV);
        listAdapter=new ImageListAdapter(PrescriptionList.this);
        listview.setAdapter(listAdapter);

        //
    }

}
