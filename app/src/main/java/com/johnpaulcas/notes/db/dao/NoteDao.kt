package com.johnpaulcas.notes.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.johnpaulcas.notes.db.model.Note

/**
 * Created by johnpaulcas on 06/06/2020.
 */
@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    fun getAllNotes(): LiveData<List<Note>>

}