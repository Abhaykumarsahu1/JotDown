package com.example.jotdown.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {
    //its the interface that is data accessing object helps to get all notes, update,insert,delete,delete all etc...
    @Query("SELECT * FROM Notes")
    fun getAllNotes() : LiveData<List<Notes>>
    //live data here updates the ui whenever the underlying data gets deleted,updated or deleted etc.
    @Query("SELECT * FROM Notes WHERE noteId = :id")
    fun getNoteById(id: Int): Notes?
    @Insert
    suspend fun insertNotes(notes: Notes)

    @Update
    suspend fun updateNotes(notes: Notes)

    @Delete
    suspend fun deleteNotes(notes: Notes)

    //for deleting whole notes
    @Query("DELETE FROM Notes")
    suspend fun deleteAll()
}