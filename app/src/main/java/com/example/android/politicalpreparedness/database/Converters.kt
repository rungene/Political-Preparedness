package com.example.android.politicalpreparedness.database

import androidx.room.TypeConverter
import java.util.*

//Room knows how to persist Long objects, it can use these converters to
// persist Date objects.
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}