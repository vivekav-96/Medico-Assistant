package com.eurus.medicoassistant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kay on 11/16/2017.
 */

public class DoctorsListAdapter extends RecyclerView.Adapter<DoctorsListAdapter.DoctorViewHolder>{

    private ArrayList<Doctor> doctors;
    private Context context;

    DoctorsListAdapter(Context context, ArrayList<Doctor> doctorsList) {
        this.context=context;
        this.doctors=doctorsList;
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DoctorViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_doctor_card, parent, false));
    }

    @Override
    public void onBindViewHolder(DoctorViewHolder holder, int position) {
        holder.setDoctorDetails(doctors.get(position));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    class DoctorViewHolder extends RecyclerView.ViewHolder {
        private TextView doctorNameTV;
        private TextView qualificationsTV;
        private TextView specialityTV;
        private TextView emailTV;
        private CircleImageView displayPic;

        private DoctorViewHolder(View view) {
            super(view);
            doctorNameTV=view.findViewById(R.id.doctorTV);
            qualificationsTV=view.findViewById(R.id.qualificationsTV);
            specialityTV=view.findViewById(R.id.specialityTV);
            emailTV=view.findViewById(R.id.emailTV);
            displayPic=view.findViewById(R.id.docProfileImageView);
        }

        void setDoctorDetails(Doctor doctor) {
            doctorNameTV.setText(doctor.getName());
            qualificationsTV.setText(doctor.getQualifications());
            specialityTV.setText(doctor.getSpeciality());
            emailTV.setText(doctor.getEmail());
            Glide.with(context).load(doctor.getImgurl()).into(displayPic);
        }
    }
}
