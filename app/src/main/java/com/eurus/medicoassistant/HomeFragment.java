package com.eurus.medicoassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    private int remainingDays;
    private TextView text_remainingDaysTV;
    private TextView remainingDaysTV;
    private TextView text_daysTV;
    private TextView default_text;

    private int calculateRemainingDays(String startDate, String endDate) throws ParseException

    {
        //startDate and endDate should be in format YYYY MM DD (space-separated)

        startDate=startDate.trim().replace(' ','-');
        endDate=endDate.trim().replace(' ','-');
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
        Date start=sdf.parse(startDate);
        Date end=sdf.parse(endDate);
        long difference=end.getTime()-start.getTime();
        difference= TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
        return  (int)difference;
    }

    private void setDisplayValues(int difference)
    {
        default_text.setVisibility(View.GONE);
        String text_remainingDays="", remainingDays="", text_days="";

        switch (difference)
        {
            case 0: text_remainingDays="YOUR APPOINTMENT IS";
                    remainingDays="TODAY";
                remainingDaysTV.setPadding(0,90,0,0);
                    remainingDaysTV.setTextSize(70);
                    break;
            case 1: text_remainingDays="YOUR APPOINTMENT IS";
                    remainingDays="TOMMOROW";
                remainingDaysTV.setTextSize(50);
                remainingDaysTV.setPadding(0,90,0,0);
                    break;
            default: text_remainingDays="NEXT APPOINTMENT IN";
                    remainingDays=""+difference;
                remainingDaysTV.setTextSize(110);
                    text_days="DAYS";

        }
        text_remainingDaysTV.setText(text_remainingDays);
        remainingDaysTV.setText(remainingDays);
        text_daysTV.setText(text_days);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).getTabLayout().setVisibility(View.GONE);
        ((MainActivity)getActivity()).getFabBookApp().setVisibility(View.VISIBLE);

        text_remainingDaysTV=(view.findViewById(R.id.text_remainingDaysTV));
        remainingDaysTV=view.findViewById(R.id.remaingDaysTV);
        default_text=view.findViewById(R.id.text_default);
        text_daysTV=view.findViewById(R.id.text_daysTV);
        try {
            setDisplayValues(calculateRemainingDays("2017 11 19","2017 11 21"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
