package com.eurus.medicoassistant;

/**
 * Created by Kay on 11/18/2017.
 */

public class Appointment {

    private String date;
    private String doctor;
    private String timeSlot;
    private String timeOfDay;


    public String getDate() {
        return date;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }
}

