package com.coolapps.goonerpi.notes.data

import androidx.lifecycle.LiveData
import com.coolapps.goonerpi.notes.App
import com.coolapps.goonerpi.notes.utilities.runOnIoThread

class NotesRepository {

    private val noteDao = AppDatabase.getInstance(App.applicationContext()).noteDao()
    //val notes: LiveData<List<Note>> = getAll()

    fun insert(note: Note) = runOnIoThread { noteDao.insert(note) }

    fun get(noteId: String): LiveData<Note> = noteDao.get(noteId)

    fun getShort(noteId: String): LiveData<NoteShort> = noteDao.getShort(noteId)

    fun getAll(): LiveData<List<Note>> = noteDao.getAll()

    fun getAllShort(): LiveData<List<NoteShort>> = noteDao.getAllShort()

    fun update(note: Note) = runOnIoThread { noteDao.update(note) }

    fun delete(note: Note) = runOnIoThread { noteDao.delete(note) }

    fun deleteById(noteId: String) = runOnIoThread { noteDao.deleteById(noteId) }


    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: NotesRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: NotesRepository().also { instance = it }
                }
    }
}