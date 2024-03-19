package com.test.blindappproject.data.source.local

import androidx.room.TypeConverter
import com.test.blindappproject.util.DateUtil
import java.util.Date

class DateConverter {

    @TypeConverter
    fun toDate(timeStamp: String?): Date? {
        return timeStamp?.let {DateUtil.dbDateFormat.parse(it)}
    }

    @TypeConverter
    fun toTimeStamp(date: Date?): String? {
        return DateUtil.dbDateFormat.format(date)
    }
}