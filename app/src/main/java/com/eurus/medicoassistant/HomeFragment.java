package com.eurus.medicoassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sachin on 18-11-2017.
 */

public class HomeFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).getTabLayout().setVisibility(View.GONE);
        ((MainActivity)getActivity()).getFabBookApp().setVisibility(View.VISIBLE);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
