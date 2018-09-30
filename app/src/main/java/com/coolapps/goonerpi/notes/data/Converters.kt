package com.coolapps.goonerpi.notes.data

import androidx.room.TypeConverter
import com.coolapps.goonerpi.notes.utilities.Importance

class Converters {

    @TypeConverter
    fun fromImportance(importance: Importance): String = importance.toString()

    @TypeConverter
    fun toImportance(importance: String):Importance = Importance.valueOf(importance)
}