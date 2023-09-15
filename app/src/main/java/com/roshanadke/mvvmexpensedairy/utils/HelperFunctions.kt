package com.roshanadke.mvvmexpensedairy.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertToDisplayDate(date: Date?): String? {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    return sdf.format(date)
}
