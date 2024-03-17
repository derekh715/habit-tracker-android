package edu.cuhk.csci3310.data

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDate

@TypeConverters
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let {
            LocalDate.ofEpochDay(value)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.let { date.toEpochDay() }
    }
}
