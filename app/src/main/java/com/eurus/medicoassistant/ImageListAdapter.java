package com.eurus.medicoassistant;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Kay on 11/18/2017.
 */

public class ImageListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Bitmap> prescriptions;
    private ImageView rxIV;
    private ImageView deleteRxIV;
    private ProgressBar noImageRxPB;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Appointment appointment;
    private String uid;
    private File[] images;


    public  ImageListAdapter(Context context)
    {
        this.context=context;
        prescriptions=new ArrayList<>();
        ContextWrapper cw=new ContextWrapper(context);
        File directory = cw.getDir("prescription", Context.MODE_PRIVATE);
        File path=new File(directory.getAbsolutePath()+File.separator+"date"+File.separator+"slot");
        images=path.listFiles();
        int i=0;
        for(File f: images)
        {
            if(i==0)
            {
                i=999;
            }
            else
            {
                prescriptions.add(BitmapFactory.decodeFile(f.getPath()));
            }

        }
    }
    public ImageListAdapter(Context context,  Appointment appointment) {
        this.context = context;
        this.appointment=appointment;

    }

    @Override
    public int getCount() {
        return prescriptions.size();
    }

    @Override
    public Object getItem(int i) {
        return prescriptions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_image_preivew, viewGroup, false);
        }
        rxIV=view.findViewById(R.id.rxIV);
        deleteRxIV=view.findViewById(R.id.deleteRxIV);

        deleteRxIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prescriptions.remove(i);
                notifyDataSetChanged();
            }
        });
        rxIV.setImageBitmap(prescriptions.get(i));
        return view;
    }
}
