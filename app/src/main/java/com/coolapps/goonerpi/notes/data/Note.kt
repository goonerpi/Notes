package com.coolapps.goonerpi.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coolapps.goonerpi.notes.utilities.Importance

@Entity
data class Note(
        @PrimaryKey val id: String,
        val title: String,
        val body: String,
        val importance: Importance,
        val photo: String,
        val coordinates: String,
        val date: Long
)

