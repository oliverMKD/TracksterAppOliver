package com.trackster.tracksterapp.utils

import android.text.TextUtils
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

enum class DateFormat(val format: String) {

    DATE_FORMAT_MESSAGE("MMM dd, HH:mm"),
    DATE_FORMAT_MESSAGE_DETAILS("EEEE, MMMM dd"),
    TIME_FORMAT_MESSAGE_DETAILS("HH:mm");

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS Z", Locale.US)

        fun formatDate(value: String?, dateFormat: DateFormat): String {
            if (value == null)
                return ""

            val date: Date?
            try {
                date = DateFormat.DATE_FORMAT.parse(value)
            } catch (e: ParseException) {
                Log.e("TVChat", String.format(Locale.US, "Could not parse date %s", value))
                return value
            }

            return SimpleDateFormat(dateFormat.format, Locale.getDefault()).format(date)
        }

        fun formatDateDetailsMessage(dateString: String, returnDate: String): String {
            val nowDate = Calendar.getInstance()
            val pastDate = Calendar.getInstance()
            try {
                pastDate.time = DateFormat.DATE_FORMAT.parse(dateString)
            } catch (e: ParseException) {
                Log.e("TVChat", String.format(Locale.US, "Could not parse date %s", dateString))
            }

            if (nowDate.get(Calendar.YEAR) == pastDate.get(Calendar.YEAR)) {
                if (nowDate.get(Calendar.MONTH) == pastDate.get(Calendar.MONTH)) {
                    if (nowDate.get(Calendar.DAY_OF_MONTH) == pastDate.get(Calendar.DAY_OF_MONTH)) {
                        if (!TextUtils.isEmpty(dateString)) {
                            return "today"
                        }
                    } else if (nowDate.get(Calendar.DAY_OF_MONTH) - 1 == pastDate.get(Calendar.DAY_OF_MONTH)) {
                        if (!TextUtils.isEmpty(dateString)) {
                            return "yesterday"
                        }
                    }
                }
            }

            return returnDate
        }
    }

}