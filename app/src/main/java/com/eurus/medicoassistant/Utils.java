package com.eurus.medicoassistant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sachin on 22/10/17.
 */

public class Utils {
    public static String pref = "prefs";
    public static List<String> morningSlots = new ArrayList<String>() {
        {
            add("09:00 AM");
            add("09:15 AM");
            add("09:30 AM");
            add("09:45 AM");
            add("10:00 AM");
            add("10:15 AM");
            add("10:30 AM");
            add("10:45 AM");
            add("11:00 AM");
            add("11:15 AM");
            add("11:30 AM");
            add("11:45 AM");
        }
    };

    public static List<String> eveningSlots = new ArrayList<String>() {
        {
            add("03:00 PM");
            add("03:15 PM");
            add("03:30 PM");
            add("03:45 PM");
            add("04:00 PM");
            add("04:15 PM");
            add("04:30 PM");
            add("04:45 PM");
            add("05:00 PM");
            add("05:15 PM");
            add("05:30 PM");
            add("05:45 PM");
        }
    };

    public static Date addMinutesToDate(int minutes, Date beforeTime){
        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    public static int differenceInDaysBetweenDates(Date d1,Date d2)
    {
        int diffInDays = (int) ((d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24));
        return Math.abs(diffInDays);
    }

    public static String getDayofWeek(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek)
        {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "";
        }
    }
}
