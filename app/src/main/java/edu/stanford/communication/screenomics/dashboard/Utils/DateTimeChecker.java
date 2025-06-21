/**
 * DateTimeChecker.java
 * Created by Ian Kim; Updated on 2025-06-19.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.Utils;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeChecker {


    private static final String LOS_ANGELES_TIME_ZONE = "America/Los_Angeles";
    private static final long MILLIS_IN_24_HOURS = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

    public static boolean isMoreThan24HoursAgo(String dateTimeStr) {
        // Remove extraneous words using regex
        String cleanedDateTimeStr = dateTimeStr.replaceAll("\\[.*?\\]", "").trim();

        // Define the date format
        String dateFormat = "yyyy-MM-dd HH:mm:ss";

        // Parse the cleaned date-time string
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone(LOS_ANGELES_TIME_ZONE));
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(cleanedDateTimeStr);
        } catch (ParseException e) {
            Log.e("DateTimeChecker", "Failed to parse date: " + e.getMessage());
            return false;
        }

        // Get the current date and time in Los Angeles timezone
        sdf.setTimeZone(TimeZone.getTimeZone(LOS_ANGELES_TIME_ZONE));
        Date now = new Date();

        // Calculate the time difference
        long timeDifference = now.getTime() - parsedDate.getTime();

        // Return true if the given date is more than 24 hours ago
        return timeDifference > MILLIS_IN_24_HOURS;
    }



    public static boolean isMostRecentEventWithinLast24Hours(QueryDocumentSnapshot documentSnapshot) {
        // Get the MostRecentEventTime from the document snapshot
        String dateTimeStr = documentSnapshot.getString("MostRecentEventTime");
        if (dateTimeStr == null) {
            Log.e("DateTimeChecker", "MostRecentEventTime key not found in document snapshot.");
            return false;
        }

        // Remove extraneous words using regex
        String cleanedDateTimeStr = dateTimeStr.replaceAll("\\[.*?\\]", "").trim();

        // Define the date format
        String dateFormat = "yyyy-MM-dd HH:mm:ss";

        // Parse the cleaned date-time string
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone(LOS_ANGELES_TIME_ZONE));
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(cleanedDateTimeStr);
        } catch (ParseException e) {
            Log.e("DateTimeChecker", "Failed to parse date: " + e.getMessage());
            return false;
        }

        // Get the current date and time in Los Angeles timezone
        sdf.setTimeZone(TimeZone.getTimeZone(LOS_ANGELES_TIME_ZONE));
        Date now = new Date();

        // Calculate the time difference
        long timeDifference = now.getTime() - parsedDate.getTime();

        // Return true if the given date is within the last 24 hours
        return timeDifference <= MILLIS_IN_24_HOURS;
    }


    public static String getTimeAgoString(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return "";
        }

        String cleanedDateTimeStr = dateTimeStr.replaceAll("\\[.*?\\]", "").trim();
        String dateFormat = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone(LOS_ANGELES_TIME_ZONE));

        Date parsedDate;
        try {
            parsedDate = sdf.parse(cleanedDateTimeStr);
        } catch (ParseException e) {
            Log.e("DateTimeChecker", "Failed to parse dateTimeStr: " + e.getMessage());
            return "";
        }

        long diffMillis = new Date().getTime() - parsedDate.getTime();
        long seconds = diffMillis / 1000;

        if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (seconds < 3600) {
            return (seconds / 60) + " minutes ago";
        } else if (seconds < 86400) {
            return (seconds / 3600) + " hours ago";
        } else {
            return (seconds / 86400) + " days ago";
        }
    }


}

