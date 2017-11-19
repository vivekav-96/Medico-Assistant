package com.eurus.medicoassistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorListFragment extends Fragment {

    private ArrayList<Doctor> doctorsList = new ArrayList<>();
    private DoctorsListAdapter listAdapter;
    private RecyclerView doctorsListView;
    private DatabaseReference ref;
    private TextView textview;
    ProgressBar progressbar;
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
        textview = view.findViewById(R.id.no_docs_available_label);
        progressbar = view.findViewById(R.id.progressbar);
        ref=FirebaseDatabase.getInstance().getReference();
        ref.child("doctor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren())
                {
                    Doctor temp=data.getValue(Doctor.class);
                    Log.v(">>>>>",temp.getName());
                    doctorsList.add(temp);
                }
                progressbar.setVisibility(View.GONE);
                if(doctorsList.size()!=0){
                    textview.setVisibility(View.GONE);
                    listAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listAdapter=new DoctorsListAdapter(getActivity(), doctorsList);
        doctorsListView.setAdapter(listAdapter);
        doctorsListView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}
