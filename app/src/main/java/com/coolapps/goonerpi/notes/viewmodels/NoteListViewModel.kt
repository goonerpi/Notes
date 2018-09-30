package com.coolapps.goonerpi.notes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.coolapps.goonerpi.notes.App
import com.coolapps.goonerpi.notes.data.NoteShort

class NoteListViewModel : ViewModel() {

    private val notesRepository = App.repository

    val notes: LiveData<List<NoteShort>> = notesRepository.getAllShort()


}