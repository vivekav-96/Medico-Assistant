package com.eurus.medicoassistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class DoctorListFragment extends Fragment {

    private ArrayList<Doctor> doctorsList;
    private DoctorsListAdapter listAdapter;
    private DoctorDetails doctorDetails;
    private RecyclerView doctorsListView;

    @Override
    public void onCreate(@NonNull  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getActivity()).getTabLayout().setVisibility(View.GONE);
        ((MainActivity)getActivity()).getFabBookApp().setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_doctor_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doctorsListView= view.findViewById(R.id.doctorsListView);
        doctorDetails=new DoctorDetails();
        doctorsList=doctorDetails.getDoctorsList();
        if(doctorsList.size()!=0){
            view.findViewById(R.id.no_docs_available_label).setVisibility(View.GONE);
            listAdapter=new DoctorsListAdapter(getActivity(), doctorsList);
            doctorsListView.setAdapter(listAdapter);
            doctorsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }
}
