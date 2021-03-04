package com.example.roazhone;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.TimeZone;

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

    private int getDayIndex() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

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
