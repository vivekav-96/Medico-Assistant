package com.eurus.medicoassistant;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAppontment extends AppCompatActivity {

    private RecyclerView timeSlotRV;
    private RecyclerView.Adapter timeSlotRV_adapter;
    private RecyclerView.LayoutManager timeSlotRV_layoutManager;
    private ArrayList<String> dataset;
    private TextView current_selected_time;
    private TextView current_selected_slot;

    private TextView morningSelectTV;
    private TextView eveningSelectTV;
    View.OnClickListener changeColorOfTime;

    Drawable drawable_default, drawable_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appontment);
        current_selected_slot=current_selected_time=null;
        drawable_default=this.getResources().getDrawable(R.drawable.shape_chip_simple_drawable_default);
        drawable_selected=this.getResources().getDrawable(R.drawable.shape_chip_simple_drawable_selected);
        changeColorOfTime=new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if(current_selected_time==(TextView) view)
                {
                   view.setBackground(drawable_default);
                }
                else
                {
                    view.setBackground(drawable_selected);
                    if(current_selected_time!=null)
                    {
                        current_selected_time.setBackground(drawable_default);

                    }
                }
                current_selected_time=(TextView) view;
            }
        };
        morningSelectTV=findViewById(R.id.morningSelectChip);
        eveningSelectTV=findViewById(R.id.eveningSelectChip);
        morningSelectTV.setOnClickListener(changeColorOfTime);
        eveningSelectTV.setOnClickListener(changeColorOfTime);




        //Addding data to ArrayList
        dataset=new ArrayList<String>();
        dataset.add("9:00 AM");
        dataset.add("9:15 AM");
        dataset.add("9:30 AM");
        dataset.add("9:45 AM");
        dataset.add("10:00 AM");
        dataset.add("10:15 AM");
        dataset.add("10:30 AM");
        dataset.add("10:45 AM");
        dataset.add("11:00 AM");
        dataset.add("11:15 AM");
        dataset.add("11:30 AM");
        dataset.add("11:45 AM");
        //end-Addding data to ArrayList
        timeSlotRV=(RecyclerView)findViewById(R.id.timeSlotRV);
        timeSlotRV_layoutManager = new LinearLayoutManager(BookAppontment.this, LinearLayoutManager.HORIZONTAL, false);
        timeSlotRV.setLayoutManager(timeSlotRV_layoutManager);

        timeSlotRV_adapter=new TimeSlotRVAdapter(dataset);
        timeSlotRV.setAdapter(timeSlotRV_adapter);



    }


}
