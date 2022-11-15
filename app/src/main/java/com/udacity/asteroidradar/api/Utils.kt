package com.udacity.asteroidradar.api

import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {
        fun formattedString
                    ( date: Date , format: String , locale : Locale = Locale.getDefault()
        ) : String {
            val format = SimpleDateFormat(format, locale)
            return format.format(date)
        }

        fun addDaysToDate(date: Date, daysToAdd: Int) : Date {
            val calender = Calendar.getInstance()
            calender.add(Calendar.DATE, daysToAdd)
            calender.time = date
            return calender.time
        }
    }

    enum class AsteroidFilter(val value: String) {
        SHOW_SAVED("saved"), SHOW_TODAY("today"), SHOW_WEEK("week") }

}
