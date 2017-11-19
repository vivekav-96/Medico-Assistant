package com.eurus.medicoassistant;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doctorsListView=getActivity().findViewById(R.id.doctorsListView);
        doctorDetails=new DoctorDetails();
        doctorsList=doctorDetails.getDoctorDetails();
        listAdapter=new DoctorsListAdapter(getActivity(), doctorsList);
        doctorsListView.setAdapter(listAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getActivity()).getTabLayout().setVisibility(View.GONE);
        return inflater.inflate(R.layout.activity_doctor_list, container, false);
    }
    /* @Override
    protected void onResume() {
        super.onResume();
        doctorDetails=new DoctorDetails();
        doctorsList=doctorDetails.getDoctorDetails();
        listAdapter=new DoctorsListAdapter(DoctorListFragment.this, doctorsList);
        doctorsListView.setAdapter(listAdapter);
    }*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //progressBar=findViewById(R.id.doctorsListProgressBar);
        // progressBar.setVisibility(View.VISIBLE);

        //progressBar.setVisibility(View.GONE);
    }
}
