package com.adivid.mvvmexpensedairy.utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class Utils {

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getDisplayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static Date convertToStoringDate(String date, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy HH:mm", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = sdf.parse(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static String convertToDisplayDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

        return sdf.format(date);

    }

    public static String convertToDecimalFormat(String value) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(Double.valueOf(value));
    }

    public static Date getFirstDayOfMonth(String stringDate) {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);

       /* SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());*/
        return c.getTime();
    }

    public static Date getLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static Date getFirstDayOfMonthFromMonthYear(String stringMonthYear) {
        stringMonthYear = "01 " + stringMonthYear;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = sdf.parse(stringMonthYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static String getMonthAndYear() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());
        return df.format(c.getTime());
    }

}
