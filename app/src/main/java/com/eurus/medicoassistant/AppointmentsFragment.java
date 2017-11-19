package com.eurus.medicoassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sachin on 18-11-2017.
 */

public class AppointmentsFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager pager;
    TabPagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointments, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pager = view.findViewById(R.id.appointment_pager);

        tabLayout = ((MainActivity)getActivity()).getTabLayout();
        tabLayout.setVisibility(View.VISIBLE);

        adapter = new TabPagerAdapter(this.getChildFragmentManager());
        adapter.addFragment(new UpcomingAppointmentFragment(), getString(R.string.upcoming));
        adapter.addFragment(new HistoryAppointmentFragment(), getString(R.string.history));
        pager.setAdapter(adapter);
        //tabLayout.addTab(tabLayout.newTab().setText(R.string.upcoming));
        //tabLayout.addTab(tabLayout.newTab().setText(R.string.history));
        tabLayout.setupWithViewPager(pager);
    }
}
