package com.eurus.medicoassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Home extends AppCompatActivity {

    private int remainingDays;
    private TextView text_remainingDaysTV;
    private TextView remainingDaysTV;
    private TextView text_daysTV;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text_remainingDaysTV=findViewById(R.id.text_remainingDaysTV);
        remainingDaysTV=findViewById(R.id.remaingDaysTV);
        text_daysTV=findViewById(R.id.text_daysTV);
        try {
            setDisplayValues(calculateRemainingDays("",""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
