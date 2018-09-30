package com.coolapps.goonerpi.notes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notes: List<Note>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE from note WHERE id = :noteId")
    fun deleteById(noteId: String)

    @Query("SELECT * from note ORDER BY date")
    fun getAll(): LiveData<List<Note>>

    @Query("SELECT id, title, photo, importance from note")
    fun getAllShort(): LiveData<List<NoteShort>>

    @Query("SELECT id, title, photo, importance from note WHERE id = :noteId")
    fun getShort(noteId: String): LiveData<NoteShort>

    @Query("DELETE from note")
    fun deleteAll()

    @Query("SELECT * from note WHERE id = :noteId LIMIT 1")
    fun get(noteId: String): LiveData<Note>

    @Query("SELECT count(*) from Note")
    fun getCount(): Int


}