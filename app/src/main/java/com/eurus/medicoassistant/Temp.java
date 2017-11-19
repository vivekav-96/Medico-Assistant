package com.eurus.medicoassistant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Temp extends AppCompatActivity {


    private static final int CAMERA_REQUEST = 1888;
    private FirebaseDatabase firebase;
    private DatabaseReference ref;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String uid;
    private Button btn;
    private Button viewBtn;
    private Appointment appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

      /*  Dialog addRxDialog = new Dialog(Temp.this);
        View tempView = View.inflate(Temp.this, R.layout.layout_add_prescription_dialog, null);
        ImageView camIV = tempView.findViewById(R.id.camIV);
        ImageView fileIV = tempView.findViewById(R.id.fileIV);
        camIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        addRxDialog.setContentView(tempView);
        addRxDialog.show();*/

      btn=findViewById(R.id.tempBtn);
      viewBtn=findViewById(R.id.viewRxBtn);
        uid=getSharedPreferences(Utils.pref, MODE_PRIVATE).getString("uid","");

        firebase=FirebaseDatabase.getInstance();
        ref=firebase.getReference("Appointments").child(uid);

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    appointment=new Appointment();
                    appointment.setDate(data.child("Date").getValue(String.class));
                    appointment.setDoctor(data.child("Doctor").getValue(String.class));
                    appointment.setTimeOfDay(data.child("Time of Day").getValue(String.class));
                    appointment.setTimeSlot(data.child("Time Slot").getValue(String.class));



                   Log.v("date: ",appointment.getDate());
                    Log.v("doctor: ",appointment.getDoctor());
                    Log.v("Time of Day: ",appointment.getTimeOfDay());
                    Log.v("Time slot: ",appointment.getTimeSlot()+"\n");


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog addRxDialog = new Dialog(Temp.this);
                View tempView = View.inflate(Temp.this, R.layout.layout_add_prescription_dialog, null);
                ImageView camIV = tempView.findViewById(R.id.camIV);
                ImageView fileIV = tempView.findViewById(R.id.fileIV);
                camIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                });
                addRxDialog.setContentView(tempView);
                addRxDialog.show();

            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Temp.this, PrescriptionList.class);
                startActivity(i);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            String sha=""+byteArray.hashCode();
            //storageReference=firebaseStorage.getReference().child(uid+"/"+appointment.getDoctor()+"/"+appointment.getDate()+"/"+appointment.getTimeSlot()+"/"+sha);
           // storageReference.putBytes(byteArray);

            //Store in phone memory
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("prescription", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath=new File(directory.getAbsolutePath()+File.separator+"date"+File.separator+"slot");
            mypath.mkdirs();
            File rxDir=new File(mypath, photo.hashCode()+".jpg");



            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(rxDir);
                // Use the compress method on the BitMap object to write image to the OutputStream
                photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(Temp.this, "Added to "+mypath.getAbsolutePath()+" ", Toast.LENGTH_SHORT).show();
        }
    }
}
