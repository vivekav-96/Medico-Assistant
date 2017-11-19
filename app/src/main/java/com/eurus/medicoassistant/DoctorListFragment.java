package com.eurus.medicoassistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class DoctorListFragment extends Fragment {

    private ProgressBar progressBar;
    private ArrayList<Doctor> doctorsList;
    private ListAdapter listAdapter;
    private DoctorDetails doctorDetails;
    private ListView doctorsListView;

    @Override
    public void onCreate(@NonNull  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity)getActivity()).getTabLayout().setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_doctor_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doctorsListView= view.findViewById(R.id.doctorsListView);
        doctorDetails=new DoctorDetails();
        doctorsList=doctorDetails.getDoctorDetails();
        listAdapter=new DoctorsListAdapter(getActivity(), doctorsList);
        doctorsListView.setAdapter(listAdapter);
        //progressBar=findViewById(R.id.doctorsListProgressBar);
        // progressBar.setVisibility(View.VISIBLE);

        //progressBar.setVisibility(View.GONE);
    }
}
