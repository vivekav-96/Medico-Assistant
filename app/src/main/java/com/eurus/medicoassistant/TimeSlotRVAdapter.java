package com.eurus.medicoassistant;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotRVAdapter extends RecyclerView.Adapter<TimeSlotRVAdapter.MyViewHolder> {


    private List<String> dataset;
    public String selected;

     TimeSlotRVAdapter(List<String> dataset , String selected) {
        this.dataset=dataset;
        this.selected = selected;
    }



    @Override
    public TimeSlotRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_timeslot, parent, false);
        return new MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(TimeSlotRVAdapter.MyViewHolder holder, int position) {
        if(selected.equals(dataset.get(position)))
        {
            holder.timeSlotTV.setBackgroundResource(R.drawable.shape_chip_simple_drawable_selected);
            holder.timeSlotTV.setTextColor(Color.WHITE);
        }
        else
        {
            holder.timeSlotTV.setBackgroundResource(R.drawable.shape_chip_simple_drawable_default);
            holder.timeSlotTV.setTextColor(Color.BLACK);
        }
        holder.timeSlotTV.setText(dataset.get(position));
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView timeSlotTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            timeSlotTV= itemView.findViewById(R.id.timeSlotTV);
        }
    }


}
