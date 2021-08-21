package com.roshanadke.mvvmexpensedairy.utils;

import static com.roshanadke.mvvmexpensedairy.utils.Constants.KEY_DELETE_UNIQUE_WORK;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static void syncDeletedRecords(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(DeleteDataWorker.class)
                .setConstraints(constraints)
                .addTag(KEY_DELETE_UNIQUE_WORK)
                .build();
        WorkManager.getInstance(context).enqueueUniqueWork(KEY_DELETE_UNIQUE_WORK,
                ExistingWorkPolicy.KEEP, request);
    }

}
