package com.roshanadke.mvvmexpensedairy.utils

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun convertToDisplayDate(date: Date): String? {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    return sdf.format(date)
}

fun convertMillisToDate(timeInMillis: Long): String? {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(timeInMillis)
}

fun getCurrentDisplayDate(): String? {
    return convertToDisplayDate(Date(System.currentTimeMillis()))
}

fun convertDateStringToMillis(dateString: String): Long? {
    try {
        return if(dateString.isEmpty()) {
            return null
        } else {
            val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdf.parse(dateString)
            date?.time
        }
    }catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}