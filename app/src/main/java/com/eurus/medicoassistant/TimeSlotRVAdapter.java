package com.eurus.medicoassistant;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kay on 11/13/2017.
 */

class TimeSlotRVAdapter extends RecyclerView.Adapter<TimeSlotRVAdapter.ViewHolder> {


    private ArrayList<String> dataset;





    public TimeSlotRVAdapter(ArrayList<String> dataset) {
        this.dataset=dataset;

    }



    @Override
    public TimeSlotRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_timeslot, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TimeSlotRVAdapter.ViewHolder holder, int position) {
        holder.timeSlotTV.setText(dataset.get(position));
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
