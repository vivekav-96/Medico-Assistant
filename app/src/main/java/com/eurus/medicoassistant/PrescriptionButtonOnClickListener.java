package com.eurus.medicoassistant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eurus.medicoassistant.Appointment;
import com.eurus.medicoassistant.PrescriptionList;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Kay on 11/19/2017.
 */

public class PrescriptionButtonOnClickListener implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1888;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Context context;
    String uid;
    Appointment appointment;
    private TextView tempTV;
    PrescriptionButtonOnClickListener(Appointment appointment, Context context)
    {
        this.appointment=appointment;
        this.context=context;
    }

    @Override
    public void onClick(View view) {

        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("prescription", Context.MODE_PRIVATE);
        this.tempTV=(TextView)view;
        final File mypath=new File(directory.getAbsolutePath()+File.separator+appointment.getDoctor()+File.separator+appointment.getDate()+File.separator+appointment.getTimeSlot());
        if(!mypath.exists())
        {
            mypath.mkdirs();
            Log.v(">>>","Created directory: "+mypath.getAbsolutePath());
           /*   Dialog addRxDialog = new Dialog(context);
            View tempView = View.inflate(context, R.layout.layout_add_prescription_dialog, null);
          ImageView camIV = tempView.findViewById(R.id.camIV);
            ImageView fileIV = tempView.findViewById(R.id.fileIV);*/

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    ((Activity)context).startActivityForResult(cameraIntent, CAMERA_REQUEST);




                }
            });

           /* addRxDialog.setContentView(tempView);
            addRxDialog.show();*/
        }
        else
        {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i= new Intent(context, PrescriptionList.class);
                    ((Activity)context).startActivity(i);
                }
            });

        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();

            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("prescription", Context.MODE_PRIVATE);

            final File mypath=new File(directory.getAbsolutePath()+File.separator+appointment.getDoctor()+File.separator+appointment.getDate()+File.separator+appointment.getTimeSlot());
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
            tempTV.setText("VIEW/ADD PRESCRIPTION");

            Toast.makeText(context, "Prescription added successfully", Toast.LENGTH_LONG).show();

        }

    }

    }
