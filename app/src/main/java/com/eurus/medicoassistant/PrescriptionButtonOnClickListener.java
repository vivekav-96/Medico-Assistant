package com.eurus.medicoassistant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.eurus.medicoassistant.Appointment;
import com.eurus.medicoassistant.PrescriptionList;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

/**
 * Created by Kay on 11/19/2017.
 */

public class PrescriptionButtonOnClickListener implements View.OnClickListener {


    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Context context;
    String uid;
    Appointment appointment;
    PrescriptionButtonOnClickListener(Appointment appointment, Context context)
    {
        this.appointment=appointment;
        this.context=context;
    }

    @Override
    public void onClick(View view) {
        ContextWrapper cw=new ContextWrapper(context);

        File directory = cw.getDir("prescription", Context.MODE_PRIVATE);

        File mypath=new File(directory.getAbsolutePath()+File.separator+appointment.getDoctor()+File.separator+appointment.getDate()+File.separator+appointment.getTimeSlot());
        if(!mypath.exists())
        {
            Dialog addRxDialog = new Dialog(context);
            View tempView = View.inflate(context, R.layout.layout_add_prescription_dialog, null);
            ImageView camIV = tempView.findViewById(R.id.camIV);
            ImageView fileIV = tempView.findViewById(R.id.fileIV);
            camIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                  //  ((Activity)context).startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            });
            addRxDialog.setContentView(tempView);
            addRxDialog.show();
        }

    }
}
