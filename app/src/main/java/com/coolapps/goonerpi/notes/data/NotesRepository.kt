package com.coolapps.goonerpi.notes.data

import androidx.lifecycle.LiveData
import com.coolapps.goonerpi.notes.App
import com.coolapps.goonerpi.notes.utilities.runOnBackgroundThread

class NotesRepository {

    private val noteDao = AppDatabase.getInstance(App.applicationContext()).noteDao()

    fun insert(note: Note) = runOnBackgroundThread { noteDao.insert(note) }

    fun get(noteId: String): LiveData<Note> = noteDao.get(noteId)

    fun getShort(noteId: String): LiveData<NoteShort> = noteDao.getShort(noteId)

    fun getAll(): LiveData<List<Note>> = noteDao.getAll()

    fun getAllShort(): LiveData<List<NoteShort>> = noteDao.getAllShort()

    fun update(note: Note) = runOnBackgroundThread { noteDao.update(note) }

    fun delete(note: Note) = runOnBackgroundThread { noteDao.delete(note) }

    fun deleteById(noteId: String) = runOnBackgroundThread { noteDao.deleteById(noteId) }


    companion object {

        @Volatile
        private var instance: NotesRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: NotesRepository().also { instance = it }
                }
    }
}