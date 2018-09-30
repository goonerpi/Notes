package com.coolapps.goonerpi.notes.data

import com.coolapps.goonerpi.notes.utilities.Importance

data class NoteShort(
        val id: String,
        val title: String,
        val photo: String,
        val importance: Importance
)