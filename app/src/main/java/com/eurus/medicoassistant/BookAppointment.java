package com.eurus.medicoassistant;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class BookAppointment extends AppCompatActivity implements RadioButton.OnClickListener,EasyPermissions.PermissionCallbacks {

    private DatabaseReference mRef;
    private CalendarView calendarView;
    private RadioButton morning, evening;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TimeSlotRVAdapter adapter;
    List<String> slotList = new ArrayList<>();
    String selected_date_str;
    ProgressBar progressBar;
    Button submit;
    TextView empty_text, already_booked_text;
    String selected = "", uid;

    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;
    Date current_datetime;
    SimpleDateFormat hour_minute_format;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    static final long FIFTEEN_MINUTE_IN_MILLIS=900000;//millisecs
    boolean isCalenderSelectedAsFirst = true , alreadyBooked = true;
    private String current_date_str;
    private String doctor_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }
        setContentView(R.layout.activity_book_appointment);
        sharedPreferences = getSharedPreferences(Utils.pref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        uid = sharedPreferences.getString("uid", "");
        mRef = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.toolbar);
        doctor_name = "Jennifer Wong";
        toolbar.setTitle(doctor_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarView = findViewById(R.id.calenderView);
        recyclerView = findViewById(R.id.recyclerView);
        morning = findViewById(R.id.morning_radiobtn);
        evening = findViewById(R.id.evening_radtobtn);
        progressBar = findViewById(R.id.progressbar);
        empty_text = findViewById(R.id.empty_text);
        already_booked_text = findViewById(R.id.already_booked_text);
        submit = findViewById(R.id.submit_appointment_btn);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selected_date_str = dayOfMonth + "-" + (month + 1) + "-" + year;
                isCalenderSelectedAsFirst = current_date_str.equals(selected_date_str);
                alreadyBookedOnDate(selected_date_str);
            }
        });

        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new TimeSlotRVAdapter(slotList, selected);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selected = adapter.selected = slotList.get(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        Date date = new Date();
        try {
            hour_minute_format = new SimpleDateFormat("hh:mm a");
            current_datetime = hour_minute_format.parse(hour_minute_format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendarView.setDate(calendar.getTimeInMillis(), false, true);
        calendarView.setMinDate(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        current_date_str = selected_date_str = sdf.format(date);

        morning.setChecked(true);
        alreadyBookedOnDate(current_date_str);
        int noOfDays = 14;
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        calendarView.setMaxDate(calendar.getTimeInMillis());
        morning.setOnClickListener(this);
        evening.setOnClickListener(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected.equals(""))
                    Toast.makeText(getApplicationContext(), "Select a time slot", Toast.LENGTH_LONG).show();
                else {
                    final Dialog dialog = new Dialog(BookAppointment.this);
                    dialog.setContentView(R.layout.dialog_submit_appointment);
                    final TextView name = dialog.findViewById(R.id.name_textview_dialog);
                    TextView line = dialog.findViewById(R.id.nextLine);
                    Button ok = dialog.findViewById(R.id.btn_ok_dialog);
                    Button cancel = dialog.findViewById(R.id.btn_cancel_dialog);
                    dialog.show();
                    name.setText(doctor_name);
                    final String batch = morning.isChecked() ? "Morning" : "Evening";
                    line.setText(selected_date_str + " " + batch + " " + selected);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            mProgress = new ProgressDialog(BookAppointment.this);
                            mProgress.setTitle("Booking an com.eurus.medicoassistant.Appointment");
                            mProgress.setMessage("This will only take a sec..");
                            mProgress.setCancelable(false);
                            mProgress.show();
                            Map<String, String> map = new HashMap<>();
                            map.put("Date", selected_date_str);
                            map.put("Time Slot", selected);
                            map.put("Doctor", doctor_name);
                            map.put("Time of Day", batch);
                            mRef.child("Appointments").child(uid).push().setValue(map, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    mRef.child("Booked Slots").child(doctor_name).child(selected_date_str).child(batch).child(selected).setValue(true);
                                    getResultsFromApi();

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
            }
        });

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    private void alreadyBookedOnDate(String selected_date_str) {
        alreadyBooked = false;
        mRef.child("Appointments").child(uid).orderByChild("Date").equalTo(selected_date_str).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postDataSnapshot : dataSnapshot.getChildren())
                {
                    String doctor = postDataSnapshot.child("Doctor").getValue(String.class);
                    if(doctor.equals(doctor_name))
                        alreadyBooked = true;
                }
                if(!alreadyBooked)
                    loadInitalData();
                else
                {
                    already_booked_text.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    empty_text.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void loadSlots(DataSnapshot dataSnapshot) {
        slotList.clear();
        if (morning.isChecked())
            slotList.addAll(Utils.morningSlots);
        else
            slotList.addAll(Utils.eveningSlots);

        checkforValidSlots(slotList);
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            String slot = postSnapshot.getKey();
            if (postSnapshot.getValue(Boolean.class)) {
                slotList.remove(slot);
            }
        }
        progressBar.setVisibility(View.GONE);
        if (slotList.size() != 0) {
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
        } else {
            empty_text.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
        }
    }

    private void checkforValidSlots(List<String> slotList) {

        for(Iterator<String> it = slotList.iterator(); it.hasNext();) {
            String slot = it.next();
            if (isCalenderSelectedAsFirst) {
                if (!isValidSlot(slot)) {
                    it.remove();
                }
            }
        }
    }


    private boolean isValidSlot(String slot) {
        try {
            Date date = hour_minute_format.parse(slot);
            return date.after(current_datetime);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return false;
        }
    }


    public void loadInitalData() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        empty_text.setVisibility(View.GONE);
        already_booked_text.setVisibility(View.GONE);
        String batch = morning.isChecked() ? "Morning" : "Evening";
        mRef.child("Booked Slots").child("Jennifer Wong").child(selected_date_str).child(batch).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DS", dataSnapshot.toString());
                loadSlots(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                empty_text.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Log.d("RBCLick", String.valueOf(v.getId()));
        switch (v.getId()) {
            case R.id.morning_radiobtn:
                evening.setChecked(false);
                break;
            case R.id.evening_radtobtn:
                morning.setChecked(false);
                break;
        }
        if(!alreadyBooked)
            loadInitalData();
    }


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Log.d("GoogleAuth","No network connection available.");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Log.d("GoogleAuth",
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }



    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                BookAppointment.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private String getDataFromApi() throws IOException, ParseException {
            // List the next 10 events from the primary calendar.
            final String batch = morning.isChecked() ? "Morning" : "Evening";
            Event event = new Event()
                    .setSummary("Doctor's appointment with " + doctor_name)
                    .setDescription("You have booked an appointment with " + doctor_name+ " on " + selected_date_str + " " +  selected);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            Date startDate = sdf.parse(selected_date_str + " " + selected);
            DateTime startDateTime = new DateTime(startDate);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("Asia/Calcutta");
            event.setStart(start);

            Date end_date = Utils.addMinutesToDate(15,startDate);
            DateTime endDateTime = new DateTime(end_date);
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("Asia/Calcutta");
            event.setEnd(end);

            String calendarId = "primary";
            event = mService.events().insert(calendarId, event).execute();
            System.out.printf("Event created: %s\n", event.getHtmlLink());
            return event.getHtmlLink();
        }


        @Override
        protected void onPostExecute(String output) {
            mProgress.dismiss();
            Toast.makeText(getApplicationContext(), "Successfully Booked", Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            BookAppointment.REQUEST_AUTHORIZATION);
                } else {
                    Log.d("GoogleAuth","The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                Log.d("GoogleAuth","Request cancelled.");
            }
        }
    }
}
