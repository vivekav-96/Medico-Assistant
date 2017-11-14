package com.eurus.medicoassistant;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TimeSlotRVAdapter extends RecyclerView.Adapter<TimeSlotRVAdapter.ViewHolder> {


    private ArrayList<String> dataset;

    private Drawable drawable_default;
    private Drawable drawable_selected;
    private TextView current_selected;
    public String selectedSlot=null;

     TimeSlotRVAdapter(ArrayList<String> dataset, Drawable drawable_default, Drawable drawable_selected) {
        this.dataset=dataset;
        this.drawable_default=drawable_default;
        this.drawable_selected=drawable_selected;
        current_selected=null;
    }



    @Override
    public TimeSlotRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_timeslot, parent, false);

        ViewHolder vh = new ViewHolder(v);
        v.findViewById(R.id.timeSlotTV).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                TextView t_current=(TextView)view;
                if(current_selected==t_current)
                {
                    t_current.setBackground(drawable_default);
                    current_selected=null;
                    selectedSlot=null;
                }
                else
                {
                    t_current.setBackground(drawable_selected);

                    if(current_selected!=null)
                    {
                        current_selected.setBackground(drawable_default);

                    }
                    current_selected=t_current;
                    selectedSlot=current_selected.getText().toString();
                    BookAppointment.selectedTimeSlot=selectedSlot;
                }

            }
        });
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(TimeSlotRVAdapter.ViewHolder holder, int position) {
        holder.timeSlotTV.setText(dataset.get(position));
        holder.setIsRecyclable(false);
        if(holder.timeSlotTV.getText().toString().equals(selectedSlot))
        {
            holder.timeSlotTV.setBackground(drawable_selected);
            current_selected=holder.timeSlotTV;

        }


    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView timeSlotTV;

        public ViewHolder(View itemView) {
            super(itemView);
            timeSlotTV= itemView.findViewById(R.id.timeSlotTV);
        }
    }


}
