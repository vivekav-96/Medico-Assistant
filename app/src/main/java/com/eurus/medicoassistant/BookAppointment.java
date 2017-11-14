package com.eurus.medicoassistant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class BookAppointment extends AppCompatActivity {

    private RecyclerView timeSlotRV;
    private RecyclerView.Adapter timeSlotRV_adapter;
    private RecyclerView.LayoutManager timeSlotRV_layoutManager;

    private ArrayList<String> dataset;

    private TextView current_selected_time;
    private TextView current_selected_slot;
    private TextView morningSelectTV;
    private TextView eveningSelectTV;
    private TextView dateTV;
    private TextView pickADoctorTV;
    private TextView pickADateTV;
    private TextView pickASlotTV;

    private Button confirmBookingButton;
    private Spinner doctorSpinner;

    final int DIAG_ID=0;
    private int year_x, month_x, day_x;

    private View.OnClickListener changeColorOfTime;

    private String selected_slot_time_of_day=null, selected_slot_time=null;

    private Drawable drawable_default, drawable_selected, drawable_default_small, drawable_selected_small;

   private  Animation animation;


    private boolean validateEntries()
    {

        int doctorSpinnerSelectedIndex=0;
        String doctor;
        String slot_time_of_day;
        String slot_time;
        int flag=0;


        if(doctorSpinnerSelectedIndex==0)
        {
            pickADoctorTV.requestFocus();
            pickADoctorTV.startAnimation(animation);
            flag++;
        }

        Toast.makeText(this, dateTV.getText(), Toast.LENGTH_LONG).show();
        if(dateTV.getText().toString().equals("Date"))
        {
            pickADateTV.requestFocus();
            pickADateTV.startAnimation(animation);
            flag++;
        }

        if(selected_slot_time_of_day==null || selected_slot_time==null)
        {
            pickASlotTV.requestFocus();
            pickASlotTV.startAnimation(animation);
            flag++;
        }

        if(flag!=0)
        {
            return false;
        }

        return true;
    }

    public DatePickerDialog.OnDateSetListener onDateSet=
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    year_x=i;
                    month_x=i1;
                    day_x=i2;
                    dateTV.setText(day_x+"/"+month_x+"/"+year_x);
                }
            };

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id==DIAG_ID)
        {
            DatePickerDialog dp_diag=new DatePickerDialog(this, onDateSet, year_x, month_x, day_x);
            dp_diag.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            return dp_diag;
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appontment);
        current_selected_slot=current_selected_time=null;

        //Instances of Drawables to be used for chip design
        drawable_default=this.getResources().getDrawable(R.drawable.shape_chip_simple_drawable_default);
        drawable_selected=this.getResources().getDrawable(R.drawable.shape_chip_simple_drawable_selected);
        drawable_default_small=this.getResources().getDrawable(R.drawable.shape_chip_simple_drawable_default_small);
        drawable_selected_small=this.getResources().getDrawable(R.drawable.shape_chip_simple_drawable_selected_small);


        //Animation
        animation = AnimationUtils.loadAnimation(this, R.anim.shake);

        //Datepicker
        Calendar calendar=Calendar.getInstance();
        year_x=calendar.get(Calendar.YEAR);
        month_x=calendar.get(Calendar.MONTH);
        day_x=calendar.get(Calendar.DAY_OF_MONTH);

        dateTV=findViewById(R.id.dateTV);
        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIAG_ID);
            }
        });
        pickADoctorTV =findViewById(R.id.pickADocTextView);
        pickADateTV =findViewById(R.id.pickADateTextView);
        pickASlotTV =findViewById(R.id.pickASlotTextView);

        changeColorOfTime=new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if(current_selected_time==(TextView) view)
                {
                    view.setBackground(drawable_default);
                    current_selected_time=null;
                    selected_slot_time_of_day=null;
                }
                else
                {
                    view.setBackground(drawable_selected);

                    if(current_selected_time!=null)
                    {
                        current_selected_time.setBackground(drawable_default);

                    }
                    current_selected_time=(TextView) view;
                    selected_slot_time_of_day=current_selected_time.getText().toString();
                }

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
        //end-Adding data to ArrayList
        timeSlotRV=(RecyclerView)findViewById(R.id.timeSlotRV);
        timeSlotRV_layoutManager = new LinearLayoutManager(BookAppointment.this, LinearLayoutManager.HORIZONTAL, false);
        timeSlotRV.setLayoutManager(timeSlotRV_layoutManager);

        timeSlotRV_adapter=new TimeSlotRVAdapter(dataset, drawable_default_small, drawable_selected_small);
        timeSlotRV.setAdapter(timeSlotRV_adapter);

        doctorSpinner=findViewById(R.id.doctorsListSpinner);

        //Confirm Booking
        confirmBookingButton=findViewById(R.id.confirmBookingButton);
        confirmBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateEntries()==true)
                {
                    //confirm booking, update database
                }
            }
        });



    }



}
