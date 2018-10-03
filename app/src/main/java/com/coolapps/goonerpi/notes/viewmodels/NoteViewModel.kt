package com.coolapps.goonerpi.notes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.coolapps.goonerpi.notes.App
import com.coolapps.goonerpi.notes.data.Note
import com.coolapps.goonerpi.notes.data.NoteShort


class NoteViewModel : ViewModel() {

    val repository = App.repository
    val notes: LiveData<List<Note>> = repository.getAll()
    val notesPreview: LiveData<List<NoteShort>> = repository.getAllShort()

    fun insert(note: Note) = repository.insert(note)

    fun get(noteId: String) = repository.get(noteId)

    fun update(note: Note) = repository.update(note)

    fun delete(note: Note) = repository.delete(note)

    fun delete(noteId: String) = repository.deleteById(noteId)


}