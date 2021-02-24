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

    /*public static String convertToStoringDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        try {
            Date newDate = sdf.parse(date);
            sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            date = sdf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }*/

    public static Date convertToStoringDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static String convertToDisplayDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        return sdf.format(date);

    }

    /*public static String convertToDisplayDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date newDate = sdf.parse(date);
            sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
            date = sdf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }*/

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTime();
    }

    public static String convertToDecimalFormat(String value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(Double.valueOf(value));
    }

}
