package com.eurus.medicoassistant;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sachin on 19-11-2017.
 */

public class AppointmentsListAdapter extends Adapter<RecyclerView.ViewHolder> {

    public static final int APPOINTMENT_UPCOMING = 0;
    public static final int APPOINTMENT_EXPIRED = 1;
    List<Appointment> appointments;
    private int appointmentType;
    Activity activity;
    String uid;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};
    DatabaseReference mRef;
    private GoogleAccountCredential mCredential;
    ProgressDialog mProgress;

    public AppointmentsListAdapter(List<Appointment> appointments,int appointmentType , Activity activity, String uid) {
        this.appointments = appointments;
        this.appointmentType = appointmentType;
        this.activity = activity;
        this.uid = uid;
        mRef = FirebaseDatabase.getInstance().getReference();
        mCredential = GoogleAccountCredential.usingOAuth2(
                activity, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        String accountName = activity.getSharedPreferences(Utils.pref,Context.MODE_PRIVATE)
                .getString("Account Name", null);

        mCredential.setSelectedAccountName(accountName);
        mProgress = new ProgressDialog(activity);
        mProgress.setTitle("Cancelling the Appointment");
        mProgress.setMessage("This will only take a sec..");
        mProgress.setCancelable(false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == APPOINTMENT_UPCOMING)
            return new UpComingHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_appointment_upcoming_card, parent, false));
        else if(viewType == APPOINTMENT_EXPIRED)
            return new HistoryHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_appointment_card, parent, false));
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof UpComingHolder)
        {
            Appointment appointment = appointments.get(position);
            UpComingHolder upComingHolder = (UpComingHolder) holder;
            upComingHolder.doctorTV.setText(appointment.getDoctor());
            upComingHolder.dateTV.setText(appointment.getDate());
            upComingHolder.timeTV.setText(appointment.getTimeSlot());
            upComingHolder.remainingTV.setText(appointment.getRemaining());
        }
        else if(holder instanceof HistoryHolder)
        {
            Appointment appointment = appointments.get(position);
            HistoryHolder historyHolder = (HistoryHolder) holder;
            historyHolder.doctorTV.setText(appointment.getDoctor());
            historyHolder.dateTV.setText(appointment.getDate());
            historyHolder.timeTV.setText(appointment.getTimeSlot());
            historyHolder.dayOfWeekTV.setText(appointment.getDayOfWeek());
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    @Override
    public int getItemViewType(int position) {
        return appointmentType;
    }

    public class UpComingHolder extends RecyclerView.ViewHolder {

        TextView doctorTV,dateTV,timeTV,remainingTV;
        TextView cancel;
        public UpComingHolder(View itemView) {
            super(itemView);
            doctorTV = itemView.findViewById(R.id.doctorTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            remainingTV = itemView.findViewById(R.id.remainingTV);
            cancel = itemView.findViewById(R.id.cancelAppointmentButtion);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Appointment appointment = appointments.get(getLayoutPosition());
                    appointments.remove(appointment);
                    createDialog(appointment);
                    if(appointments.size()==0)
                        activity.findViewById(R.id.empty_appointments_text).setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {
        TextView doctorTV,dateTV,timeTV,dayOfWeekTV;
        TextView bookFollowUpButton;
        public HistoryHolder(final View itemView) {
            super(itemView);
            doctorTV = itemView.findViewById(R.id.doctorTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            dayOfWeekTV = itemView.findViewById(R.id.dayOfWeekTV);
            bookFollowUpButton = itemView.findViewById(R.id.bookFollowUpTV);

            bookFollowUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,BookAppointment.class);
                    intent.putExtra("Name",appointments.get(getLayoutPosition()).getDoctor());
                    activity.startActivity(intent);
                }
            });
        }
    }

    private void createDialog(final Appointment appointment) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_submit_appointment);
        final TextView name = dialog.findViewById(R.id.name_textview_dialog);
        TextView line = dialog.findViewById(R.id.nextLine);
        Button ok = dialog.findViewById(R.id.btn_ok_dialog);
        Button cancel = dialog.findViewById(R.id.btn_cancel_dialog);
        dialog.show();
        name.setText(appointment.getDoctor());
        line.setText(appointment.getDate() + " " + appointment.getTimeSlot());
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                mProgress.show();
                mRef.child("Appointments").child(uid).orderByChild("Date").equalTo(appointment.getDate()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postDataSnapshot : dataSnapshot.getChildren())
                        {
                            String name = postDataSnapshot.child("Doctor").getValue(String.class);
                            final String calender_id = postDataSnapshot.child("Calendar ID").getValue(String.class);
                            Log.d("DS",postDataSnapshot.toString());
                            if(name.equals(appointment.getDoctor()))
                            {
                                mRef.child("Booked Slots").child(appointment.getDoctor()).child(appointment.getDate()).child(appointment.getTimeOfDay()).child(appointment.getTimeSlot()).setValue(null, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        new MakeRequestTask().execute(calender_id);
                                    }
                                });
                                postDataSnapshot.getRef().setValue(null);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private class MakeRequestTask extends AsyncTask<String, Void, Void> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;


        @Override
        protected Void doInBackground(String... params) {
            try {
                getDataFromApi(params[0]);
                return null;
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private void getDataFromApi(String event_id) throws IOException, ParseException {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            Calendar service = new Calendar.Builder(transport, jsonFactory, mCredential)
                    .setApplicationName("Google Calendar API Android Quickstart").build();
            try {
                service.events().delete("primary",event_id).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(Void output) {
            mProgress.dismiss();

        }
    }

}
