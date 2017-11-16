package com.eurus.medicoassistant;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Kay on 11/16/2017.
 */

public class DoctorsListAdapter extends BaseAdapter{

    private ArrayList<Doctor> doctors;
    private Context context;

    private TextView doctorNameTV;
    private TextView qualificationsTV;
    private TextView specialityTV;
    private TextView emailTV;
    private de.hdodenhof.circleimageview.CircleImageView displayPicField;

    public DoctorsListAdapter(Context context, ArrayList<Doctor> doctorsList) {
            this.context=context;
            this.doctors=doctorsList;
    }

    @Override
    public int getCount() {
        return doctors.size();
    }


    @Override
    public Object getItem(int i) {
        return doctors.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null)
        {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_doctor_card, viewGroup, false);
        }
        doctorNameTV=view.findViewById(R.id.doctorTV);
        qualificationsTV=view.findViewById(R.id.qualificationsTV);

        specialityTV=view.findViewById(R.id.specialityTV);
        emailTV=view.findViewById(R.id.emailTV);
        displayPicField=view.findViewById(R.id.docProfileImageView);
        Doctor temp=doctors.get(i);
        doctorNameTV.setText("Dr. "+temp.getName());
        qualificationsTV.setText(temp.getQualifications());
        specialityTV.setText(temp.getSpeciality());
        emailTV.setText(temp.getEmail());
        Glide.with(context).load(temp.getImgurl()).into(displayPicField);


        return view;
    }
}
