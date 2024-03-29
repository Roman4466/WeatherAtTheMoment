package com.example.weatherNow.model.database.converters

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}
