package com.example.roazhone;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * This object represents the opening periods of a parking with a given name on the current day.
 * Opening time for each parking are contained in individual text files (raw resources).
 * It is able to retrieve if the parking is opened at the current time.
 * Used as a hotfix for the non working underground parking status in the API.
 */
public class OpeningPeriod {
    private String parkingName;
    private final int dayIndex;
    private String[] periodsList;
    private final Calendar calendar;
    private final Context context;

    public OpeningPeriod(Context context, String parkingName) {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        this.context = context;
        this.parkingName = parkingName.toLowerCase();
        this.parkingName = this.parkingName.replace("-", "_");
        this.parkingName = this.parkingName.replace("é", "e");
        this.dayIndex = getDayIndex();
        this.getPeriodsForDay();
    }

    /**
     * Gets the different opening periods of the parking on the current day. A parking can be open at several periods
     * on the same day. For example : from 00:00 to 03:00 and from 06:00 to 23:59.
     */
    private void getPeriodsForDay() {
        int resId = context.getResources().getIdentifier(parkingName, "raw", context.getPackageName());
        InputStream inputStream = this.context.getResources().openRawResource(resId);
        InputStreamReader inputreader = new InputStreamReader(inputStream);
        try {
            int i = 1;
            BufferedReader br = new BufferedReader(inputreader);
            String line = "";
            while (i <= dayIndex && (line = br.readLine()) != null) {
                i++;
            }
            br.close();
            periodsList = line.split(";");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the parking is open.
     * @return true if open, false if closed
     */
    public boolean isOpen() {
        boolean isOpen = false;
        int i = 0;
        int opening;
        int closure;
        int timeOfDay = getTimeOfDay();
        while (!isOpen && i < periodsList.length) {
            String[] openingTimesForPeriod = periodsList[i].split("-");
            if (openingTimesForPeriod.length == 2) {
                opening = Integer.parseInt(openingTimesForPeriod[0].replace(":", ""));
                closure = Integer.parseInt(openingTimesForPeriod[1].replace(":", ""));
                if (opening <= timeOfDay && timeOfDay <= closure) {
                    isOpen = true;
                }
            } else {
                i = periodsList.length - 1;
            }
            i++;
        }
        return isOpen;
    }

    /**
     * Gets the current day index from 1 to 7 (from Sunday to Saturday).
     * @return the current day index
     */
    private int getDayIndex() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Gets the current time of day as an integer (without the ":"), for an easier comparison.
     * @return the current time of day.
     */
    private Integer getTimeOfDay() {
        int ret = 0;
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        if(minutes < 10) {
            ret = Integer.parseInt(String.valueOf(hours) + String.valueOf(0) + minutes);
        }
        else {
            ret = Integer.parseInt(String.valueOf(hours) + minutes);
        }
        return ret;
    }
}
